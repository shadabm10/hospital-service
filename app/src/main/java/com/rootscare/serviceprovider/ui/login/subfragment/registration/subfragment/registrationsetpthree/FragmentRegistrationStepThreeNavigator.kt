package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import com.rootscare.data.model.api.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse

interface FragmentRegistrationStepThreeNavigator {
    fun successRegistrationResponse(registrationResponse: RegistrationResponse?)
    fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?)
    fun errorRegistrationResponse(throwable: Throwable?)
}