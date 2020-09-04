package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.newappointment

import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentRequestedAppointmentForNurseNavigator {
    fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}