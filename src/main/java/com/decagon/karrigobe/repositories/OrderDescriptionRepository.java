package com.decagon.karrigobe.repositories;

import com.decagon.karrigobe.entities.model.OrderDescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDescriptionRepository extends JpaRepository<OrderDescriptionEntity, Long> {
}
