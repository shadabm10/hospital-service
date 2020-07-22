package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse

interface FragmentDoctorAppointmentDetailsNavigator {


    fun onSuccessDetails(response: AppointmentDetailsResponse)

    fun onThrowable(throwable: Throwable)

    fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?
    )
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)

    fun onSuccessMarkAsComplete(response: CommonResponse)

    fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?)

    fun onSuccessUploadPrescription(response:CommonResponse)
}