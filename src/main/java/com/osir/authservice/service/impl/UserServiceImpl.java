package com.osir.authservice.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.osir.authservice.exception.LoginFailedException;
import com.osir.authservice.feign.UserServiceFeignClient;
import com.osir.authservice.properties.WeChatProperties;
import com.osir.authservice.service.UserService;
import com.osir.authservice.utils.HttpClientUtil;
import com.osir.takeoutpojo.constant.ErrorMessageConstant;
import com.osir.takeoutpojo.dto.UserLoginDTO;
import com.osir.takeoutpojo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    private final WeChatProperties weChatProperties;
    private final UserServiceFeignClient userServiceFeignClient;

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信登录接口服务，获得当前微信用户的openid
        String openid = getOpenid(userLoginDTO);

        // 判断openid是否为空，如果为空则抛出业务异常
        if(openid==null){
            throw new LoginFailedException(ErrorMessageConstant.LOGIN_FAILED);
        }
        // 判断当前用户是否为新用户
        User user = userServiceFeignClient.getByOpenid(openid);
        // 如果是则加入到数据库中
        if(user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userServiceFeignClient.insert(user);
        }
        // 返回user对象
        return user;
    }

    private String getOpenid(UserLoginDTO userLoginDTO){
        // 调用该接口需要四个参数:
        Map<String, String> param = new HashMap<>();
        param.put("appid",weChatProperties.getAppid());
        param.put("secret",weChatProperties.getSecret());
        param.put("js_code",userLoginDTO.getCode());
        param.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, param);
        // 把返回的json字符串转为json对象
        JSONObject jsonObject = JSONUtil.parseObj(json);
        return jsonObject.getStr("openid");
    }

}
