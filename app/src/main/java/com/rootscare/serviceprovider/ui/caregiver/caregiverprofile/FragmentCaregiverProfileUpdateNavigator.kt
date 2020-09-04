package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile

import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse

interface FragmentCaregiverProfileUpdateNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: com.rootscare.data.model.api.response.caregiver.profileresponse.GetDoctorProfileResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}