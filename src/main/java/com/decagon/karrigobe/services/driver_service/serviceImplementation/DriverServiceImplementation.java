package com.decagon.karrigobe.services.driver_service.serviceImplementation;

import com.decagon.karrigobe.entities.model.DriverTaskEntity;
import com.decagon.karrigobe.entities.model.OrderEntity;
import com.decagon.karrigobe.entities.model.UserEntity;
import com.decagon.karrigobe.exceptions.TaskNotFoundException;
import com.decagon.karrigobe.exceptions.UserNotFoundException;
import com.decagon.karrigobe.kafka.KafkaProducer;
import com.decagon.karrigobe.repositories.DriverTaskRepository;
import com.decagon.karrigobe.repositories.OrderRepository;
import com.decagon.karrigobe.repositories.UserRepository;
import com.decagon.karrigobe.services.driver_service.DriverService;
import com.decagon.karrigobe.services.driver_service.DriverTaskChoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.decagon.karrigobe.entities.enums.DriverStatus.AVAILABLE;
import static com.decagon.karrigobe.entities.enums.DriverStatus.UNAVAILABLE;
import static com.decagon.karrigobe.entities.enums.OrderStatus.ORDER_CONFIRMED;
import static com.decagon.karrigobe.entities.enums.Roles.DRIVER;
import static com.decagon.karrigobe.entities.enums.TaskStatus.REJECTED;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverServiceImplementation implements DriverService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final DriverTaskRepository driverTaskRepo;
    private final KafkaProducer kafkaProducer;
    private final DriverTaskChoice driverTaskChoice;

    @Override
    public void generateRandomOrder() {
        List<OrderEntity> allConfirmedOrders = orderRepository.findAllByStatus(ORDER_CONFIRMED);
        List<UserEntity> drivers = userRepository.findByRolesAndDriverStatus(DRIVER, AVAILABLE);
        Random random = new Random();
        OrderEntity customerOrder = (allConfirmedOrders.size() == 1) ? allConfirmedOrders.get(0) :
                allConfirmedOrders.get(random.nextInt(0,allConfirmedOrders.size()-1));

        if (!drivers.isEmpty()){
            assignTaskToDriver(drivers, customerOrder);
        }
        else {
            kafkaProducer.sendUnavailableMessage();
        }
    }

    @Override
    public void assignTaskToDriver(List<UserEntity> driverList, OrderEntity order) {
        new Thread(() -> {
            Random random = new Random();
            long startTime = System.currentTimeMillis();
            long timeLimit =  5 * 60 * 1000;


            UserEntity driver = (driverList.size() == 1) ? driverList.get(0) :
                    driverList.get(random.nextInt(0, driverList.size()-1));

            driverList.remove(driver);

            DriverTaskEntity task = new DriverTaskEntity();
            task.addOrder(order);
            driver.addToDriverTask(task);
            driver.setDriverStatus(UNAVAILABLE);
            UserEntity savedDriver = userRepository.save(driver);

            savedDriver.setDriverTaskEntities(List.of(task));
            kafkaProducer.sendTaskDetailsMessageToAssignedDriver(savedDriver.getEmail(), savedDriver.getDriverTaskEntities().get(0).getId());

            while (System.currentTimeMillis() - startTime <= timeLimit) {
            }

            Long taskId = savedDriver.getDriverTaskEntities().get(0).getId();
            DriverTaskEntity driverTask = driverTaskRepo.findById(taskId)
                    .orElseThrow(()-> new TaskNotFoundException("Task not found."));

            if (driverTask.getTaskStatus().equals(REJECTED) && !driverList.isEmpty()) {
                assignTaskToDriver(driverList, order);
            } else if (driverTask.getTaskStatus().equals(REJECTED)) {
                kafkaProducer.sendUnavailableMessage();
                // TODO: tell the user that no driver is available at the moment . And notify the management via email
            }
            else {
                kafkaProducer.sendAvailableMessage();
                log.info("Task id:------------->"+savedDriver.getDriverTaskEntities().get(0).getId());
                log.info("Driver email:----------->"+driver.getEmail());
            }
        }).start();
    }

    @Override
    public List<DriverTaskEntity> viewAllOrdersInDriversTask() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity driver = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Driver not found"));

        return driver.getDriverTaskEntities();
    }
}
