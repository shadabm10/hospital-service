package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import com.rootscare.data.model.api.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse

interface FragmentDoctorAppointmentDetailsNavigator {


    fun onSuccessDetails(response: AppointmentDetailsResponse)

    fun onThrowable(throwable: Throwable)

}