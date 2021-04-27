package com.onegravity.accountservice.application

import com.onegravity.accountservice.util.getProperty
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlin.reflect.KType

fun Application.openAPIGenerator() {

    // install OpenAPI generator
    install(OpenAPIGen) {
        val serviceName = getProperty("ktor.serviceName", "Service")
        val serviceContact = getProperty("ktor.serviceContact", "Service")
        val serviceEmail = getProperty("ktor.serviceEmail", "Service")
        info {
            version = "1.0.0"
            title = "$serviceName API"
            description = "$serviceName API"
            contact {
                name = serviceContact
                email = serviceEmail
            }
        }

        val protocol = getProperty("ktor.deployment.protocol")
        val host = getProperty("ktor.deployment.host")
        val port = getProperty("ktor.deployment.port")
        server("$protocol://$host:$port") {
            description = serviceName
        }

        // rename DTOs from java type name to generator compatible form
        replaceModule(DefaultSchemaNamer, object : SchemaNamer {
            val regex = Regex("[A-Za-z0-9_.]+")

            override fun get(type: KType): String {
                return type.toString().replace(regex) { it.value.split(".").last() }.replace(Regex(">|<|, "), "_")
            }
        })
    }

    routing {
        get("/openapi.json") {
            call.respond(openAPIGen.api.serialize())
        }
        get("/") {
            call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
        }
    }

}