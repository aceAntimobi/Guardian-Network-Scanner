# Guardian Network Scanner

Guardian Network Scanner is an on-device Android application concept that empowers privacy-conscious users to understand and monitor the security posture of the networks they join. The project currently ships with a fully local Jetpack Compose prototype that demonstrates user flows for Wi-Fi scanning, network safety assessment, privacy threat detection, Bluetooth tracker discovery, professional troubleshooting tools, and notification preferences.

## Project structure

```
Guardian-Network-Scanner/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/guardian/networkscanner/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── GuardianRepository.kt
│       │   │   └── Models.kt
│       │   ├── feature/
│       │   │   ├── bluetooth/BluetoothScreen.kt
│       │   │   ├── privacy/PrivacyScreen.kt
│       │   │   ├── scan/ScanScreen.kt
│       │   │   ├── security/SecurityScreen.kt
│       │   │   ├── settings/SettingsScreen.kt
│       │   │   └── tools/ToolsScreen.kt
│       │   └── ui/
│       │       ├── navigation/
│       │       │   ├── GuardianApp.kt
│       │       │   └── GuardianDestinations.kt
│       │       └── theme/
│       │           ├── Color.kt
│       │           ├── Theme.kt
│       │           └── Type.kt
│       └── res/
│           ├── values*/strings.xml
│           ├── values/themes.xml
│           └── xml/
│               ├── backup_rules.xml
│               └── data_extraction_rules.xml
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

## Development notes

* The repository layer intentionally returns deterministic mock data so the prototype can run without sensitive permissions during early development. Replace `GuardianRepository` with platform-specific scanners when implementing production features.
* All analytics and security assessments are designed to execute entirely on the device in alignment with the product privacy requirements.
* The Gradle wrapper JAR is not committed in this environment. Run `gradle wrapper` on a local machine to generate it if you plan to build the application directly.

## Roadmap ideas

* Integrate real Wi-Fi, ARP, and Bluetooth scanning implementations gated behind runtime permission flows.
* Persist device labels, trust states, and tool logs using Room or DataStore.
* Implement sensor-driven privacy scanning modes (magnetometer, camera IR filter, microphone analysis) with clear UX guidance and consent prompts.
* Add localization coverage for all UI strings (English, Chinese, Japanese currently supported) and refine copy for target markets.
