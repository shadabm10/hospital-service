package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorManegeScheduleBinding
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorconsulting.FragmentDoctorConsulting
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.FragmentDoctorMyschedule
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.FragmentDoctorMyscheduleNavigator
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.FragmentDoctorMyscheduleViewModel
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.adapter.AdapterDoctorMyScheduleRecyclerView
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adapter.AdapterDoctoeViewScheduleRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.FragmentAddDoctorScheduleTime
import com.rootscare.serviceprovider.ui.home.HomeActivity

class FragmentdoctorManageSchedule: BaseFragment<FragmentDoctorManegeScheduleBinding, FragmentdoctorManageScheduleViewModel>(),
    FragmentdoctorManageScheduleNavigator {
    private var fragmentDoctorManegeScheduleBinding: FragmentDoctorManegeScheduleBinding? = null
    private var fragmentdoctorManageScheduleViewModel: FragmentdoctorManageScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_manege_schedule
    override val viewModel: FragmentdoctorManageScheduleViewModel
        get() {
            fragmentdoctorManageScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentdoctorManageScheduleViewModel::class.java!!)
            return fragmentdoctorManageScheduleViewModel as FragmentdoctorManageScheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentdoctorManageSchedule {
            val args = Bundle()
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


        var dropdownlist= ArrayList<String?>()
        dropdownlist.add("Monday")
        dropdownlist.add("Tuesday")
        dropdownlist.add("Wednesday")
        dropdownlist.add("Thursday")
        dropdownlist.add("Friday")
        dropdownlist.add("Saturday")
        dropdownlist.add("Sunday")
        fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Select Day",dropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.setText(text)
                }

            })
        })
        setUpDoctorMySchedulelistingRecyclerview()

        fragmentDoctorManegeScheduleBinding?.btnAddSlotAndTime?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentAddDoctorScheduleTime.newInstance())
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMySchedulelistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing != null)
        val recyclerView = fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctoeViewScheduleRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialogForAddReason(context!!,object:
                    DialogClickCallback {
                    override fun onConfirm() {

                    }

                    override fun onDismiss() {
                    }
                })
            }

        }

    }
}