import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("maven-publish") // Support for maven publishing artifacts
    id ("signing") // Support for signing artifacts
}

android {
    namespace = "io.github.afalabarce.jetpackcompose.ads.wortise"
    compileSdk = Constants.targetAndCompilingSdk

    defaultConfig {
        minSdk = Constants.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion  = Constants.composeCompilerVersion
    }
}

dependencies {

    implementation("androidx.core:core-ktx:${Constants.androidxCoreKtxVersion}")
    implementation("androidx.appcompat:appcompat:${Constants.androidxAppCompatVersion}")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:${Constants.lifeCycleRuntimeVersion}")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:${Constants.lifeCycleRuntimeVersion}")
    implementation ("androidx.compose.ui:ui:${Constants.composeVersion}")
    implementation ("androidx.compose.ui:ui-tooling:${Constants.composeVersion}")
    implementation ("androidx.compose.ui:ui-tooling-preview:${Constants.composeVersion}")
    implementation ("androidx.activity:activity-compose:${Constants.activityComposeVersion}")
    implementation ("androidx.constraintlayout:constraintlayout-compose:${Constants.constraintLayoutComposeVersion}")
    implementation ("androidx.compose.material3:material3:${Constants.composeMaterial3Version}")

    // ads dependencies
    implementation ("com.wortise:android-sdk:1.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

val androidSourcesJarTask by tasks.register("androidSourcesJar", Jar::class.java) {
    archiveClassifier.set("sources")

    android.sourceSets.toTypedArray().forEach {
        println("${it.name}(${it.java.name}) -> ${it.java}")
    }
    val mainSourceSet = android.sourceSets.first{sourceSets -> sourceSets.name == "main"}

    from(mainSourceSet.java.srcDirs).source

}

artifacts {
    archives(androidSourcesJarTask)
}

group = SonatypePublishingCommon.publishedGroupId
version = SonatypePublishingCommon.libraryVersionId

val signingData = mutableMapOf(
    "signing.keyId" to "",
    "signing.password" to "",
    "signing.secretKeyRingFile" to "",
    "ossrhUsername" to "",
    "ossrhPassword" to ""
)

val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    println("Found secret props file, loading props")
    val props = Properties()
    props.load(FileInputStream(secretPropsFile))
    for ((name, value) in props) {
        println("Prop: $name -> $value")
        signingData[name.toString()] = value.toString()
    }
}

publishing {
    publications {
        create<MavenPublication>("Maven"){
            groupId = SonatypePublishingCommon.publishedGroupId
            artifactId = SonatypePublishingWortise.artifact
            version = SonatypePublishingCommon.libraryVersionCode

            println ("groupId: ${SonatypePublishingCommon.publishedGroupId}")
            println ("Artifact: ${SonatypePublishingWortise.artifact}")
            println ("Version: ${SonatypePublishingCommon.libraryVersionCode}")
        }
        withType<MavenPublication>{
            println(project.name)
            // Two artifacts, the `aar` and the sources
            artifact("$buildDir/outputs/aar/${project.name}-release.aar")
            artifact(androidSourcesJarTask)


            pom {
                packaging = "aar"
                name.set(SonatypePublishingWortise.artifact)
                description.set(SonatypePublishingWortise.libraryDescription)
                url.set(SonatypePublishingCommon.gitUrl)
                licenses {
                    license {
                        name.set(SonatypePublishingCommon.licenseName)
                        url.set(SonatypePublishingCommon.licenseUrl)
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/afalabarce/jetpackcompose-admob/issues")
                }
                scm {
                    connection.set("scm:git:${SonatypePublishingCommon.gitPath}")
                    developerConnection.set("scm:git:ssh://${SonatypePublishingCommon.gitPath}")
                    url.set("https://github.com/afalabarce/jetpackcompose-admob/tree/master")
                }
                developers {
                    developer {
                        id.set(SonatypePublishingCommon.developerId)
                        name.set(SonatypePublishingCommon.developerName)
                        email.set(SonatypePublishingCommon.developerEmail)
                    }
                }

                // A slightly hacky fix so that your POM will include any transitive dependencies
                // that your library builds upon
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    project.configurations.implementation.configure {
                        dependencies.forEach { dependency ->
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dependency.group)
                            dependencyNode.appendNode("artifactId", dependency.name)
                            dependencyNode.appendNode("version", dependency.version)
                        }
                    }

                }
            }
        }
    }
    repositories {
        // The repository to publish to, Sonatype/MavenCentral
        maven {
            // This is an arbitrary name, you may also use "mavencentral" or
            // any other name that's descriptive for you
            name = "sonatype"
            // these urls depend on the configuration provided to the user by sonatype
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            // You only need this if you want to publish snapshots, otherwise just set the URL
            // to the release repo directly

            setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            // The username and password we've fetched earlier
            credentials {
                username = signingData["ossrhUsername"]
                password = signingData["ossrhPassword"]
            }
        }
    }
}


signing {
    val keyRingPrivateKey = File(signingData["signing.secretKeyRingFile"].toString()).readText(Charsets.UTF_8)
    println("Signing KeyId: ${signingData["signing.keyId"]}")
    println("Signing Password: ${signingData["signing.password"]}")
    println("Signing Key:\n$keyRingPrivateKey\n---------")
    useInMemoryPgpKeys(
        signingData["signing.keyId"],
        keyRingPrivateKey,
        signingData["signing.password"],
    )
    publishing.publications.forEach { publication ->
        println("Signing [${publication.name}] Publication...")
        sign(publication)
    }
}