package com.onegravity.accountservice.controller

import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.accountservice.route.model.ServiceStatus
import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer
import com.onegravity.accountservice.util.Config
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

object MasterController : AccountController, CustomerController, EmailController, DeviceController, HealthController,
    KoinComponent {

    private val config: Config by inject()

    private val daoProvider: DaoProvider by inject()

    /* Account operations */

    private val accounts = daoProvider.accountDao()

    override suspend fun getAccounts() = accounts.getAll()

    override suspend fun getAccount(accountUUID: String) = accounts.get(accountUUID)

    override suspend fun createAccount(account: CreateAccount) = accounts.insert(account)

    override suspend fun updateAccount(account: UpdateAccount) = accounts.update(account)

    override suspend fun deleteAccount(accountUUID: String) = accounts.delete(accountUUID)

    /* Customer operations */

    private val customers = daoProvider.customerDao()

    override suspend fun getCustomers() = customers.getAll()

    override suspend fun getCustomer(customerUUID: String) = customers.get(customerUUID)

    override suspend fun createCustomer(customer: CreateCustomer) = customers.insert(customer)

    override suspend fun updateCustomer(customer: UpdateCustomer) = customers.update(customer)

    override suspend fun deleteCustomer(customerUUID: String) = customers.delete(customerUUID)

    /* Health operations */

    private val serviceStartMillis = System.currentTimeMillis()

    private val serviceName = config.getProperty("ktor.serviceName", "my-service")

    override suspend fun getServiceStatus(): ServiceStatus {
        val uptimeS = System.currentTimeMillis().minus(serviceStartMillis).toDouble().div(1000).roundToInt()
        return ServiceStatus(serviceName, "${uptimeS}s")
    }

}
