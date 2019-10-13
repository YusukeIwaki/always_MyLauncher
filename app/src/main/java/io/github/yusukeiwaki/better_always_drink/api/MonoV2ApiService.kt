package io.github.yusukeiwaki.better_always_drink.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MonoV2ApiService {
    @POST("auth/signin")
    suspend fun signIn(@Body req: SignInRequest): SignInResponse

    @GET("pb/subscription-plan/751acbbe/provider")
    suspend fun listDrinkProviders(@Query("area")area: String): ListDrinkProvidersResponse
}
