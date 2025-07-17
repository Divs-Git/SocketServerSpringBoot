package com.example.ubersocketserver.controllers;

import com.example.ubersocketserver.dtos.ChatRequestDto;
import com.example.ubersocketserver.dtos.ChatResponseDto;
import com.example.ubersocketserver.dtos.TestRequestDto;
import com.example.ubersocketserver.dtos.TestResponseDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponseDto pingCheck(TestRequestDto requestDto) {
        System.out.println("Received message from client: " + requestDto.getData());
        return new TestResponseDto("Received");
    }

//    @Scheduled(fixedDelay = 2000)
//    public void sendPeriodicMethod() {
//        simpMessagingTemplate.convertAndSend("/topic/schedule","Periodic message from server: " + System.currentTimeMillis());
//    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public ChatResponseDto chatResponse(ChatRequestDto request) {
        return ChatResponseDto.builder()
                                .name(request.getName())
                                .message(request.getMessage())
                                .timestamp("" + System.currentTimeMillis())
                                .build();
    }
}
