apply {
    from("$rootDir/android-library-build.gradle")
}

dependencies {
    "implementation"(project(Modules.heroInteractors))
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.components))

    "implementation"(Coil.coil)

    "androidTestImplementation"(project(Modules.heroDataSourceTest))
    "androidTestImplementation"(ComposeTest.uiTestJunit4)
    "debugImplementation"(ComposeTest.uiTestManifest)
    "androidTestImplementation"(Junit.junit4)
    // need this dependency to solve activity exported error on Android 12 and higher
    "androidTestImplementation"("androidx.test.ext:junit:1.1.3")
}