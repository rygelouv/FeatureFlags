Feature Flags Debug Menu

![Screenshot](on-button.png)

Feature Flags are essential for software development and release management

This work is inspired and is an update and improvement (in my opinion) of the original Feature Flags architecture done by Joeren Mols
- UI was built with Jetpack Compose
- Dependency Injection was added using Hilt
- Several class were renamed and completely re-imagined
- TestFeatureFlagProvider moved to test source set and configured to easily enable or disable feature but still used with FeatureManager
- FeatureManager will be used in Test but a different instance will be injected. The test instance will remove all the other providers and keep only TestFeatureFlagProvider


![Screenshot](screenshot-1648756729152.png) ![Screenshot](screenshot-1648756740397.png) ![Screenshot](screenshot-1648756748689.png)
