package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class FragmentRegistrationStepThreeViewModel : BaseViewModel<FragmentRegistrationStepThreeNavigator>() {

    fun apieditpatientprofilepersonal(user_type: RequestBody, first_name: RequestBody, last_name: RequestBody, email: RequestBody, mobile_number: RequestBody,dob: RequestBody,
                                      gender: RequestBody,password: RequestBody,
                                      confirm_password: RequestBody,image: MultipartBody.Part? = null,
                                      certificate: List<MultipartBody.Part>? = null,qualification: RequestBody,passing_year: RequestBody,institute: RequestBody,
                                      description: RequestBody,experience: RequestBody,available_time: RequestBody,fees: RequestBody,department: RequestBody) {
//        userId: RequestBody,first_name: RequestBody,last_name: RequestBody,id_number: RequestBody,status: RequestBody,image: MultipartBody.Part? = null
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        var disposable: Disposable? = null
        if (image != null) {
            disposable = apiServiceWithGsonFactory.apiserviceproviderregistration(user_type,first_name,last_name,email,
                mobile_number,dob,gender,password,confirm_password,qualification,passing_year,institute,description,experience,available_time,
                fees,department, certificate!!,image)
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

    fun apidepartmentlist() {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apidepartmentlist()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDepartmentListResponse(response)
                    /* Saving access token after singup or login */
                    if (response.result!= null){
//                        appSharedPref?.deleteLoginModelData()
//                        val gson = Gson()
//                        val loginresposeJson = gson.toJson(response)
//                        appSharedPref?.loginmodeldata=loginresposeJson
//                        appSharedPref?.loginUserType=response?.result?.userType
//                        appSharedPref?.loginUserId=response?.result?.userId


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
                    navigator.errorRegistrationResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}