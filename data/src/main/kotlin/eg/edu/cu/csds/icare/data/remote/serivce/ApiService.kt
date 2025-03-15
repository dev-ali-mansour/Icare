package eg.edu.cu.csds.icare.data.remote.serivce

import eg.edu.cu.csds.icare.core.domain.model.LoginInfoResponse
import eg.edu.cu.csds.icare.core.domain.model.RegisteredResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("isRegistered")
    suspend fun isRegistered(
        @Body body: HashMap<String, String>,
    ): Response<RegisteredResponse>

    @POST("userReg")
    suspend fun register(
        @Body body: HashMap<String, String>,
    ): Response<LoginInfoResponse>

    @POST("loginInfo")
    suspend fun getLoginInfo(
        @Body body: HashMap<String, String>,
    ): Response<LoginInfoResponse>
}
