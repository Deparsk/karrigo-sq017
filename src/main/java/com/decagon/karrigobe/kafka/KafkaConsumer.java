package com.decagon.karrigobe.kafka;

import com.decagon.karrigobe.entities.enums.TaskStatus;
import com.decagon.karrigobe.entities.model.DriverTaskEntity;
import com.decagon.karrigobe.entities.model.UserEntity;
import com.decagon.karrigobe.exceptions.TaskNotFoundException;
import com.decagon.karrigobe.exceptions.UserNotFoundException;
import com.decagon.karrigobe.repositories.DriverTaskRepository;
import com.decagon.karrigobe.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KafkaConsumer {
    private final DriverTaskRepository driverTaskRepo;
    private final UserRepository userRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "driverUnavailableStatus")
  public void consume (String message){
        LOGGER.info(String.format("Message received -> %s", message));
  }

  @KafkaListener(topics = "driverAvailableStatus")
  public void consumeStatus(String message){
        LOGGER.info(String.format("Message received -> %s", message));
  }
  @KafkaListener(topics = "assignedTask")
  public void consumeTaskDetailsMessage(String message){
       LOGGER.info(String.format("Message received -> %s", message));
  }

}
