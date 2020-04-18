package com.freecharge.dashboard.Controller;

import com.freecharge.dashboard.ExceptionHandler.NoMethodFound;
import com.freecharge.dashboard.ExceptionHandler.NoServicesFound;
import com.freecharge.dashboard.ExceptionHandler.NoTestClassesFound;
import com.freecharge.dashboard.ExceptionHandler.PassPercentageNotFound;
import com.freecharge.dashboard.Model.DashBoardResult;
import com.freecharge.dashboard.Repository.DashBoardResultRespository;
import com.freecharge.dashboard.Request.CurrentlyRunningTestMethodRequest;
import com.freecharge.dashboard.Request.DashboardResultRequest;
import com.freecharge.dashboard.Request.KeyValue;
import com.freecharge.dashboard.Response.*;
import com.freecharge.dashboard.Services.Impl.ResultPercentageServicesImpl;
import com.freecharge.dashboard.Services.ResultPercentageServices;
import com.mashape.unirest.http.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    public static final String PASS="Pass";
    public static final String FAIL="Fail";
    public static final String SKIP="Skip";
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

    @Autowired
    DashBoardResult dashBoardResult;

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        model.put("message", "HowToDoInJava Reader !!");
        return "index";
    }

    @RequestMapping("/next")
    public String next(Map<String, Object> model) {
        model.put("message", "You are in new page !!");
        return "next";
    }

   /* @GetMapping("/show-users")
    public String showAllUsers(HttpServletRequest request) {
        request.setAttribute("users", userService.showAllUsers());
        request.setAttribute("mode", "ALL_USERS");
        return "welcomepage";
    }*/
//{"listServices":["orchestrator","checkout","kycService"]}


    @GetMapping(value ="/totalServicesCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public String totalServicesCurrentlyRunning(HttpServletRequest request){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listServices =  dashBoardResultRespository.currentlyRunningServices(date);
        currentlyRunningServices.setListServices(listServices);

        if(currentlyRunningServices.getListServices().isEmpty()){
            throw new NoServicesFound();
        }
        request.setAttribute("services", currentlyRunningServices.getListServices());
        request.setAttribute("mode", "ALL_SERVICES");

        return "welcomepage";
    }


    @GetMapping(value ="/totalServicesClassCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public String totalServicesMethodCurrentlyRunning(HttpServletRequest request,@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listTestClasses =  dashBoardResultRespository.currentlyRunningServicesClass(serviceName,date);

        currentlyRunningTestClassResponse.setTestClasses(listTestClasses);
        currentlyRunningTestClassResponse.setServiceName(serviceName);
        if(currentlyRunningTestClassResponse.getTestClasses().isEmpty()){
            throw new NoTestClassesFound();
        }
        request.setAttribute("testclasses", currentlyRunningTestClassResponse);
        request.setAttribute("mode", "ALL_TEST_CLASSES");
        return "testclasses";
    }

    @GetMapping(value ="/totalServicesMethodCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public String totalServicesMethodCurrentlyRunning(HttpServletRequest request, @RequestParam(name="serviceName") String serviceName, @RequestParam(name="className") String className){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> testMethodResponse =  dashBoardResultRespository.currentlyRunningServicesMethod(serviceName,className,date);
        final List<String> testMethodstatus = dashBoardResultRespository.currentlyRunningServicesMethodStatus(serviceName,className,date);
        if(testMethodResponse.isEmpty()){
            throw new NoTestClassesFound();
        }
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i <testMethodResponse.size() ; i++) {
            map.put(testMethodResponse.get(i),testMethodstatus.get(i));
        }
        request.setAttribute("serviceName",serviceName);
        request.setAttribute("testMethodAttribute", map);
        request.setAttribute("mode", "ALL_TEST_CLASSES_METHODS");

         return "testMethods";
    }


    @GetMapping(value ="/servicesResultPercentageCount")
    @ResponseStatus(HttpStatus.OK)
    public String servicesResultPercentageCount(HttpServletRequest request,@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final int passCount =  dashBoardResultRespository.passfailCount(serviceName,PASS,date);
        final int failCount =  dashBoardResultRespository.passfailCount(serviceName,FAIL,date);
        final int skipCount =  dashBoardResultRespository.passfailCount(serviceName,SKIP,date);
       // final double passPercentage =  resultPercentageServicesImpl.passPercentageCount(passCount,failCount);

        if(passCount==0 && failCount==0)
            throw new PassPercentageNotFound();
       else {
            passPercentageResponse.setPassPercentage(passCount);
            passPercentageResponse.setFailPercentage(failCount);
            passPercentageResponse.setSkipPercentage(skipCount);
        }
        final List<KeyValue> pieDataList;

            pieDataList = new ArrayList<KeyValue>();

      /*  pieDataList.add(new KeyValue("Pass", "50"));
        pieDataList.add(new KeyValue("Fail", "10"));
        pieDataList.add(new KeyValue("Skip", "29"));*/

            pieDataList.add(new KeyValue("Pass", passPercentageResponse.getPassPercentage()+""));
            pieDataList.add(new KeyValue("Fail", passPercentageResponse.getFailPercentage()+""));
            pieDataList.add(new KeyValue("Skip", passPercentageResponse.getSkipPercentage()+""));
            request.setAttribute("pieDataList", pieDataList);

        return "piechart";
    }


   /* @GetMapping(value ="/totalServicesMethodCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningTestMethodResponse> totalServicesMethodCurrentlyRunning(@RequestBody CurrentlyRunningTestMethodRequest currentlyRunningTestMethodRequest){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listTestClasses =  dashBoardResultRespository.currentlyRunningServicesMethod(currentlyRunningTestMethodRequest.getServiceName(),currentlyRunningTestMethodRequest.getClassName(),date);
        currentlyRunningTestMethodResponse.setTestMethods(listTestClasses);

        if(currentlyRunningTestMethodResponse.getTestMethods().isEmpty()){
            throw new NoTestClassesFound();
        }
        return new ResponseEntity<CurrentlyRunningTestMethodResponse>(currentlyRunningTestMethodResponse,HttpStatus.OK);
    }*/


    /*@GetMapping(value ="/totalServicesClassCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningTestClassResponse> totalServicesMethodCurrentlyRunning(@RequestParam(name="serviceName") String serviceName){
        final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listTestClasses =  dashBoardResultRespository.currentlyRunningServicesClass(serviceName,date);
        currentlyRunningTestClassResponse.setTestClasses(listTestClasses);

        if(currentlyRunningTestClassResponse.getTestClasses().isEmpty()){
            throw new NoTestClassesFound();
        }
        return new ResponseEntity<CurrentlyRunningTestClassResponse>(currentlyRunningTestClassResponse,HttpStatus.OK);
    }*/





    /*@GetMapping(value ="/totalServicesCurrentlyRunning")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CurrentlyRunningServices> totalServicesCurrentlyRunning(HttpServletRequest request){
       final String date = String.valueOf(java.time.LocalDate.now());
        final List<String> listServices =  dashBoardResultRespository.currentlyRunningServices(date);
          currentlyRunningServices.setListServices(listServices);

        if(currentlyRunningServices.getListServices().isEmpty()){
            throw new NoServicesFound();
        }
        return new ResponseEntity<CurrentlyRunningServices>(currentlyRunningServices,HttpStatus.OK);
    }*/

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





    /*@GetMapping(value ="/servicesResultPercentageCount")
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
    }*/
}
