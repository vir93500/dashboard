package com.freecharge.dashboard.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResultRequest {
    private String serviceName;
    private String suiteRunningDate;

}
