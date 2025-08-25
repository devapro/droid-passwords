App for managing your passwords.

<table>
  <tr>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img.png?raw=true" height="200px" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_1.png?raw=true" height="200px" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_2.png?raw=true" height="200px" />
    </td>
    <td>
      <img src="https://github.com/devapro/droid-passwords/blob/main/screenshots/img_3.png?raw=true" height="200px" />
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

### TODO
- [ ] Modularize the project
- [ ] Add CI for iOS and Android, desktop builds
- [ ] Add tests
- [ ] Add ability to have a few vaults
