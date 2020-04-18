package com.freecharge.dashboard.Services;

import com.freecharge.dashboard.Services.Impl.ResultPercentageServicesImpl;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ResultPercentageServices implements ResultPercentageServicesImpl {

   // Logger logger = (Logger) LoggerFactory.getLogger(ResultPercentageServices.class);

    @Override
    public double passPercentageCount(int passCount, int failCount) {

        float percentage;
        float total=passCount+failCount;
        percentage = (float)((passCount / total) * 100);
       // logger.info("An INFO Message:::::"+percentage);
        return percentage;
    }
}
