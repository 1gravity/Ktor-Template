rootProject.name = "ktor-template"

include(
    "common",
    "account-service"
    // more services go here
)

// referencing a local project goes like this:
// project(":OpenAPI-Generator").projectDir = File("../Ktor-OpenAPI-Generator")
include("common")
