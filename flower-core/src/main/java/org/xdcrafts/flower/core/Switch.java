/*
 * Copyright (c) 2016 Vadim Dubs https://github.com/xdcrafts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.xdcrafts.flower.core;

import java.util.List;
import java.util.Map;

/**
 * Action that makes decision about where should flow go.
 */
public interface Switch extends Action {

    /**
     * Returns extensions registered in this router.
     */
    List<Extension> extensions();

    /**
     * Makes decision about which action should proceed with processing of proposed context.
     */
    Action selectAction(Map context);

    @Override
    default Map apply(Map ctx) {
        return selectAction(ctx).apply(ctx);
    }
}
