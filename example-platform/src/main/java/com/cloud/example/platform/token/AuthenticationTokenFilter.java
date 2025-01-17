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
package com.cloud.example.platform.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloud.example.common.constant.Constants;
import com.cloud.example.common.enums.ResultCode;
import com.cloud.example.common.model.ResultMsg;
import com.cloud.example.common.utils.JacksonUtils;
import com.cloud.example.platform.common.props.PermitUrlProperties;
import com.cloud.example.common.constant.HeadConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Token interceptor filter
 *
 * @author Benji
 * @date 2019-04-15
 */
@Component
@Slf4j
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired(required = false)
    private PermitUrlProperties permitUrlProperties;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${checkToken:true}")
    private Boolean checkToken;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        if (!checkToken) {
            if (log.isDebugEnabled()) {
                log.debug("Current application Check Token is Disable.");
            }
            chain.doFilter(request, response);
            return;
        }

        if (!handlerNextChain(request.getRequestURI())) {
            String authToken = extractToken(request);

            log.info("Get Token=[{}]", authToken);

            if (StringUtils.isNotEmpty(authToken)) {
                String cacheJwtId = redisTemplate.opsForValue().get(Constants.JWT_ID_USERNAME + "admin");
                log.info("Check JWT Id return Result[{}]", cacheJwtId);

                DecodedJWT jwt = JWT.decode(authToken);

                if (StringUtils.isEmpty(cacheJwtId) || jwt == null) {
                    ResultMsg result = ResultMsg.create().status(ResultCode.ERROR_TOKEN);
                    try {
                        log.error("Check Token is failed，Token is Invalid. message={}", JacksonUtils.toJson(result));
                        handlerResponse(response, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String jwtId = jwt.getId();
                    if (!Objects.equals(jwtId,cacheJwtId)) {
                        ResultMsg result = ResultMsg.create().status(ResultCode.DUPLICATE_MACHINE);
                        try {
                            log.error("Check Token is failed，token has duplicate machine. message={}", JacksonUtils.toJson(result));
                            handlerResponse(response, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        chain.doFilter(request, response);
                    }
                }
            } else {
                ResultMsg result = ResultMsg.create().status(ResultCode.NO_PREMISSION);
                try {
                    log.error("Check Token is failed，Token is Not Exist. message={}", JacksonUtils.toJson(result));
                    handlerResponse(response, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            chain.doFilter(request, response);
        }

    }

    /**
     * Get Token information by header
     *
     * @param request
     * @return
     */
    protected String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HeadConstant.AUTHORIZATION_HEAD);
        String authToken = null;
        if (authHeader != null) {
            authToken = authHeader.substring(HeadConstant.BEARER_HEAD.length()).trim();
        }
        return authToken;
    }

    /**
     * Handler Next chain
     *
     * @param requestURI
     * @return
     */
    private Boolean handlerNextChain(String requestURI) {
        boolean nextChain = false;
        List<String> ignoreUrls = Arrays.asList(permitUrlProperties.getIgnored());

        for (String path : ignoreUrls) {
            if (pathMatcher.match(path, requestURI)) {
                nextChain = true;
                break;
            }
        }
        if (log.isDebugEnabled()) {
            log.info("Ignore resource=[{}]，Request path=[{}], Request path is ignore. result=[{}]", ignoreUrls.toString(), requestURI, nextChain);
        }

        return nextChain;
    }

    /**
     * Handler Request return Response with json
     *
     * @param response
     * @param responseObject
     */
    protected void handlerResponse(HttpServletResponse response, Object responseObject) {
        String json = JacksonUtils.toJson(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(json);
        } catch (IOException ex) {
            log.error("Handler response error, ErrorMsg={}", ex.getMessage(), ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}