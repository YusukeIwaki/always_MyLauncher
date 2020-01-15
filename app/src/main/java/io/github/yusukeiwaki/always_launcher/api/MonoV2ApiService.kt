package io.github.yusukeiwaki.always_launcher.api

import retrofit2.http.*

interface MonoV2ApiService {
    @POST("auth/signin")
    suspend fun signIn(@Body req: SignInRequest): SignInResponse

    @GET("pb/subscription-plan/{uuid}/provider")
    suspend fun listProviders(@Path("uuid")uuid: String, @Query("area")area: String?): ListProvidersResponse
}
