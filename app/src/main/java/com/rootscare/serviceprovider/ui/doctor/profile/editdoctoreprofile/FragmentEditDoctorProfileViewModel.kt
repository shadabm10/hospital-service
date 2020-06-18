package com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileNavigator
import io.reactivex.disposables.Disposable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentEditDoctorProfileViewModel : BaseViewModel<FragmentEditDoctorProfileNavigator>() {
    fun apidepartmentlist() {
        val disposable = apiServiceWithGsonFactory.apidepartmentlist()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successDepartmentListResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apidoctorprofile(commonUserIdRequest: CommonUserIdRequest) {
        val disposable = apiServiceWithGsonFactory.apidoctorprofile(commonUserIdRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successGetDoctorProfileResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }


    fun apiHitForUdateProfileWithProfileAndCertificationImage(
        user_id: RequestBody,
        first_name: RequestBody,
        last_name: RequestBody,
        email: RequestBody,
        mobile_number: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        qualification: RequestBody,
        passing_year: RequestBody,
        institute: RequestBody,
        description: RequestBody,
        experience: RequestBody,
        available_time: RequestBody,
        fees: RequestBody,
        department: RequestBody,
        image: MultipartBody.Part? = null,
        certificate: MultipartBody.Part? = null
    ) {
        var disposable: Disposable? = null
        if (image != null) {
            disposable = apiServiceWithGsonFactory.apiHitUpdateProfileWithImageAndCertificate(
                user_id,
                first_name,
                last_name,
                email,
                mobile_number,
                dob,
                gender,
                qualification,
                passing_year,
                institute,
                description,
                experience,
                available_time,
                fees,
                department,
                image,
                certificate!!
            )
                .subscribeOn(_scheduler_io)
                .observeOn(_scheduler_ui)
                .subscribe({ response ->
                    if (response != null) {
                        Log.d("check_response", ": " + Gson().toJson(response))
                        navigator.onSuccessEditProfile(response)
                    } else {
                        Log.d("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
                        navigator.errorInApi(throwable)
                        Log.d("check_response_error", ": " + throwable.message)
                    }
                })
        }
        compositeDisposable.add(disposable!!)
    }

}