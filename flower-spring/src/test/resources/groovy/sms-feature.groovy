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

import org.xdcrafts.flower.core.spring.example.sms.SmsAuthorizer
import org.xdcrafts.flower.core.spring.example.sms.SmsRequestValidator
import org.xdcrafts.flower.core.spring.example.sms.SmsSender
import org.xdcrafts.flower.spring.impl.DefaultActionFactory
import org.xdcrafts.flower.spring.impl.DefaultFeature
import org.xdcrafts.flower.spring.impl.extensions.KeywordExtensionFactory
import org.xdcrafts.flower.spring.impl.flows.BasicSyncFlowFactory

beans {

    // --------------------------------------- Simple spring beans ---------------------------------------

    smsRequestValidator SmsRequestValidator

    smsAuthorizer SmsAuthorizer

    smsSender SmsSender

    // --------------------------------------- Actions ---------------------------------------

    smsReqiestValidatorAction DefaultActionFactory, "smsRequestValidator::validate"

    smsAuthorizerAction DefaultActionFactory, "smsAuthorizer::authorize"

    smsSenderAction DefaultActionFactory, "smsSender::send"

    // --------------------------------------- Flows ---------------------------------------

    smsFlow BasicSyncFlowFactory, [smsAuthorizerAction, smsReqiestValidatorAction, smsSenderAction]

    // --------------------------------------- Extensions ---------------------------------------

    smsExtension KeywordExtensionFactory, "sms", smsFlow

    // --------------------------------------- Features ---------------------------------------

    smsFeature DefaultFeature, true, [smsExtension: "switcher"]
}
