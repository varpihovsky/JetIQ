dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven("https://www.jitpack.io")
    }
}
rootProject.name = "JetIQ"
include(":app")
include(":core_repo")
include(":core")
include(":ui_data")
include(":core_network")
include(":core_db")
include(":repo_data")
include(":core_nav")
