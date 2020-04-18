package com.freecharge.dashboard.Repository;

import com.freecharge.dashboard.Model.DashBoardResult;
import com.freecharge.dashboard.Response.PassFailCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DashBoardResultRespository extends JpaRepository<DashBoardResult,String> {

    @Query(value="select distinct(b.service_name) from backend_automation b where b.start_time like %:startDate% ", nativeQuery = true)
    public List<String> currentlyRunningServices(String startDate);

    @Query(value="select count(b.status) from backend_automation b where b.service_name=:serviceName and b.start_time like %:startDate% and b.status=:status", nativeQuery = true)
    public int passfailCount(String serviceName,String status,String startDate);

    @Query(value="select distinct(b.test_class_name) from backend_automation b where b.service_name =:serviceName and b.start_time like %:startDate% ", nativeQuery = true)
    public List<String> currentlyRunningServicesClass(String serviceName,String startDate);

    @Query(value="select b.test_method_name from backend_automation b where b.service_name =:serviceName and b.test_class_name=:className and b.start_time like %:startDate% ", nativeQuery = true)
    public List<String> currentlyRunningServicesMethod(String serviceName,String className,String startDate);

}
