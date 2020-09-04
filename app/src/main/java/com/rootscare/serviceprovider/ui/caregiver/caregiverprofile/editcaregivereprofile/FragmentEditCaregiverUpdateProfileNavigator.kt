package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.editcaregivereprofile

import com.rootscare.data.model.api.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse

interface FragmentEditCaregiverUpdateProfileNavigator {

    fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?)
    fun onSuccessEditProfile(response: LoginResponse)
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: com.rootscare.data.model.api.response.caregiver.profileresponse.GetDoctorProfileResponse?)
    fun errorInApi(throwable: Throwable?)

}