package com.osir.authservice.feign;

import com.osir.takeoutpojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", path = "/user/user/user")
public interface UserServiceFeignClient {

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @GetMapping("/getByOpenid")
    User getByOpenid(@RequestParam("openid") String openid);

    /**
     * 插入用户
     * @param user
     */
    @PostMapping("/insert")
    void insert(@RequestBody User user);

}
