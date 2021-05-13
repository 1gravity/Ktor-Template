package com.onegravity.module

import com.onegravity.util.getKoinInstance
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import com.papsign.ktor.openapigen.schema.namer.DefaultSchemaNamer
import com.papsign.ktor.openapigen.schema.namer.SchemaNamer
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlin.reflect.KType
import com.onegravity.config.Config

fun Application.openAPIGenerator() {

    val config = getKoinInstance<Config>()

    // install OpenAPI generator
    install(OpenAPIGen) {
        val serviceName = config.getProperty("ktor.serviceName", "Service")
        val serviceContact = config.getProperty("ktor.serviceContact", "Service")
        val serviceEmail = config.getProperty("ktor.serviceEmail", "Service")
        info {
            version = "1.0.0"
            title = "$serviceName API"
            description = "$serviceName API"
            contact {
                name = serviceContact
                email = serviceEmail
            }
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