package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class FragmentRegistrationStepThreeViewModel : BaseViewModel<FragmentRegistrationStepThreeNavigator>() {

    fun apieditpatientprofilepersonal(user_type: RequestBody, first_name: RequestBody, last_name: RequestBody, email: RequestBody, mobile_number: RequestBody,dob: RequestBody,
                                      gender: RequestBody,password: RequestBody,
                                      confirm_password: RequestBody,image: MultipartBody.Part? = null,
                                      certificate: MultipartBody.Part? = null,qualification: RequestBody,passing_year: RequestBody,institute: RequestBody,
                                      description: RequestBody,experience: RequestBody,available_time: RequestBody,fees: RequestBody,department: RequestBody) {
//        userId: RequestBody,first_name: RequestBody,last_name: RequestBody,id_number: RequestBody,status: RequestBody,image: MultipartBody.Part? = null
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        var disposable: Disposable? = null
        if (image != null) {
            disposable = apiServiceWithGsonFactory.apiserviceproviderregistration(user_type,first_name,last_name,email,
                mobile_number,dob,gender,password,confirm_password,image,
                certificate!!,qualification,passing_year,institute,description,experience,available_time,fees,department)
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        // Store last login time
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.successRegistrationResponse(response)
                    } else {
                        Log.d("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
                        navigator.errorRegistrationResponse(throwable)
                        Log.d("check_response_error", ": " + throwable.message)
                    }
                })
        }
        compositeDisposable.add(disposable!!)
    }
}