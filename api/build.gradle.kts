plugins {
  id("publishing-conventions")
}

description = "API for extending squaremap, a minimalistic and lightweight world map viewer for Minecraft servers"

java {
  disableAutoTargetJvm()
}

dependencies {
  compileOnlyApi(libs.checkerQual)
}

indra {
  javaVersions {
    target(8)
  }
}
