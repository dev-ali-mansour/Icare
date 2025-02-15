package eg.edu.cu.csds.icare.data.remote.serivce

import eg.edu.cu.csds.icare.core.domain.model.LoginInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("userReg")
    suspend fun register(
        @Body body: HashMap<String, String>,
    ): Response<LoginInfoResponse>

    @POST("login")
    suspend fun getLoginInfo(
        @Body body: HashMap<String, String>,
    ): Response<LoginInfoResponse>
}
