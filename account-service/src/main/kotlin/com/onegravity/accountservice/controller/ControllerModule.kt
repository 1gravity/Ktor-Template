package com.onegravity.accountservice.controller

import org.koin.dsl.module as KoinModule

val controllerModule = KoinModule {
    single<AccountController> { MasterController }
    single<CustomerController> { MasterController }
    single<DeviceController> { MasterController }
    single<EmailController> { MasterController }
    single<HealthController> { MasterController }
}
