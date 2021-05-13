package com.onegravity.route

import com.onegravity.util.getKoinInstance
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.onegravity.config.Config
import com.papsign.ktor.openapigen.APITag
import kotlin.math.roundToInt

fun NormalOpenAPIRoute.healthRouting() {
    val config = getKoinInstance<Config>()

    val serviceName = config.getProperty("ktor.serviceName", "my-service")

    val serviceStartMillis = System.currentTimeMillis()

    fun getServiceStatus(): ServiceStatus {
        val uptimeS = System.currentTimeMillis().minus(serviceStartMillis).toDouble().div(1000).roundToInt()
        return ServiceStatus(serviceName, "${uptimeS}s")
    }

    tag(Tags.Misc) {
        route("/status")
            .get<Unit, ServiceStatus>(
                info(
                    summary = "Get service status.",
                    description = "Health check: returns the service name and uptime."
                ),
                example = ServiceStatus(serviceName, "1000s")
            ) {
                val status = getServiceStatus()
                respond(status)
            }
    }

}

private enum class Tags(override val description: String) : APITag {
    Misc("Miscellaneous API."),
}
