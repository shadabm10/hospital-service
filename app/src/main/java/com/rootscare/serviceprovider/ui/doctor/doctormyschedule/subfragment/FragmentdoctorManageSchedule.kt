package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.request.doctor.myscheduletimeslot.GetTimeSlotMyScheduleRequest
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.ResultItem
import com.rootscare.data.model.api.response.doctor.myschedule.timeslotlist.MyScheduleTimeSlotResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorManegeScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adapter.AdapterDoctoeViewScheduleRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.FragmentAddDoctorScheduleTime
import com.rootscare.serviceprovider.ui.home.HomeActivity
import java.util.*
import kotlin.collections.ArrayList

class FragmentdoctorManageSchedule : BaseFragment<FragmentDoctorManegeScheduleBinding, FragmentdoctorManageScheduleViewModel>(),
    FragmentdoctorManageScheduleNavigator {


    private var passedItem: ResultItem? = null

    private var contactListAdapter: AdapterDoctoeViewScheduleRecyclerview? = null

    private var fragmentDoctorManegeScheduleBinding: FragmentDoctorManegeScheduleBinding? = null
    private var fragmentdoctorManageScheduleViewModel: FragmentdoctorManageScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_manege_schedule
    override val viewModel: FragmentdoctorManageScheduleViewModel
        get() {
            fragmentdoctorManageScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentdoctorManageScheduleViewModel::class.java
            )
            return fragmentdoctorManageScheduleViewModel as FragmentdoctorManageScheduleViewModel
        }

    companion object {
        fun newInstance(passedItem: ResultItem): FragmentdoctorManageSchedule {
            val args = Bundle()
            args.putSerializable("passedItem", passedItem)
            val fragment = FragmentdoctorManageSchedule()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentdoctorManageScheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorManegeScheduleBinding = viewDataBinding

        passedItem = arguments?.getSerializable("passedItem") as ResultItem

        with(fragmentDoctorManegeScheduleBinding!!) {
            if (passedItem != null) {
                if (passedItem?.name != null) {
                    tvHospitalTitle.text = passedItem?.name
                }
            }
        }


        var dropdownlist = ArrayList<String?>()
        dropdownlist.add("Monday")
        dropdownlist.add("Tuesday")
        dropdownlist.add("Wednesday")
        dropdownlist.add("Thursday")
        dropdownlist.add("Friday")
        dropdownlist.add("Saturday")
        dropdownlist.add("Sunday")
        fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this.activity!!, "Select Day", dropdownlist, object :
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text = text
                    apiHitFetchDaySpecificTimeSlotList(text)
                }

            })
        })
        setUpDoctorMySchedulelistingRecyclerview()

        fragmentDoctorManegeScheduleBinding?.btnAddSlotAndTime?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentAddDoctorScheduleTime.newInstance(passedItem!!)
            )
        })



        fragmentDoctorManegeScheduleBinding?.clearFilterButton?.setOnClickListener {
            apiHitFetchAllTimeSlotList()
        }

    }

    override fun onResume() {
        super.onResume()
        apiHitFetchAllTimeSlotList()
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMySchedulelistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing != null)
        val recyclerView = fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        contactListAdapter = AdapterDoctoeViewScheduleRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(position: Int) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        apiHitRemoveTimeSlot(position)
                    }

                }, "Confirmation", "Are you sure to remove this time slot?")
            }

        }
    }

    private fun apiHitFetchAllTimeSlotList() {
        if (isNetworkConnected) {
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text = ""
            fragmentDoctorManegeScheduleBinding?.clearFilterButton?.visibility = View.GONE
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcommingAppointmentRequest.doctor_id = fragmentdoctorManageScheduleViewModel?.appSharedPref?.loginUserId
            getDoctorUpcommingAppointmentRequest.clinic_id = passedItem?.id
            fragmentdoctorManageScheduleViewModel!!.getAllTimeSlotOfEveryday(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitFetchDaySpecificTimeSlotList(day: String) {
        if (isNetworkConnected) {
            fragmentDoctorManegeScheduleBinding?.clearFilterButton?.visibility = View.VISIBLE
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcommingAppointmentRequest.doctor_id = fragmentdoctorManageScheduleViewModel?.appSharedPref?.loginUserId
            getDoctorUpcommingAppointmentRequest.clinic_id = passedItem?.id
            getDoctorUpcommingAppointmentRequest.day = day.toLowerCase(Locale.ROOT)
            fragmentdoctorManageScheduleViewModel!!.getAllTimeSlotOfDaySpecific(getDoctorUpcommingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitRemoveTimeSlot(position: Int) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcommingAppointmentRequest.id = contactListAdapter?.result!![position].id
//            getDoctorUpcommingAppointmentRequest.day = contactListAdapter?.result!![position].day
            getDoctorUpcommingAppointmentRequest.day = ""
            fragmentdoctorManageScheduleViewModel!!.apiHitRemoveTimeSlot(position, getDoctorUpcommingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSuccessTimeSlotList(response: MyScheduleTimeSlotResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                hideNoData()
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            } else {
                showNoData()
            }
        } else {
            showNoData()
        }
    }

    override fun onSuccessAfterRemoveTimeSlot(position: Int, response: MyScheduleTimeSlotResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (contactListAdapter?.result?.size!! > 0) {
                contactListAdapter?.result?.removeAt(position)
                contactListAdapter?.notifyItemRemoved(position)
                contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.result?.size!!)
                if (contactListAdapter?.result?.size==0){
                    showNoData()
                }else{
                    hideNoData()
                }
            }
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }

    private fun showNoData() {
        with(fragmentDoctorManegeScheduleBinding!!) {
            recyclerViewDoctorViewscheduleListing.visibility = android.view.View.GONE
            tvNoDate.visibility = android.view.View.VISIBLE
        }
    }

    private fun hideNoData() {
        with(fragmentDoctorManegeScheduleBinding!!) {
            recyclerViewDoctorViewscheduleListing.visibility = android.view.View.VISIBLE
            tvNoDate.visibility = android.view.View.GONE
        }
    }

}