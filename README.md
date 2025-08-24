App for managing your passwords.

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