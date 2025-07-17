package com.example.ubersocketserver.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {

    private String name;

    private String message;

    private String timestamp;
}
