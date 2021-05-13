package com.onegravity.accountservice.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.adapter.GsonInstantAdapter
import java.time.Instant

val gson: Gson = GsonBuilder().apply {
    setPrettyPrinting()
    disableHtmlEscaping()
    registerTypeAdapter(Instant::class.java, GsonInstantAdapter)
    registerTypeAdapter(AccountStatus::class.java, AccountStatusAdapter)
    registerTypeAdapter(CustomerStatus::class.java, CustomerStatusAdapter)
    registerTypeAdapter(Language::class.java, LanguageAdapter)
}.create()
