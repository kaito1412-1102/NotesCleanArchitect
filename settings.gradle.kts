pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NotesCleanArchitecture"
include (":app")
include (":core")
include (":core:database")
include (":core:model")
include (":core:data")
include (":core:domain")
include (":core:datastore")
