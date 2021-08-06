package extensions

import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.kotlin.dsl.get

fun Project.kapt(options: String) {
    val split = options.split(":")
    configurations["kapt"].dependencies.add(DefaultExternalModuleDependency(split[0], split[1], split[2]))
}
