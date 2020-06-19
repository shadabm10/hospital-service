package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.myscheduleaddhospital.AddHospitalRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRatingNavigator

class FragmentDoctorMyscheduleViewModel: BaseViewModel<FragmentDoctorMyscheduleNavigator>() {


    fun getHospitalListFromApi(getDoctorUpcommingAppointmentRequest: GetDoctorUpcommingAppointmentRequest) {
        val disposable = apiServiceWithGsonFactory.getMyScheduleHospitalList(getDoctorUpcommingAppointmentRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessHospitalList(response)
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


    fun addHospital(request: AddHospitalRequest) {
        val disposable = apiServiceWithGsonFactory.addHospitalMyschedule(request)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onnSuccessAddHospital(response)
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