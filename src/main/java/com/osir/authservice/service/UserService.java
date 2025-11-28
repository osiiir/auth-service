package com.osir.authservice.service;

import com.osir.takeoutpojo.dto.UserLoginDTO;
import com.osir.takeoutpojo.entity.User;

public interface UserService {

    /**
     * 微信用户登录
     *
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);

}
