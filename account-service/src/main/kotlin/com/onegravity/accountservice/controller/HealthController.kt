package com.onegravity.accountservice.controller

import com.onegravity.accountservice.route.model.ServiceStatus

interface HealthController {

    suspend fun getServiceStatus(): ServiceStatus

}
