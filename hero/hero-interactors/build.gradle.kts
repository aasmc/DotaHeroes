apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDataSource))
    "implementation"(project(Modules.heroDomain))
    "testImplementation"(project(Modules.heroDataSourceTest))

    "implementation"(Kotlinx.coroutinesCore) // need for flows

    "testImplementation"(Junit.junit4) // need for flows
    "testImplementation"(Ktor.clientSerialization) // need for flows
    "testImplementation"(Ktor.ktorClientMock) // need for flows
}