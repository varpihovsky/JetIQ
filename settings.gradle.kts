dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter() // Warning: this repository is going to shut down soon
        maven("https://www.jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
include(":core_test")
include(":feature_profile")
include(":feature_messages")
include(":feature_contacts")
include(":feature_new_message")
include(":feature_auth")
include(":feature_settings")
include(":feature_subjects")
include(":core_ui")
