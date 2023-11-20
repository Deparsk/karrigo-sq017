package com.decagon.karrigobe.repositories;

import com.decagon.karrigobe.entities.model.DriverTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverTaskRepository extends JpaRepository<DriverTaskEntity, Long> {
}
