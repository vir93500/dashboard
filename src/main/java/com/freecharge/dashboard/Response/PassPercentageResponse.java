package com.freecharge.dashboard.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PassPercentageResponse {
    private double passPercentage;
    private double failPercentage;
    private double skipPercentage;
}
