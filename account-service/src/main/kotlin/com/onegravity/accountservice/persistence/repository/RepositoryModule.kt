package com.onegravity.accountservice.persistence.repository

import org.koin.dsl.module as KoinModule

val repositoryModule = KoinModule {

    single<AccountRepository> { AccountRepositoryImpl() }

    single<CustomerRepository> { CustomerRepositoryImpl() }

}
