package com.rootscare.data.datasource.api


import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.request.doctor.myscheduleaddhospital.AddHospitalRequest
import com.rootscare.data.model.api.request.doctor.myscheduleaddtimeslot.AddTimeSlotRequest
import com.rootscare.data.model.api.request.doctor.myscheduletimeslot.GetTimeSlotMyScheduleRequest
import com.rootscare.data.model.api.request.forgotpassword.forgotpasswordchangerequest.ForgotPasswordChangeRequest
import com.rootscare.data.model.api.request.forgotpassword.forgotpasswordemailrequest.ForgotPasswordSendEmailRequest
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.api.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.data.model.api.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.MyScheduleHospitalResponse
import com.rootscare.data.model.api.response.doctor.myschedule.timeslotlist.MyScheduleTimeSlotResponse
import com.rootscare.data.model.api.response.doctor.payment.PaymentResponse
import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.api.response.doctor.review.ReviewResponse
import com.rootscare.data.model.api.response.forgotpasswordresponse.forgotpasswordchangepassword.ForgotPasswordChangePasswordResponse
import com.rootscare.data.model.api.response.forgotpasswordresponse.forgotpasswordsendmailresponse.ForgotPasswordSendMailResponse
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse
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

    //Get Doctor Profile Api Call
    @POST("api-doctor-profile")
    fun apidoctorprofile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<GetDoctorProfileResponse>

    @POST("api-nurse-profile")
    fun apinurseprofile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<GetDoctorProfileResponse>


    @POST("api-caregiver-profile")
    fun apicaregivereprofile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<com.rootscare.data.model.api.response.caregiver.profileresponse.GetDoctorProfileResponse>

    @Multipart
    @POST("api-service-provider-registration")
    fun apiserviceproviderregistration(
        @Part("user_type") user_type: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,
        @Part("qualification") qualification: RequestBody,
        @Part("passing_year") passing_year: RequestBody,
        @Part("institute") institute: RequestBody,
        @Part("description") description: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("available_time") available_time: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("department") department: RequestBody?=null,
        @Part certificate: List<MultipartBody.Part>,
        @Part image: MultipartBody.Part
    ): Single<RegistrationResponse>

    //Forgot Password Api Call
    @POST("api-forgot-password-email")
    fun apiforgotpasswordemail(@Body forgotPasswordSendEmailRequestBody: ForgotPasswordSendEmailRequest): Single<ForgotPasswordSendMailResponse>

    @POST("api-forgot-change-password")
    fun apiforgotchangepassword(@Body forgotPasswordChangeRequestBody: ForgotPasswordChangeRequest): Single<ForgotPasswordChangePasswordResponse>


    //Doctor Upcoming Appointment
    @POST("api-doctor-upcoming-appointment-list")
    fun apidoctorupcomingappointmentlis(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-nurse-upcoming-appointment-list")
    fun apiNurseUpComingAppointmentList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>


    @POST("api-caregiver-upcoming-appointment-list")
    fun apiCaregiverUpComingAppointmentList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-doctor-upcoming-appointment-list")
    fun apifilterdoctorupcomingappointmentlist(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-nurse-upcoming-appointment-list")
    fun apiFilterNurseUpComingAppointmentList(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-doctor-today-appointment-list")
    fun apidoctortodayappointmentlist(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-nurse-today-appointment-list")
    fun apiNurseTodayAppointmentList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>


    @POST("api-caregiver-today-appointment-list")
    fun apiCaregiverTodayAppointmentList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-doctor-appointment-request-list")
    fun apidoctorappointmentrequestlist(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-nurse-appointment-request-list")
    fun apiNurseAppointmentRequestList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>


    @POST("api-caregiver-appointment-request-list")
    fun apiCaregiverAppointmentRequestList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-doctor-appointment-request")
    fun apiupdatedoctorappointmentrequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-nurse-appointment-request")
    fun apiUpdateNurseAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>


    @POST("api-update-caregiver-appointment-request")
    fun apiUpdateCaregiverAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-department-list")
    fun apidepartmentlist(): Single<DepartmentListResponse>

    @POST("api-doctor-past-appointment-list")
    fun apidoctorappointmentPastList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>


    @POST("api-nurse-past-appointment-list")
    fun apiNurseAppointmentPastList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>


    @POST("api-caregiver-past-appointment-list")
    fun apiCaregiverAppointmentPastList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>


    @Multipart
//    @POST("api-sp-doctor-edit-profile")
    @POST("api-sp-edit-profile")
    fun apiHitUpdateProfileWithImageAndCertificate(
        @Part("user_id") user_id: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("qualification") qualification: RequestBody?=null,
        @Part("passing_year") passing_year: RequestBody?=null,
        @Part("institute") institute: RequestBody?=null,
        @Part("description") description: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("available_time") available_time: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("department") department: RequestBody,
        @Part image: MultipartBody.Part?=null,
        @Part certificate: List<MultipartBody.Part>?=null
    ): Single<LoginResponse>

//@Multipart
//@POST("api-sp-doctor-edit-profile")
//fun apiHitUpdateProfileWithImageAndCertificate(
//    @Part("user_id") user_id: RequestBody,
//    @Part("first_name") first_name: RequestBody,
//    @Part("last_name") last_name: RequestBody,
//    @Part("email") email: RequestBody,
//    @Part("mobile_number") mobile_number: RequestBody,
//    @Part("dob") dob: RequestBody,
//    @Part("gender") gender: RequestBody,
//    @Part("description") description: RequestBody,
//    @Part("experience") experience: RequestBody,
//    @Part("available_time") available_time: RequestBody,
//    @Part("fees") fees: RequestBody,
//    @Part("department") department: RequestBody
//): Single<RegistrationResponse>


    @POST("api-doctor-review")
    fun getDoctorReview(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>

    @POST("api-nurse-review")
    fun getNurseReview(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>


    @POST("api-caregiver-review")
    fun getCaregiverReview(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>

    @POST("api-doctor-payment-history")
    fun getPaymentHistory(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-nurse-payment-history")
    fun getPaymentHistoryForNurse(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-caregiver-payment-history")
    fun getPaymentHistoryForCaregiver(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-doctor-hospital-clinic")
    fun getMyScheduleHospitalList(@Body getDoctorUpcommingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<MyScheduleHospitalResponse>

    @POST("api-insert-doctor-private-clinic")
    fun addHospitalMyschedule(@Body getDoctorUpcommingAppointmentRequestBody: AddHospitalRequest): Single<CommonResponse>


    @POST("api-doctor-clinic-private-slot")
    fun getAllTimeSlotMyschedule(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-doctor-clinic-slot-filter")
    fun getDaySpecificTimeSlotMyschedule(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-delete-doctor-private-slot")
    fun apiHitRemoveTimeSlotMySchedule(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-insert-doctor-private-slot")
    fun apiHitSaveTimeSlotForDoctor(@Body request: AddTimeSlotRequest): Single<CommonResponse>

    @POST("api-appointment-details")
    fun getAppointmnetDetails(@Body request: CommonUserIdRequest): Single<AppointmentDetailsResponse>


    @POST("api-doctor-complete-appointment-status")
    fun apiHitForMarkAsComplete(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @POST("api-nurse-complete-appointment-status")
    fun apiHitForMarkAsCompleteForNurse(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @Multipart
    @POST("api-doctor-insert-prescription")
    fun uploadPrescription(
        @Part("patient_id") patient_id: RequestBody,
        @Part("doctor_id") doctor_id: RequestBody,
        @Part("appointment_id") appointment_id: RequestBody,
        @Part("prescription_number") prescription_number: RequestBody,
        @Part prescription: MultipartBody.Part?=null
    ): Single<CommonResponse>


    // for nurse
    @POST("api-hourly-rates")
    fun savePriceForSlot1(@Body requestBody: RequestBody): Single<LoginResponse>

    @POST("api-daily-rates")
    fun saveHourlyPrice(@Body requestBody: RequestBody): Single<LoginResponse>
}
