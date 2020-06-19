package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment

import com.rootscare.data.model.api.response.doctor.myschedule.timeslotlist.MyScheduleTimeSlotResponse

interface FragmentdoctorManageScheduleNavigator {

    fun onSuccessTimeSlotList(response: MyScheduleTimeSlotResponse)

    fun onSuccessAfterRemoveTimeSlot(position:Int,response: MyScheduleTimeSlotResponse)

    fun onThrowable(throwable: Throwable)


}