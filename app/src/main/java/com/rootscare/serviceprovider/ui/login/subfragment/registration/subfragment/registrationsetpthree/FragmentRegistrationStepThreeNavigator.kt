package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse

interface FragmentRegistrationStepThreeNavigator {
    fun successRegistrationResponse(registrationResponse: RegistrationResponse?)
    fun errorRegistrationResponse(throwable: Throwable?)
}