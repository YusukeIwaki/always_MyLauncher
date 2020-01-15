package io.github.yusukeiwaki.always_launcher.api

import io.github.yusukeiwaki.always_launcher.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AlwaysApiClient {
    private val monoV2 = Retrofit.Builder()
        .baseUrl("https://api.always.fan/mono/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val monoV2Api = monoV2.create(MonoV2ApiService::class.java)

    suspend fun signIn(signInRequest: SignInRequest): SignInResponse {
        return monoV2Api.signIn(signInRequest)
    }

    suspend fun listProviders(area: String? = null): ListProvidersResponse {
        return monoV2Api.listProviders(BuildConfig.SERVICE_UUID, area)
    }
}
