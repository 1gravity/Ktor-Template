package com.onegravity.route

import com.papsign.ktor.openapigen.annotations.Response

@Response("Status of the service.", 200)
data class ServiceStatus(val serviceName: String, val uptime: String)
