package com.onegravity.module

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.headerModule() {

    // some default headers
    install(DefaultHeaders) {
        header(HttpHeaders.AccessControlAllowOrigin, "*")
        header(HttpHeaders.Server, "N/A for security reasons")
        header(HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
        header(HttpHeaders.Pragma, "no-cache")
        header(HttpHeaders.Expires, "0")
    }

}