package com.onegravity.accountservice.controller

import com.onegravity.accountservice.route.response.ServiceStatus

interface HealthController {

    suspend fun getServiceStatus(): ServiceStatus

}
