package com.example.ubersocketserver.controllers;

import com.example.uberprojectentityservice.models.BookingStatus;
import com.example.ubersocketserver.dtos.*;
import com.example.ubersocketserver.producers.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/socket")
public class DriverRequestController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate,KafkaProducerService kafkaProducerService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = new RestTemplate();
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/new-ride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto) {
        sendDriverNewRideRequest(requestDto);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    private void sendDriverNewRideRequest(RideRequestDto requestDto) {
        simpMessagingTemplate.convertAndSend("/topic/ride-request", requestDto);
    }

    @MessageMapping("/ride-response/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId, RideResponseDto responseDto) {

        System.out.println(responseDto.getResponse() + " " + userId);
        UpdateBookingDto request = UpdateBookingDto.builder()
                .driverId(Optional.of(Long.parseLong(userId)))
                .status(BookingStatus.SCHEDULED)
                .build();
        ResponseEntity<UpdateBookingResponseDto> result =  this.restTemplate.postForEntity("http://localhost:8080/api/v1/booking/" + responseDto.getBookingId(), request, UpdateBookingResponseDto.class);
        kafkaProducerService.publishMessage("sample-topic", "Hello");
        System.out.println(result.getStatusCode());
    }

    @GetMapping
    public boolean help() {
        kafkaProducerService.publishMessage("sample-topic", "Hello");
        return true;
    }
}
