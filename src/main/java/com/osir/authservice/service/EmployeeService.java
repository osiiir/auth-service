package com.osir.authservice.service;

import com.osir.takeoutpojo.dto.EmployeeLoginDTO;
import com.osir.takeoutpojo.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
