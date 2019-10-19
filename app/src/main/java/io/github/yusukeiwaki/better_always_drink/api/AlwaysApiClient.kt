package io.github.yusukeiwaki.better_always_drink.api

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

    suspend fun listDrinkProviders(area: String? = null): ListDrinkProvidersResponse {
        return monoV2Api.listDrinkProviders(area)
    }
}
