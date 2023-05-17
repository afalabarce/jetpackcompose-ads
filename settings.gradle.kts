import java.net.URI

include(":ads-applovin")


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
        maven { url = URI("https://maven.wortise.com/artifactory/public") }
    }
}

rootProject.name = "JetpackComposeComponentsAdsAdmob"
include(":ads-admob")
include(":ads-wortise")
