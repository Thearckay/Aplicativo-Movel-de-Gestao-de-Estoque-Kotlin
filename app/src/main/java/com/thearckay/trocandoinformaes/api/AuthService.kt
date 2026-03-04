package com.thearckay.trocandoinformaes.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class ItemRequest(
    val name: String,
    val itemCode: String,
    val quantity: Int,
    val price: Double,
    val type: String
)

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
    val timestamp: String
)

data class DashboardData(
    val totalInventoryValue: Double,
    val inventoryChangePercentage: Double,
    val recentActivities: List<ActivityData>
)

data class ActivityData(
    val itemName: String,
    val statusDescription: String,
    val timeAgo: String,
    val type: String
)

data class StockItem(
    val id: Int,
    val name: String,
    val itemCode: String,
    val price: Double,
    val quantity: Int,
    val category: String? = null
)

interface AuthService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<Int>>

    @POST("users/register")
    suspend fun register(@Body request: UserRegisterRequest): Response<ApiResponse<Int>>

    @GET("api/itens/dashboard")
    suspend fun getDashboardData(@Query("userId") userId: Int): Response<ApiResponse<DashboardData>>

    @POST("api/itens/add")
    suspend fun registerItem(
        @Query("userId") userId: Int,
        @Body request: ItemRequest
    ): Response<ApiResponse<Any>>

    @GET("api/itens/stock")
    suspend fun getStockItems(@Query("userId") userId: Int): Response<ApiResponse<List<StockItem>>>
}