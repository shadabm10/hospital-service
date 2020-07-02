package com.rootscare.serviceprovider.ui.login.subfragment.login

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import java.util.*


class FragmentLoginViewModel : BaseViewModel<FragmentLoginNavigator>() {

    fun apiserviceproviderlogin(loginRequest: LoginRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.apiserviceproviderlogin(loginRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))

                    /* Saving access token after singup or login */
                    if (response.result!= null){
                        appSharedPref?.deleteLoginModelData()
                        val gson = Gson()
                        val loginresposeJson = gson.toJson(response)
                        appSharedPref?.loginmodeldata=loginresposeJson
                        appSharedPref?.loginUserType=response?.result?.userType
                        appSharedPref?.loginUserId=response?.result?.userId

                        if (loginRequest.userType?.toLowerCase(Locale.ROOT)?.contains("nurse")!!){
                            appSharedPref?.loggedInDataForNurseAfterLogin = Gson().toJson(response.result)
                        }

                        //To get the data
//                        val json: String = mPrefs.getString("MyObject", "")
//                        val json: String =appSharedPref?.loginmodeldata!!
//                        val obj: Result = gson.fromJson(json, Result::class.java)
//                        appSharedPref?.userName=response?.result?.firstName+" "+response?.result?.lastName
//                        appSharedPref?.userEmail=response?.result?.email
//                        appSharedPref?.userImage=response?.result?.image



                    }
                    navigator.successLoginResponse(response)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorLoginResponse(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
}