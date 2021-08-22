import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    compose()
}

group = Config.group
version = Config.version

kotlin {
    jvm()

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(project(Modules.core))
                implementation(project(Modules.core_repo))
                implementation(project(Modules.core_nav))
                implementation(project(Modules.ui_data))
                implementation(project(Modules.repo_data))
                implementation(project(Modules.core_ui))
                implementation(project(Modules.core_lifecycle))
                implementation(project(Modules.feature_auth))
                implementation(project(Modules.feature_contacts))
                implementation(project(Modules.feature_messages))
                implementation(project(Modules.feature_new_message))
                implementation(project(Modules.feature_profile))
                implementation(project(Modules.feature_settings))
                implementation(project(Modules.core_network))

                // Needed for dependency injection
                api(project(Modules.core_db))

                // Compose
                api(compose.ui)
                api(compose.material)
                api(compose.uiTooling)
                api(compose.desktop.currentOs)
                implementation(Compose.material_motion)

                implementation(kotlin("stdlib"))

                // Kodein
                implementation(JvmDependencies.kodein)
                implementation(JvmDependencies.kodein_jni)

                // Koin
                implementation(CommonDependencies.koin_core)
            }
        }

    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

compose.desktop {
    application {
        mainClass = "com.varpihovsky.jetiq.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "JetIQ"
            packageVersion = "1.1.1"

            windows {
                menuGroup = "JetIQ"
                upgradeUuid = "b4700ac2-93ec-44e7-b214-dae0f4300776"
            }
        }
    }

}