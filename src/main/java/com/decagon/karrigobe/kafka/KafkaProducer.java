package com.decagon.karrigobe.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUnavailableMessage(){
        String message = "DRIVER IS BUSY";
        kafkaTemplate.send("driverUnavailableStatus",message);
    }

    public void sendAvailableMessage(){
        String message = "ORDER HAS BEEN ASSIGNED TO A DRIVER";
        kafkaTemplate.send("driverAvailableStatus",message);
    }

    public void sendTaskDetailsMessageToAssignedDriver(String driverEmail, Long taskId) {
        String message = "you have been assigned the following task " + driverEmail +" with task ID " + taskId;
        kafkaTemplate.send("assignedTask", message);
    }
}
