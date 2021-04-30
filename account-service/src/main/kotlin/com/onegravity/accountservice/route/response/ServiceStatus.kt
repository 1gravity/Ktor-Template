package com.onegravity.accountservice.route.response

import com.papsign.ktor.openapigen.annotations.Response
import kotlinx.serialization.Serializable

@Serializable
@Response("Status of the service.", 200)
data class ServiceStatus(val serviceName: String, val uptime: String)
