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

package groovy

import org.xdcrafts.flower.core.spring.example.Authenticator
import org.xdcrafts.flower.core.spring.example.LoggingMiddleware
import org.xdcrafts.flower.core.spring.example.Receiver
import org.xdcrafts.flower.core.spring.example.User
import org.xdcrafts.flower.spring.impl.DefaultActionFactory
import org.xdcrafts.flower.spring.impl.flows.BasicSyncFlowFactory
import org.xdcrafts.flower.spring.impl.switches.KeywordSwitchFactory

beans {

    importBeans('classpath:/groovy/email-feature.groovy')
    importBeans('classpath:/groovy/sms-feature.groovy')

    // --------------------------------------- Simple spring beans ---------------------------------------

    authenticator Authenticator, ["secret-admin": new User("admin", "admin@example.com", "adminpwd", ["sms", "email"]),
                                  "sms-only-user": new User("sms-only-user", "sms-only-user@example.com", "smsonlyuserpwd", ["sms"]),
                                  "email-only-user": new User("email-only-user", "email-only-user@example.com", "emailonlyuserpwd", ["email"])]

    receiver Receiver

    // --------------------------------------- Middleware ---------------------------------------

    loggingMiddleware LoggingMiddleware

    // --------------------------------------- Actions ---------------------------------------

    authenticatorAction DefaultActionFactory, "authenticator::authenticate", [loggingMiddleware]

    switcher KeywordSwitchFactory, "request.type"

    receiverAction DefaultActionFactory, "receiver::receive"

    // --------------------------------------- Flows ---------------------------------------

    mainFlow BasicSyncFlowFactory, [authenticatorAction, switcher, receiverAction], [loggingMiddleware]
}
