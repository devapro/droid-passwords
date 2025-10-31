App for managing your passwords.

<table>
  <tr>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_1.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_2.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_3.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_8.png?raw=true" width="200" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_4.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_5.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_6.png?raw=true" width="200" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_7.png?raw=true" width="200" />
    </td>
  </tr>
</table>

# Password Manager

This is a password manager application built using Kotlin Multiplatform and Jetpack Compose.

The main features include:

- Cross-platform support for Android, iOS, and Desktop.
- Local storage of passwords.
- Customizable additional fields.

# Technologies Used

- Compose Multiplatform.
- MVI (Model-View-Intent) architecture for state management.
- Koin DI (Dependency Injection) for managing dependencies.
- androidx.datastore for setting storage.
- [Voyager for navigation](https://github.com/adrielcafe/voyager).
- [FileKit for file handling](https://github.com/vinceglb/FileKit).
- [CryptoKit for encryption](https://github.com/whyoleg/cryptography-kotlin).
- Multi-modules architecture for better code organization.

This project is inspired by [Buttercup Desktop](https://github.com/buttercup/buttercup-desktop)

## Building and Running

### Android

Run: `./gradlew :composeApp:installDebug`

### Desktop

Run: `./gradlew :composeApp:run`

Packaging: 

`./gradlew :composeApp:packageReleaseDmg` (macOS),
`./gradlew :composeApp:packageReleaseMsi` (Windows),
`./gradlew :composeApp:packageReleaseDeb` (Linux)

### TODO
- [ ] Add CI for iOS and Android, desktop builds
- [ ] Add tests
- [ ] Add ability to have a few vaults
