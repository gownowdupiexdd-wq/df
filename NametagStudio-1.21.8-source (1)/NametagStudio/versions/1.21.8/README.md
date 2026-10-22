# Minecraft 1.21.8

This project currently targets Minecraft Java Edition 1.21.8 using Fabric and Java 21.

The Gradle project at the repository root builds this version. The GitHub Actions workflow at `.github/workflows/build.yml` builds the project and uploads the resulting non-sources JAR as a workflow artifact.

To add another Minecraft version later, create a separate version-specific project/configuration and update the workflow deliberately; do not copy source files into this folder unless the build is also configured to compile them.
