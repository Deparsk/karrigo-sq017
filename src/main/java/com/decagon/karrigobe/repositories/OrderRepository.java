package com.decagon.karrigobe.repositories;

import com.decagon.karrigobe.entities.enums.OrderStatus;
import com.decagon.karrigobe.entities.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByStatus(OrderStatus status);
}
