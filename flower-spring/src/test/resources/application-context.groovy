import org.xdcrafts.flower.core.spring.example.Authenticator
import org.xdcrafts.flower.core.spring.example.LoggingMiddleware
import org.xdcrafts.flower.core.spring.example.Receiver
import org.xdcrafts.flower.core.spring.example.User
import org.xdcrafts.flower.spring.impl.DefaultActionFactory
import org.xdcrafts.flower.spring.impl.flows.BasicSyncFlowFactory
import org.xdcrafts.flower.spring.impl.switches.KeywordSwitchFactory

beans {

    importBeans('classpath:/email-feature.groovy')
    importBeans('classpath:/sms-feature.groovy')

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
