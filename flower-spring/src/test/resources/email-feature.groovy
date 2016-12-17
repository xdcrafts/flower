import org.xdcrafts.flower.core.spring.example.email.EmailAuthorizer
import org.xdcrafts.flower.core.spring.example.email.EmailRequestValidator
import org.xdcrafts.flower.core.spring.example.email.EmailSender
import org.xdcrafts.flower.spring.impl.DefaultActionFactory
import org.xdcrafts.flower.spring.impl.DefaultFeature
import org.xdcrafts.flower.spring.impl.extensions.KeywordExtensionFactory
import org.xdcrafts.flower.spring.impl.flows.BasicSyncFlowFactory

beans {

    // --------------------------------------- Simple spring beans ---------------------------------------

    emailRequestValidator EmailRequestValidator

    emailAuthorizer EmailAuthorizer

    emailSender EmailSender

    // --------------------------------------- Actions ---------------------------------------

    emailRequestValidatorAction DefaultActionFactory, "emailRequestValidator::validate"

    emailAuthorizerAction DefaultActionFactory, "emailAuthorizer::authorize"

    emailSenderAction DefaultActionFactory, "emailSender::send"

    // --------------------------------------- Flows ---------------------------------------

    emailFlow BasicSyncFlowFactory, [emailAuthorizerAction, emailRequestValidatorAction, emailSenderAction]

    // --------------------------------------- Extensions ---------------------------------------

    emailExtension KeywordExtensionFactory, "email", emailFlow

    // --------------------------------------- Features ---------------------------------------

    emailFeature DefaultFeature, true, [emailExtension: "switcher"]
}
