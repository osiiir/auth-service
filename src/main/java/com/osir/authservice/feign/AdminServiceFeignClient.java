package com.osir.authservice.feign;

import com.osir.takeoutpojo.entity.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "admin-service", path = "/admin/admin/employee")
public interface AdminServiceFeignClient {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @GetMapping("/getByUsername")
    Employee getByUsername(@RequestParam("username") String username);

}
