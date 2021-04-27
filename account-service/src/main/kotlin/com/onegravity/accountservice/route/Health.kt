package com.onegravity.accountservice.route

import com.onegravity.accountservice.controller.HealthController
import com.onegravity.accountservice.route.response.ServiceStatus
import com.onegravity.accountservice.util.getKoinInstance
import com.onegravity.accountservice.util.getProperty
import com.papsign.ktor.openapigen.route.*
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import io.ktor.http.*

fun NormalOpenAPIRoute.healthRouting() {
    val controller = getKoinInstance<HealthController>()

    val serviceName = getProperty("ktor.serviceName", "my-service")

    tag(Tags.Misc) {
        route("/status")
            .get<Unit, ServiceStatus>(
                info(
                    summary = "Get service status.",
                    description = "Health check: returns the service name and uptime."
                ),
                status(HttpStatusCode.OK),
                example = ServiceStatus(serviceName, "1000s")
            ) {
                val status = controller.getServiceStatus()
                respond(status)
            }
    }

}
