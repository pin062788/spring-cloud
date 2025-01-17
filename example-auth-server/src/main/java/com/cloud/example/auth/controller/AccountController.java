/*
 *  Copyright 2019 https://github.com/romeoblog/spring-cloud.git Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.cloud.example.auth.controller;

import com.cloud.example.common.enums.ResultCode;
import com.cloud.example.common.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 账号中心
 *
 * @author Joe-Benji
 * @date 2019-04-08
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AccountController {

//    @Autowired
//    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/getUser")
    public Principal user(Principal member) {
        //获取当前用户信息
        return member;
    }

    @GetMapping("/user")
    public Authentication getUser(Authentication authentication) {
        log.info("resource: user {}", authentication);
        return authentication;
    }


//    @DeleteMapping(value = "/logout")
//    public ResultMsg revokeToken(String accessToken) {
//        if (consumerTokenServices.revokeToken(accessToken)) {
//            return ResultMsg.create().status(ResultCode.OK);
//        } else {
//            return ResultMsg.error(ResultCode.EXCEPTION);
//        }
//    }
}
