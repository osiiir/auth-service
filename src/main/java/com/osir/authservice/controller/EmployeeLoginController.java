package com.osir.authservice.controller;


import com.osir.authservice.constant.JwtClaimsConstant;
import com.osir.authservice.properties.JwtProperties;
import com.osir.authservice.service.EmployeeService;
import com.osir.authservice.utils.JwtUtil;
import com.osir.takeoutpojo.dto.EmployeeLoginDTO;
import com.osir.takeoutpojo.entity.Employee;
import com.osir.takeoutpojo.result.Result;
import com.osir.takeoutpojo.vo.EmployeeLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/admin/employee")
public class EmployeeLoginController {

    private final EmployeeService employeeService;
    private final JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        log.info("退出账号");
        return Result.success();
    }

}
