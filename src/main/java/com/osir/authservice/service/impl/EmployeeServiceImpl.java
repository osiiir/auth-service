package com.osir.authservice.service.impl;


import com.osir.authservice.constant.StatusConstant;
import com.osir.authservice.exception.AccountLockedException;
import com.osir.authservice.exception.AccountNotFoundException;
import com.osir.authservice.exception.PasswordErrorException;
import com.osir.authservice.feign.AdminServiceFeignClient;
import com.osir.authservice.service.EmployeeService;
import com.osir.takeoutpojo.constant.ErrorMessageConstant;
import com.osir.takeoutpojo.dto.EmployeeLoginDTO;
import com.osir.takeoutpojo.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final AdminServiceFeignClient adminServiceFeignClient;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = adminServiceFeignClient.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(ErrorMessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传来的明文密码，进行md5加密后再对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(ErrorMessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(ErrorMessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


}
