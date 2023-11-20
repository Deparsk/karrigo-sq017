package com.decagon.karrigobe.services.user_service.serviceImplementation;

import com.decagon.karrigobe.entities.enums.ItemCategory;
import com.decagon.karrigobe.entities.enums.OrderStatus;
import com.decagon.karrigobe.entities.enums.TransactStatus;
import com.decagon.karrigobe.entities.model.OrderDescriptionEntity;
import com.decagon.karrigobe.entities.model.OrderEntity;
import com.decagon.karrigobe.entities.model.TransactionEntity;
import com.decagon.karrigobe.entities.model.UserEntity;
import com.decagon.karrigobe.exceptions.OrderNotFoundException;
import com.decagon.karrigobe.exceptions.UserNotFoundException;
import com.decagon.karrigobe.payload.request.OrderDescriptionRequest;
import com.decagon.karrigobe.payload.response.TransactionResponse;
import com.decagon.karrigobe.repositories.OrderRepository;
import com.decagon.karrigobe.repositories.TransactionRepository;
import com.decagon.karrigobe.repositories.UserRepository;
import com.decagon.karrigobe.services.driver_service.DriverService;
import com.decagon.karrigobe.services.user_service.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import static com.decagon.karrigobe.commons.CostConstants.*;
import static com.decagon.karrigobe.utils.CustomIdGenerator.trackingIdGenerator;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final DriverService driverService;

    /**
     * formula =>  Total cost = COST_PER_KM * KM + COST_PER_KG * KG + COST_PER_50K *(declaredPrice/50,000) +COST_FRAGILE + COST_PERISHABLE + COST_DOCUMENT
     * */

    @Transactional
    @Override
    public TransactionResponse createOrder(OrderDescriptionRequest orderDesc) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepo.findUserEntityByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User not found!"));

        OrderDescriptionEntity orderDescription = OrderDescriptionEntity.builder()
                .itemName(orderDesc.getItemName())
                .itemDescription(orderDesc.getItemDescription())
                .declaredPrice(orderDesc.getDeclaredPrice())
                .itemWeight(orderDesc.getItemWeight())
                .itemCategory(ItemCategory.valueOf(orderDesc.getItemCategory().toUpperCase()))
                .pickUpLocation(orderDesc.getPickUpLocation())
                .dropOffLocation(orderDesc.getDropOffLocation())
                .distance(orderDesc.getDistance())
                .build();

        OrderEntity order = OrderEntity.builder()
                .status(OrderStatus.PENDING)
                .trackingNum("KG"+trackingIdGenerator())
                .userEntity(user)
                .orderDescriptionEntity(orderDescription)
                .build();

        OrderEntity savedOder = orderRepo.save(order);

        deleteOrder(savedOder.getId());

        BigDecimal totalPrice = calcTotalCost(orderDesc.getDistance(),
                                          orderDesc.getItemWeight(),
                                          orderDesc.getDeclaredPrice(),
                                          orderDesc.getItemCategory());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String price = formatter.format(totalPrice);

        String priceDeclared = formatter.format(orderDesc.getDeclaredPrice());

        String receipt = generateReceipt(savedOder.getTrackingNum(), orderDesc.getItemName(),
                                         orderDesc.getItemDescription(), orderDesc.getItemWeight(),
                                         priceDeclared, orderDesc.getItemCategory(),
                                         orderDesc.getPickUpLocation(), orderDesc.getDropOffLocation(),
                                         price);

        TransactionEntity transaction = TransactionEntity.builder()
                .amount(totalPrice)
                .receipt(receipt)
                .orderEntity(savedOder)
                .status(TransactStatus.PENDING)
                .build();

        TransactionEntity savedTransaction = transactionRepo.save(transaction);

        return TransactionResponse.builder()
                .transactionId(savedTransaction.getId())
                .amount(price)
                .receipt(receipt)
                .trackingNum(savedOder.getTrackingNum())
                .time(savedTransaction.getDateCreated())
                .build();
    }

    @Override
    public String cancelOder(Long orderId) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order not found."));

        order.setStatus(OrderStatus.CANCELED);

        orderRepo.save(order);
        return "Order canceled successfully.";
    }

    private BigDecimal calcTotalCost(Double distance, Double weight, Double declaredPrice, String itemCategory){
        return BigDecimal.valueOf(COST_PER_KM * distance)
                .add(BigDecimal.valueOf(weight > 0 ? COST_PER_KG * weight : 0))
                .add(BigDecimal.valueOf(COST_PER_50K * (declaredPrice/50_000.0)))
                .add(BigDecimal.valueOf(itemCategory.equalsIgnoreCase("FRAGILE") ? COST_FRAGILE
                        : itemCategory.equalsIgnoreCase("PERISHABLES") ? COST_PERISHABLE
                        : itemCategory.equalsIgnoreCase("DOCUMENTS") ? COST_DOCUMENT : 0));
    }

    private String generateReceipt(String trackingId, String itemName, String description,
                                   Double weight, String declaredPrice, String category,
                                   String pickupLocation, String dropOffLocation, String deliveryCost){

        return String.format("""
                ********** KarriGo Delivery Company **********
                Tracking Number: %s
                Item Name: %s
                Description: %s
                Weight: %s kg
                Declared price: %s
                Item category: %s
                Pickup location: %s
                Drop-off location: %s
                Cost of delivery: %s
                **********************************************
                """,trackingId, itemName, description, weight, declaredPrice, category, pickupLocation, dropOffLocation, deliveryCost);
    }


    public void deleteOrder(Long orderId){
        new Thread(()-> {
            long minutes = 1L;
            Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * minutes);

            while (!new Date().after(expireDate)) {
            }

            OrderEntity orderEntity = orderRepo.findById(orderId)
                    .orElseThrow(()-> new OrderNotFoundException("Order not found"));

            if (!orderEntity.getStatus().equals(OrderStatus.CANCELED)){
                orderEntity.setStatus(OrderStatus.ORDER_CONFIRMED);
                orderRepo.save(orderEntity);

                driverService.generateRandomOrder();
            }
        }).start();
    }
}
