package com.onegravity.accountservice.route.response

import com.papsign.ktor.openapigen.annotations.Response
import kotlinx.serialization.Serializable

@Serializable
@Response("Service Status Response.")
data class ServiceStatus(val serviceName: String, val uptime: String)
