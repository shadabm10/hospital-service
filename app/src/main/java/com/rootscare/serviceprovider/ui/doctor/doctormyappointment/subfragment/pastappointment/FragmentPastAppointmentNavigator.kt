package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment

import com.rootscare.data.model.api.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentPastAppointmentNavigator {

    fun responseListPastAppointment(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}