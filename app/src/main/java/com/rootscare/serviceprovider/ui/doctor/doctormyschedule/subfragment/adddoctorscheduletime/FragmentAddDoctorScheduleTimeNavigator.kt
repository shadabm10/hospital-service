package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime

import com.rootscare.data.model.api.response.CommonResponse

interface FragmentAddDoctorScheduleTimeNavigator {

    fun onSuccessTimeSlotSave(response: CommonResponse)

    fun onThrowable(throwable: Throwable)

}