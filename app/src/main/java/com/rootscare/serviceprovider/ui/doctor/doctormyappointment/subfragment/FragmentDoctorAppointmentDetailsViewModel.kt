package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointmentNavigator

class FragmentDoctorAppointmentDetailsViewModel : BaseViewModel<FragmentDoctorAppointmentDetailsNavigator>() {


    fun getDetails(getDoctorUpcommingAppointmentRequest: CommonUserIdRequest) {
        val disposable = apiServiceWithGsonFactory.getAppointmnetDetails(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessDetails(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.onThrowable(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

}