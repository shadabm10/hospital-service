package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment

import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse
import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse

interface FragmentUpcommingAppointmentNavigator {
    fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?)
    fun errorDoctorUpcomingAppointmentResponse(throwable: Throwable?)
    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    )
}