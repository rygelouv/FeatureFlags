package com.example.featureflagdebugmenu.framework

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class ServerFeatureFlagProvider @Inject constructor(private val api: ServerApi) :
    FeatureFlagProvider {

    private val features: Array<ServerFlag> = ServerFlag.values()
    private val user by lazy { api.getUser() }

    override fun isFeatureEnabled(feature: Feature): Boolean {
        return when (feature) {
            ServerFlag.USER_BANNED -> user.isBanned
            ServerFlag.USER_PEP -> user.isPep
            else -> feature.defaultValue
        }
    }

    override fun hasFeature(feature: Feature): Boolean {
        if (feature !is ServerFlag) return false
        return features.contains(feature)
    }
}

enum class ServerFlag(
    override val key: String,
    override val title: String,
    override val explanation: String,
    override val defaultValue: Boolean = true
) : Feature {
    USER_BANNED("feature.user_banned", "User Banned", "User gets banned from the app"),
    USER_PEP(
        "feature.pep",
        "Politically Exposed Person",
        "The user is identified as PEP and receives special treatment"
    );

    companion object {
        fun getOrNull(key: String): ServerFlag? = values().find { it.key == key }
    }
}

data class User(
    val id: String,
    val isBanned: Boolean,
    val isPep: Boolean
)


interface ServerApi {
    fun getUser(): User
}

class ServerApiImpl @Inject constructor() : ServerApi {
    override fun getUser() = User(
        id = "user_id_00",
        isBanned = false,
        isPep = true
    )
}

@Module
@InstallIn(SingletonComponent::class)
interface AnswerBuilderModule {

    @Binds
    fun ServerApiImpl.provideAnswerFlowBuilder(): ServerApi
}
