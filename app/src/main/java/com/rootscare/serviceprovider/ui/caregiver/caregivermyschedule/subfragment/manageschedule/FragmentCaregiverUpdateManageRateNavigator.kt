package com.rootscare.serviceprovider.ui.caregiver.caregivermyschedule.subfragment.manageschedule

import com.rootscare.data.model.api.response.loginresponse.LoginResponse

interface FragmentCaregiverUpdateManageRateNavigator {
    fun onSuccessAfterSavePrice(loginResponse: LoginResponse)

    fun onThrowable(throwable: Throwable)


}