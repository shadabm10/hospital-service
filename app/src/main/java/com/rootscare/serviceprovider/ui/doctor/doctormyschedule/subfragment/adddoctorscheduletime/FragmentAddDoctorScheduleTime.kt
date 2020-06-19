package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.doctor.myscheduleaddtimeslot.AddTimeSlotRequest
import com.rootscare.data.model.api.request.doctor.myscheduleaddtimeslot.SlotItem
import com.rootscare.data.model.api.request.doctor.myscheduletimeslot.GetTimeSlotMyScheduleRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.ResultItem
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.model.AddDoctorSlotTimeItmModel
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentAddDoctorSlotAndTimeBinding
import com.rootscare.serviceprovider.databinding.FragmentDoctorManegeScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.FragmentdoctorManageSchedule
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.FragmentdoctorManageScheduleNavigator
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.FragmentdoctorManageScheduleViewModel
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.adapter.AdapterAddSlotPerDayRecyclerview
import java.util.*
import kotlin.collections.ArrayList

class FragmentAddDoctorScheduleTime : BaseFragment<FragmentAddDoctorSlotAndTimeBinding, FragmentAddDoctorScheduleTimeViewModel>(),
    FragmentAddDoctorScheduleTimeNavigator {

    private var passedItem: ResultItem? = null

    private var fragmentAddDoctorSlotAndTimeBinding: FragmentAddDoctorSlotAndTimeBinding? = null
    private var fragmentAddDoctorScheduleTimeViewModel: FragmentAddDoctorScheduleTimeViewModel? = null
    var adapterAddSlotPerDayRecyclerview: AdapterAddSlotPerDayRecyclerview? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_doctor_slot_and_time
    override val viewModel: FragmentAddDoctorScheduleTimeViewModel
        get() {
            fragmentAddDoctorScheduleTimeViewModel = ViewModelProviders.of(this).get(
                FragmentAddDoctorScheduleTimeViewModel::class.java
            )
            return fragmentAddDoctorScheduleTimeViewModel as FragmentAddDoctorScheduleTimeViewModel
        }

    companion object {
        fun newInstance(passedItem: ResultItem): FragmentAddDoctorScheduleTime {
            val args = Bundle()
            args.putSerializable("passedItem", passedItem)
            val fragment = FragmentAddDoctorScheduleTime()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAddDoctorScheduleTimeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddDoctorSlotAndTimeBinding = viewDataBinding

        passedItem = arguments?.getSerializable("passedItem") as ResultItem

        with(fragmentAddDoctorSlotAndTimeBinding!!) {
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
        fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(activity!!, "Select Day", dropdownlist, object :
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setText(text)
                }

            })
        })
        setUpAddSlotAndTimeListing()

        fragmentAddDoctorSlotAndTimeBinding?.btnAddMoreSlotAndTime?.setOnClickListener(View.OnClickListener {
            adapterAddSlotPerDayRecyclerview?.addField(AddDoctorSlotTimeItmModel("", "", ""))
        })
        fragmentAddDoctorSlotAndTimeBinding?.btnSubmit?.setOnClickListener(View.OnClickListener {
            apiHitForAddingTimeSlot()
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpAddSlotAndTimeListing() {
        assert(fragmentAddDoctorSlotAndTimeBinding!!.recyclerViewAddSlotAndtime != null)
        val recyclerView = fragmentAddDoctorSlotAndTimeBinding!!.recyclerViewAddSlotAndtime
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapterAddSlotPerDayRecyclerview = AdapterAddSlotPerDayRecyclerview(activity!!)
        recyclerView.adapter = adapterAddSlotPerDayRecyclerview
        adapterAddSlotPerDayRecyclerview?.addField(AddDoctorSlotTimeItmModel("", "", ""))

    }


    private fun apiHitForAddingTimeSlot() {
        if (checkValidation()) {
            var request = AddTimeSlotRequest()
            request.doctorId = fragmentAddDoctorScheduleTimeViewModel?.appSharedPref?.loginUserId
            request.clinicId = passedItem?.id
            request.day = fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.text.toString().toLowerCase(Locale.ROOT)
            if (adapterAddSlotPerDayRecyclerview?.productlistItem?.size!!>0) {
                var slot: LinkedList<SlotItem> = LinkedList()
                for (i in 0 until adapterAddSlotPerDayRecyclerview?.productlistItem?.size!!) {
                    var tempSlot = SlotItem()
                    tempSlot.timeFrom = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].stat_time
                    tempSlot.timeTo = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].end_time
                    slot.add(tempSlot)
                }
                request.slot = slot
            }

            if (isNetworkConnected) {
                baseActivity?.showLoading()
                fragmentAddDoctorScheduleTimeViewModel!!.saveTimeSlot(request)
            } else {
                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkValidation(): Boolean {
        with(fragmentAddDoctorSlotAndTimeBinding!!) {
            if (TextUtils.isEmpty(txtSelectDay.text.toString().trim())) {
                Toast.makeText(activity!!, activity?.getString(R.string.please_select_day), Toast.LENGTH_SHORT).show()
                return false
            }
            var temppBool = true
            for (i in 0 until adapterAddSlotPerDayRecyclerview?.productlistItem?.size!!){
                var startTime = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].stat_time
                var endTime = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].end_time
                if (TextUtils.isEmpty(startTime.trim()) || TextUtils.isEmpty(endTime.trim())){
                    temppBool = false
                    Toast.makeText(activity!!, activity?.getString(R.string.please_add_time_for_all_slot), Toast.LENGTH_LONG).show()
                    break
                }
            }
            return temppBool
        }
    }

    override fun onSuccessTimeSlotSave(response: CommonResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}