plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    macosArm64("macos") {
        binaries {
            executable {
                entryPoint = "main"
                baseName = "app-macos"
            }
        }
    }

    linuxX64("linux") {
        binaries {
            executable {
                entryPoint = "main"
                baseName = "app-linux"
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                // common deps should go here
            }
        }
    }
}
