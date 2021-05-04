package com.onegravity.accountservice.route

import com.onegravity.accountservice.controller.HealthController
import com.onegravity.accountservice.route.response.ServiceStatus
import com.onegravity.accountservice.util.Config
import com.onegravity.accountservice.util.getKoinInstance
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

fun NormalOpenAPIRoute.healthRouting() {
    val config = getKoinInstance<Config>()
    val controller = getKoinInstance<HealthController>()

    val serviceName = config.getProperty("ktor.serviceName", "my-service")

    tag(Tags.Misc) {
        route("/status")
            .get<Unit, ServiceStatus>(
                info(
                    summary = "Get service status.",
                    description = "Health check: returns the service name and uptime."
                ),
                example = ServiceStatus(serviceName, "1000s")
            ) {
                val status = controller.getServiceStatus()
                respond(status)
            }
    }

}
