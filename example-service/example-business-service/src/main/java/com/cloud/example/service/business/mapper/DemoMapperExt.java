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
package com.cloud.example.service.business.mapper;

import com.cloud.example.service.business.entity.DemoVO;

/**
 * demo mapper
 *
 * @author Benji
 * @date 2019-04-28
 */
public interface DemoMapperExt {

    /**
     * demo
     *
     * @param id
     * @return
     */
    DemoVO getInformation(Integer id);

}
