# Convention Plugins

This folder (`build-logic`) defines project-specific **Gradle convention plugins**, used to maintain a centralized, clean, and scalable approach to module configuration across the project.

> This implementation is adapted from Google’s [Now in Android](https://github.com/android/nowinandroid) project.

---

## Why use convention plugins?

By using convention plugins, we avoid:

- Repetitive build script configurations.
- Messy and error-prone `subprojects {}` blocks.
- The limitations and complications of `buildSrc`.

This approach draws inspiration from:
- [Herding Elephants – Square Engineering Blog](https://developer.squareup.com/blog/herding-elephants/)
- [Idiomatic Gradle by Johannes](https://github.com/jjohannes/idiomatic-gradle)

---

## Structure

- The `build-logic` module is an **included build**, configured in the root [`settings.gradle.kts`](../settings.gradle.kts).
- It includes:
  - A `convention` module that defines reusable Gradle plugins.
  - Shared Kotlin logic that can be reused across plugins (especially useful for Android-specific configurations).

---

## Design Philosophy

These plugins follow a **single-responsibility** and **composable** design. Each plugin:
- Configures only what is necessary.
- Can be combined with other plugins as needed.

> If a module needs one-off logic that isn't shared elsewhere, it's better to define it directly in that module’s `build.gradle.kts`.

---

## Available Convention Plugins

- Application and Library Configuration:
  - `convention.android.application`
  - `convention.android.library`
  - `convention.android.test`
- Jetpack Compose Support:
  - `convention.android.application.compose`
  - `convention.android.library.compose`

> (Replace `convention` with your actual namespace if needed.)

---

## License & Attribution

This project contains modified code from the [Now in Android](https://github.com/android/nowinandroid) project by Google, used under the terms of the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).  
Modifications were made to better suit the architecture and needs of this application.

You can find the full license in the [LICENSE](../LICENSE) file.
