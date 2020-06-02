package com.rootscare.data.datasource.api





import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //    @Headers({
    //            "x-api-key: 123456"
    //    })

//    @POST("api-login")
//    fun login(@Body loginRequestBody: LogInRequest): Single<LogInResponse>
////
////    @POST("admin/api-student-registration")
////    fun apistudentregistration(@Body registrationRequestBody: RegistrationRequest): Single<RegistrationResponse>
////
//    @GET("api-product-list")
//    fun getOurProductList(): Single<OurProductListResponse>

    //Login Api Call
    @POST("api-service-provider-login")
    fun apiserviceproviderlogin(@Body loginRequestBody: LoginRequest): Single<LoginResponse>



}
