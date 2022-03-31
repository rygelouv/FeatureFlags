# Feature Flags Debug Menu
<p align="center">
  <img src="https://github.com/rygelouv/FeatureFlags/blob/main/on-button.png" width="200">
</p>

Feature Flags are essential for software development and release management

This work is inspired and is an update and improvement (in my opinion) of the original Feature Flags architecture done by Joeren Mols
- UI was built with Jetpack Compose
- Dependency Injection was added using Hilt
- Several components were renamed and completely re-imagined
- TestFeatureFlagProvider moved to test source set and configured to easily enable or disable feature but still used with FeatureManager
- FeatureManager will be used in Test but a different instance will be injected. The test instance will remove all the other providers and keep only TestFeatureFlagProvider
- Removed the priority system as it was ambiguous and was adding complexity
- Added an exclusion system to enforce uniqueness of Flags provider by provider
- Created specific types of feature flags for each provider except Local provider which can provider feature from other providers in debug mode.

## Screenshots
<p align="center">
<img src="https://github.com/rygelouv/FeatureFlags/blob/main/screenshot-1648756729152.png" width="200">   <img src="https://github.com/rygelouv/FeatureFlags/blob/main/screenshot-1648756740397.png" width="200">   <img src="https://github.com/rygelouv/FeatureFlags/blob/main/screenshot-1648756748689.png" width="200">
</p>

## Architecture implementation

### Feature
- A Feature uniquely identifies a part of the app code that can either be enabled or disabled.
Features only have two states by design to simplify the implementation

### Feature Flag Providers
- Every provider has an explicit priority so they can override each other (e.g. "Remote Config tool" > Store).
Not every provider has to provide a flag value for every feature. This is to avoid implicitly relying on build-in
defaults (e.g. "Remote Config tool" returns false when no value for a feature) and to avoid that every provider has to provide a
value for every feature. (e.g. no "Remote Config tool" configuration needed, unless you want the toggle to be remote)

### Feature Manager
- Check whether a feature is enabled or not. Will first look into the providers to see which one has the feature
  Normally, based on how the framework is built, two provider should provide the same feature flag at the same time
  That would be a violation of the design. Each provider should provider a pool of feature flags that are different
  from what the providers are providing.

#### Firebase Feature Flag Provider
- Provider of feature flags set up on Firebase Remote Config. Usually used in production for toggling
mobile feature. Used by both mobile engineers and product managers
This is refreshable because Firebase Remote Configs can be refresh programmatically from the app code
#### Server Feature Flag Provider
- Provider of Feature Flags own by the backend. These feature flags are based on business logic.
The one implemented in this repository is purely fictional. A real world implementation might look very different.
#### Local Feature Flag Provider
- Provides all possible feature flags in the system. This include `RemoteConfigFlag`, `ServerFlag`
and its own `DebugConfig`. This is the priority feature flag provider for a Debug build
It has the ability to enable `FirebaseFeatureFlagProvider` and `ServerFeatureFlagProvider` and when
that's the case, the flags provided by those provider are just ignored (excluded)
- This provider is the one that is used to build and auto-generate the debug menu
- It is `UpdatableFeatureFlagProvider` because these flags can be turned on/off from the debug menu