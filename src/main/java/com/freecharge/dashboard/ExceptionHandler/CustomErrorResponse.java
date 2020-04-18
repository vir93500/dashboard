package com.freecharge.dashboard.ExceptionHandler;


import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomErrorResponse {
    private String Error;
    private String Status;


}
