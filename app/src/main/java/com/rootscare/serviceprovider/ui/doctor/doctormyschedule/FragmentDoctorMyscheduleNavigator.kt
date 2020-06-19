package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.MyScheduleHospitalResponse

interface FragmentDoctorMyscheduleNavigator {

    fun onSuccessHospitalList(response:MyScheduleHospitalResponse)
    fun onnSuccessAddHospital(response: CommonResponse)
    fun onThrowable(throwable: Throwable)
}