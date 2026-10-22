# Nametag Studio — Minecraft 1.21.8 (Fabric)

Source project targeting Minecraft Java Edition **1.21.8**, Fabric, and Java 21.

## Build locally

Install a JDK 21 and Gradle 8.12, open a terminal in the repository root (the folder containing `build.gradle`), then run:

```bash
gradle clean build --no-daemon
```

The resulting JAR is placed in `build/libs/`. Do not install a `-sources.jar` file in Minecraft.

## Build on GitHub

The workflow `.github/workflows/build.yml` runs on pushes, pull requests, and manual dispatches. On GitHub, open **Actions**, select **Build Nametag Studio**, and open the completed run. Download the artifact named `nametag-studio-minecraft-1.21.8`.

## Project layout

- `src/main/java/` — Java source code.
- `src/main/resources/` — Fabric metadata, translations, and Mixin configuration.
- `versions/1.21.8/` — notes for the supported Minecraft version. The active Gradle build stays at the repository root.
- `.github/workflows/build.yml` — GitHub Actions build workflow.

## Important

This is a source-project scaffold and has not been verified by a successful Minecraft launch in this environment. GitHub Actions will report compile errors if any source/API details need adjustment; a successful build alone does not replace an in-game test.
