package com.onegravity.accountservice.controller

import com.onegravity.accountservice.persistence.repository.AccountRepository
import com.onegravity.accountservice.persistence.repository.CustomerRepository
import com.onegravity.accountservice.route.request.RequestAccount
import com.onegravity.accountservice.route.request.RequestCustomer
import com.onegravity.accountservice.route.response.ServiceStatus
import com.onegravity.accountservice.route.response.toResponse
import com.onegravity.accountservice.util.Config
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

class MasterController : AccountController, CustomerController, EmailController, DeviceController, HealthController,
    KoinComponent {

    private val config: Config by inject()
    private val accountRepo: AccountRepository by inject()
    private val customerRepo: CustomerRepository by inject()

    /* Account operations */

    override suspend fun getAccounts() =
        accountRepo.getAccounts().map { it.toResponse() }

    override suspend fun getAccount(accountUUID: String) =
        accountRepo.getAccount(accountUUID).toResponse()

    override suspend fun createAccount(account: RequestAccount) =
        accountRepo.createAccount(account).toResponse()

    override suspend fun updateAccount(accountUUID: String, account: RequestAccount) =
        accountRepo.updateAccount(accountUUID, account).toResponse()

    override suspend fun deleteAccount(accountUUID: String) =
        accountRepo.deleteAccount(accountUUID).toResponse()

    /* Customer operations */

    override suspend fun getCustomers() =
        customerRepo.getCustomers().map { it.toResponse() }

    override suspend fun getCustomer(customerUUID: String) =
        customerRepo.getCustomer(customerUUID).toResponse()

    override suspend fun createCustomer(accountUUID: String, customer: RequestCustomer) =
        customerRepo.createCustomer(accountUUID, customer).toResponse()

    override suspend fun updateCustomer(customerUUID: String, customer: RequestCustomer) =
        customerRepo.updateCustomer(customerUUID, customer).toResponse()

    override suspend fun deleteCustomer(customerUUID: String) =
        customerRepo.deleteCustomer(customerUUID).toResponse()

    /* Health operations */

    private val serviceStartMillis = System.currentTimeMillis()

    private val serviceName = config.getProperty("ktor.serviceName", "my-service")

    override suspend fun getServiceStatus(): ServiceStatus {
        val uptimeS = System.currentTimeMillis().minus(serviceStartMillis).toDouble().div(1000).roundToInt()
        return ServiceStatus(serviceName, "${uptimeS}s")
    }

}
