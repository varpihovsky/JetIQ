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
        classpath(RootDependencies.kotlin_gradle_plugin)
        classpath(RootDependencies.hilt_gradle_plugin)
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.0.0-alpha2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        //classpath(RootDependencies.compose_gradle_plugin)
    }
}

group = Config.group
version = Config.version