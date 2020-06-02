package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
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

class FragmentAddDoctorScheduleTime: BaseFragment<FragmentAddDoctorSlotAndTimeBinding, FragmentAddDoctorScheduleTimeViewModel>(),
    FragmentAddDoctorScheduleTimeNavigator {
    private var fragmentAddDoctorSlotAndTimeBinding: FragmentAddDoctorSlotAndTimeBinding? = null
    private var fragmentAddDoctorScheduleTimeViewModel: FragmentAddDoctorScheduleTimeViewModel? = null
    var adapterAddSlotPerDayRecyclerview: AdapterAddSlotPerDayRecyclerview?=null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_doctor_slot_and_time
    override val viewModel: FragmentAddDoctorScheduleTimeViewModel
        get() {
            fragmentAddDoctorScheduleTimeViewModel = ViewModelProviders.of(this).get(
                FragmentAddDoctorScheduleTimeViewModel::class.java!!)
            return fragmentAddDoctorScheduleTimeViewModel as FragmentAddDoctorScheduleTimeViewModel
        }

    companion object {
        fun newInstance(): FragmentAddDoctorScheduleTime {
            val args = Bundle()
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


        var dropdownlist= ArrayList<String?>()
        dropdownlist.add("Monday")
        dropdownlist.add("Tuesday")
        dropdownlist.add("Wednesday")
        dropdownlist.add("Thursday")
        dropdownlist.add("Friday")
        dropdownlist.add("Saturday")
        dropdownlist.add("Sunday")
        fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Select Day",dropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setText(text)
                }

            })
        })
        setUpAddSlotAndTimeListing()

        fragmentAddDoctorSlotAndTimeBinding?.btnAddMoreSlotAndTime?.setOnClickListener(View.OnClickListener {
            adapterAddSlotPerDayRecyclerview?.addField(AddDoctorSlotTimeItmModel("","",""))
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpAddSlotAndTimeListing() {
        assert(fragmentAddDoctorSlotAndTimeBinding!!.recyclerViewAddSlotAndtime != null)
        val recyclerView = fragmentAddDoctorSlotAndTimeBinding!!.recyclerViewAddSlotAndtime
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapterAddSlotPerDayRecyclerview = AdapterAddSlotPerDayRecyclerview(context!!)
        recyclerView.adapter = adapterAddSlotPerDayRecyclerview
        adapterAddSlotPerDayRecyclerview?.addField(AddDoctorSlotTimeItmModel("","",""))

    }
}