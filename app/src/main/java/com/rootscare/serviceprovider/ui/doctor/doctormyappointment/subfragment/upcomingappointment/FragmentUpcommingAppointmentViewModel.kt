package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentUpcommingAppointmentViewModel : BaseViewModel<FragmentUpcommingAppointmentNavigator>()  {

    fun apidoctorupcomingappointmentlis(getDoctorUpcommingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apidoctorupcomingappointmentlis(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorUpcomingAppointmentResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){

//                        appSharedPref?.loginmodeldata=loginresposeJson
//                        appSharedPref?.loginUserType=response?.result?.userType


                        //To get the data
//                        val json: String = mPrefs.getString("MyObject", "")
//                        val json: String =appSharedPref?.loginmodeldata!!
//                        val obj: Result = gson.fromJson(json, Result::class.java)
//                        appSharedPref?.userName=response?.result?.firstName+" "+response?.result?.lastName
//                        appSharedPref?.userEmail=response?.result?.email
//                        appSharedPref?.userImage=response?.result?.image


                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorDoctorUpcomingAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apifilterdoctorupcomingappointmentlist(filterAppointmentRequest: FilterAppointmentRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apifilterdoctorupcomingappointmentlist(filterAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDoctorUpcomingAppointmentResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){


//                        appSharedPref?.loginmodeldata=loginresposeJson
//                        appSharedPref?.loginUserType=response?.result?.userType


                        //To get the data
//                        val json: String = mPrefs.getString("MyObject", "")
//                        val json: String =appSharedPref?.loginmodeldata!!
//                        val obj: Result = gson.fromJson(json, Result::class.java)
//                        appSharedPref?.userName=response?.result?.firstName+" "+response?.result?.lastName
//                        appSharedPref?.userEmail=response?.result?.email
//                        appSharedPref?.userImage=response?.result?.image


                    }

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorDoctorUpcomingAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }





    fun apiupdatedoctorappointmentrequest(
        updateAppointmentRequestBody: UpdateAppointmentRequest,
        position: Int
    ) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiupdatedoctorappointmentrequest(updateAppointmentRequestBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorRequestAppointmentUpdateResponse(response, position)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorDoctorUpcomingAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}