package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.pastappointment

import com.rootscare.data.model.api.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentPastAppointmentForNurseNavigator {

    fun responseListPastAppointment(response: ResponsePastAppointment)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}