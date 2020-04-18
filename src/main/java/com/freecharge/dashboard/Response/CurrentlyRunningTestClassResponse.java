package com.freecharge.dashboard.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CurrentlyRunningTestClassResponse {
    private List<String> testClasses;
    private String serviceName;
}
