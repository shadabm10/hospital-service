package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment

import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse

interface FragmentTodaysAppointmentNavigator {
    fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?)
    fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?)
    fun onSuccessMarkAsComplete(response:CommonResponse, position:Int)
}