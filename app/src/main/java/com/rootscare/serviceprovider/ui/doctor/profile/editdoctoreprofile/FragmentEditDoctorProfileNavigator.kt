package com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile

import com.rootscare.data.model.api.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse

interface FragmentEditDoctorProfileNavigator {

    fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?)
    fun onSuccessEditProfile(response: RegistrationResponse)
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?)
    fun errorInApi(throwable: Throwable?)

}