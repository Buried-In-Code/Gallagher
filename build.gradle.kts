import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.versions)
    application
}

println("Java v${System.getProperty("java.version")}")
println("Arch: ${System.getProperty("os.arch")}")

group = "github.buriedincode.gallagher"
version = "0.1.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    annotationProcessor(libs.lombok)

    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.spring.boot)
    implementation(libs.lombok)
    implementation(libs.bouncycastle)

    testImplementation(libs.spring.boot.test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

spotless {
    java {
        importOrder()
        removeUnusedImports()
        cleanthat()
            .sourceCompatibility("21")
        googleJavaFormat()
        eclipse()
            .sortMembersEnabled(true)
            .sortMembersOrder("SF,F,SI,I,C,SM,M,T")
            .sortMembersVisibilityOrderEnabled(true)
            .sortMembersVisibilityOrder("B,R,D,V")
        leadingTabsToSpaces(2)
    }
    kotlin {
        ktlint()
    }
    flexmark {
        target("**/*.md")
    }
    json {
        target("**/*.json")
        simple().indentWithSpaces(2)
    }
    yaml {
        target("**/*.yaml")
        jackson().feature("ORDER_MAP_ENTRIES_BY_KEYS", true)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
}
