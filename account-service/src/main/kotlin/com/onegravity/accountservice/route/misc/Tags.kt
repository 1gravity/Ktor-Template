package com.onegravity.accountservice.route.misc

import com.papsign.ktor.openapigen.APITag

enum class Tags(override val description: String) : APITag {
    Account("Account entity API."),
    Customer("Customer entity API."),
    Misc("Unclassified API."),
}