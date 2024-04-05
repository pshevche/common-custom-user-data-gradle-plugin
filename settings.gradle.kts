plugins {
    id("com.gradle.develocity") version "3.17"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "2"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

def isCI = System.getenv("GITHUB_ACTIONS") != null

develocity {
    server = "https://ge.solutions-team.gradle.com"
    buildScan {
        uploadInBackground = !isCI
        publishing.onlyIf { it.isAuthenticated() }
        obfuscation {
            ipAddresses { addresses -> addresses.collect { address -> "0.0.0.0"} }
        }
    }
}

buildCache {
    local {
        enabled = true
    }

    remote(develocity.buildCache) {
        enabled = true
        // Check access key presence to avoid build cache errors on PR builds when access key is not present
        def accessKey = System.getenv("GRADLE_ENTERPRISE_ACCESS_KEY")
        push = isCI && accessKey != null
    }
}

rootProject.name = "common-custom-user-data-gradle-plugin"
