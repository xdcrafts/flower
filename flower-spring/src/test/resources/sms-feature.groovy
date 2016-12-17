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
