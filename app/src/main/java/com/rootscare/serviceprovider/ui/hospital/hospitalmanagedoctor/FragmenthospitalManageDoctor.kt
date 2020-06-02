package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageDoctorBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.adapter.AdapterHospitalManageDepartmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.adapter.AdapterHospitalManageDoctorRecyclerview


class FragmenthospitalManageDoctor : BaseFragment<FragmentHospitalManageDoctorBinding, FragmenthospitalManageDoctorViewModel>(),
    FragmenthospitalManageDoctorNavigator {
    private var fragmentHospitalManageDoctorBinding: FragmentHospitalManageDoctorBinding? = null
    private var fragmenthospitalManageDoctorViewModel: FragmenthospitalManageDoctorViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_doctor
    override val viewModel: FragmenthospitalManageDoctorViewModel
        get() {
            fragmenthospitalManageDoctorViewModel = ViewModelProviders.of(this).get(
                FragmenthospitalManageDoctorViewModel::class.java!!)
            return fragmenthospitalManageDoctorViewModel as FragmenthospitalManageDoctorViewModel
        }

    companion object {
        fun newInstance(): FragmenthospitalManageDoctor {
            val args = Bundle()
            val fragment = FragmenthospitalManageDoctor()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmenthospitalManageDoctorViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageDoctorBinding = viewDataBinding
        setUpViewReviewAndRatinglistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageDoctorBinding!!.recyclerViewHospitalManageDoctors != null)
        val recyclerView = fragmentHospitalManageDoctorBinding!!.recyclerViewHospitalManageDoctors
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalManageDoctorRecyclerview(context!!)
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