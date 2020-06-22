package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment

import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentRequestedAppointmentNavigator {
    fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}