package com.example.ubersocketserver.dtos;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideResponseDto {

    private Boolean response;

    public Long bookingId;
}
