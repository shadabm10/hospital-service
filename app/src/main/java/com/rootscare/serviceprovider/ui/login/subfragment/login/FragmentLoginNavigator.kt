package com.rootscare.serviceprovider.ui.login.subfragment.login

import com.rootscare.data.model.api.response.loginresponse.LoginResponse

interface FragmentLoginNavigator {
    fun successLoginResponse(loginResponse: LoginResponse?)
    fun errorLoginResponse(throwable: Throwable?)
}