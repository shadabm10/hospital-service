package com.rootscare.serviceprovider.ui.nurses.nurseprofile

import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse

interface FragmentNursesProfileNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}