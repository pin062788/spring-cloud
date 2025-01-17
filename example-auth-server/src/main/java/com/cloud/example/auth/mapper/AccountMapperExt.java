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
package com.cloud.example.auth.mapper;

import com.cloud.example.auth.entity.Account;

/**
 * 〈用户Dao〉
 *
 * @author Joe-Benji
 * @date 2019-04-08
 * @since 1.0.0
 */
public interface AccountMapperExt {

    /**
     * 根据会员名查找会员
     *
     * @param memberName 会员名
     * @return 会员
     */
    Account findByAccountName(String memberName);
}
