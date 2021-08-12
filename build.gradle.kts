buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven(Repositories.jitpack)
        maven(Repositories.jetbrains_compose)
    }

    dependencies {
        classpath(RootDependencies.gradle)
        classpath(kotlin("gradle-plugin", version = Versions.kotlin_version))
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.0.0-alpha3")
        //classpath(RootDependencies.compose_gradle_plugin)
    }
}

group = Config.group
version = Config.version