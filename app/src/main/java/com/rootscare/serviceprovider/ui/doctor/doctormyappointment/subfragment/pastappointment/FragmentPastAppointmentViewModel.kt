package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel

class FragmentPastAppointmentViewModel : BaseViewModel<FragmentPastAppointmentNavigator>() {
    fun apidoctorappointmentPastList(getDoctorUpcommingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.apidoctorappointmentPastList(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.responseListPastAppointment(response)
                    /* Saving access token after singup or login */

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorGetDoctorRequestAppointmentResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

}