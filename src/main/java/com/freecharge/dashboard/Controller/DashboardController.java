package com.freecharge.dashboard.Controller;

import com.freecharge.dashboard.ExceptionHandler.NoMethodFound;
import com.freecharge.dashboard.ExceptionHandler.NoServicesFound;
import com.freecharge.dashboard.ExceptionHandler.NoTestClassesFound;
import com.freecharge.dashboard.ExceptionHandler.PassPercentageNotFound;
import com.freecharge.dashboard.Repository.DashBoardResultRespository;
import com.freecharge.dashboard.Request.CurrentlyRunningTestMethodRequest;
import com.freecharge.dashboard.Request.DashboardResultRequest;
import com.freecharge.dashboard.Response.*;
import com.freecharge.dashboard.Services.Impl.ResultPercentageServicesImpl;
import com.freecharge.dashboard.Services.ResultPercentageServices;
import com.mashape.unirest.http.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class DashboardController {

    public static final String PASS="Pass";
    public static final String FAIL="Fail";
    //ObjectMapper mapper = new ObjectMapper();

    @Autowired
    DashBoardResultRespository dashBoardResultRespository;

    @Autowired
    CurrentlyRunningServices currentlyRunningServices;

    @Autowired
    PassFailCountResponse passFailCountResponse;

    @Autowired
    CurrentlyRunningTestClassResponse currentlyRunningTestClassResponse;

    @Autowired
    CurrentlyRunningTestMethodResponse currentlyRunningTestMethodResponse;

    @Autowired
    ResultPercentageServicesImpl resultPercentageServicesImpl;

    @Autowired
    PassPercentageResponse passPercentageResponse;

    @GetMapping(value ="/totalServicesCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningServices> totalServicesCurrentlyRunning(){
       final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listServices =  dashBoardResultRespository.currentlyRunningServices(date);
          currentlyRunningServices.setListServices(listServices);

        if(currentlyRunningServices.getListServices().isEmpty()){
            throw new NoServicesFound();
        }
        return new ResponseEntity<CurrentlyRunningServices>(currentlyRunningServices,HttpStatus.OK);
    }

    @GetMapping(value ="/totalServicesRunParticularDay")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningServices> totalServicesRunParticularDay(@RequestParam(name="startDate") String startDate){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listServices =  dashBoardResultRespository.currentlyRunningServices(date);
        currentlyRunningServices.setListServices(listServices);

        if(currentlyRunningServices.getListServices().isEmpty()){
            throw new NoServicesFound();
        }
        return new ResponseEntity<CurrentlyRunningServices>(currentlyRunningServices,HttpStatus.OK);
    }

    @GetMapping(value ="/servicesPassCount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PassFailCountResponse> servicesPassCount(@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final int passCount =  dashBoardResultRespository.passfailCount(serviceName,PASS,date);
        passFailCountResponse.setResult(passCount);
        return new ResponseEntity<PassFailCountResponse>(passFailCountResponse,HttpStatus.OK);
    }

    @GetMapping(value ="/servicesFailCount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PassFailCountResponse> servicesFailCount(@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final int failCount =  dashBoardResultRespository.passfailCount(serviceName,FAIL,date);
        passFailCountResponse.setResult(failCount);
        return new ResponseEntity<PassFailCountResponse>(passFailCountResponse,HttpStatus.OK);
    }

    @GetMapping(value ="/totalServicesClassCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningTestClassResponse> totalServicesMethodCurrentlyRunning(@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listTestClasses =  dashBoardResultRespository.currentlyRunningServicesClass(serviceName,date);
        currentlyRunningTestClassResponse.setTestClasses(listTestClasses);

        if(currentlyRunningTestClassResponse.getTestClasses().isEmpty()){
            throw new NoTestClassesFound();
        }
        return new ResponseEntity<CurrentlyRunningTestClassResponse>(currentlyRunningTestClassResponse,HttpStatus.OK);
    }

    @GetMapping(value ="/totalServicesMethodCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningTestMethodResponse> totalServicesMethodCurrentlyRunning(@RequestBody CurrentlyRunningTestMethodRequest currentlyRunningTestMethodRequest){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listTestClasses =  dashBoardResultRespository.currentlyRunningServicesMethod(currentlyRunningTestMethodRequest.getServiceName(),currentlyRunningTestMethodRequest.getClassName(),date);
        currentlyRunningTestMethodResponse.setTestMethods(listTestClasses);

        if(currentlyRunningTestMethodResponse.getTestMethods().isEmpty()){
            throw new NoTestClassesFound();
        }
        return new ResponseEntity<CurrentlyRunningTestMethodResponse>(currentlyRunningTestMethodResponse,HttpStatus.OK);
    }

    @GetMapping(value ="/servicesResultPercentageCount")
    @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<PassPercentageResponse> servicesResultPercentageCount(@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final int passCount =  dashBoardResultRespository.passfailCount(serviceName,PASS,date);
        final int failCount =  dashBoardResultRespository.passfailCount(serviceName,FAIL,date);
        final double passPercentage =  resultPercentageServicesImpl.passPercentageCount(passCount,failCount);

        if(passCount==0 && failCount==0)
            throw new PassPercentageNotFound();
        else if(passCount==0 && failCount>0) {
            passPercentageResponse.setPassPercentage(0);
            passPercentageResponse.setFailPercentage(0);
        }
        else {
            passPercentageResponse.setPassPercentage(passPercentage);
            passPercentageResponse.setFailPercentage(100 - passPercentage);
        }
        return new ResponseEntity<PassPercentageResponse>(passPercentageResponse,HttpStatus.OK);
    }
}
