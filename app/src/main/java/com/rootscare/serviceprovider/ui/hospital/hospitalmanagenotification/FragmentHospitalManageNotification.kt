package com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageDoctorBinding
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageNotificationBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctor
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctorNavigator
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctorViewModel
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.adapter.AdapterHospitalManageDoctorRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.adapter.AdapterHospitalManageNotificationRecyclerview

class FragmentHospitalManageNotification: BaseFragment<FragmentHospitalManageNotificationBinding, FragmentHospitalManageNotificationViewModel>(),
    FragmentHospitalManageNotificationNavigator {
    private var fragmentHospitalManageNotificationBinding: FragmentHospitalManageNotificationBinding? = null
    private var fragmentHospitalManageNotificationViewModel: FragmentHospitalManageNotificationViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_notification
    override val viewModel: FragmentHospitalManageNotificationViewModel
        get() {
            fragmentHospitalManageNotificationViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalManageNotificationViewModel::class.java!!)
            return fragmentHospitalManageNotificationViewModel as FragmentHospitalManageNotificationViewModel
        }
    companion object {
        fun newInstance(): FragmentHospitalManageNotification {
            val args = Bundle()
            val fragment = FragmentHospitalManageNotification()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalManageNotificationViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageNotificationBinding = viewDataBinding
        setUpViewReviewAndRatinglistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageNotificationBinding!!.recyclerViewHospitalManageNotification != null)
        val recyclerView = fragmentHospitalManageNotificationBinding!!.recyclerViewHospitalManageNotification
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalManageNotificationRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }

}