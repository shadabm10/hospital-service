package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule

import com.rootscare.data.model.api.response.loginresponse.LoginResponse

interface FragmentNursesManageRateNavigator {
    fun onSuccessAfterSavePrice(loginResponse: LoginResponse)

    fun onThrowable(throwable: Throwable)


}