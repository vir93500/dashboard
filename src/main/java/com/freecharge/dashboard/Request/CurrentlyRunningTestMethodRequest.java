package com.freecharge.dashboard.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CurrentlyRunningTestMethodRequest {
    private String serviceName;
    private String className;
}
