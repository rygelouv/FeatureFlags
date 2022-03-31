# Feature Flags Debug Menu
<p align="center">
  <img src="https://github.com/rygelouv/FeatureFlags/blob/main/on-button.png" width="200">
</p>

Feature Flags are essential for software development and release management

This work is inspired and is an update and improvement (in my opinion) of the original Feature Flags architecture done by Joeren Mols
- UI was built with Jetpack Compose
- Dependency Injection was added using Hilt
- Several class were renamed and completely re-imagined
- TestFeatureFlagProvider moved to test source set and configured to easily enable or disable feature but still used with FeatureManager
- FeatureManager will be used in Test but a different instance will be injected. The test instance will remove all the other providers and keep only TestFeatureFlagProvider

## Screenshots
<p align="center">
<img src="https://github.com/rygelouv/FeatureFlags/blob/main/screenshot-1648756729152.png" width="200">   <img src="https://github.com/rygelouv/FeatureFlags/blob/main/screenshot-1648756740397.png" width="200">   <img src="https://github.com/rygelouv/FeatureFlags/blob/main/screenshot-1648756748689.png" width="200">
</p>

## Code details
- A Feature uniquely identifies a part of the app code that can either be enabled or disabled.
Features only have two states by design to simplify the implementation
- Every provider has an explicit priority so they can override each other (e.g. "Remote Config tool" > Store).
Not every provider has to provide a flag value for every feature. This is to avoid implicitly relying on build-in
defaults (e.g. "Remote Config tool" returns false when no value for a feature) and to avoid that every provider has to provide a
value for every feature. (e.g. no "Remote Config tool" configuration needed, unless you want the toggle to be remote)
  