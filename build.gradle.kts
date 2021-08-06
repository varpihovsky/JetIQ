buildscript {
    repositories {
        google()
        mavenCentral()
        maven(Repositories.jitpack)
        //maven(Repositories.jetbrains_compose)
    }

    dependencies {
        classpath(RootDependencies.gradle)
        classpath(RootDependencies.kotlin_gradle_plugin)
        classpath(RootDependencies.hilt_gradle_plugin)
        classpath("androidx.compose.compiler:compiler:1.0.0")
        //classpath(RootDependencies.compose_gradle_plugin)
    }
}

group = Config.group
version = Config.version