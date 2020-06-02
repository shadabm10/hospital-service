package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageDepartmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.adapter.AdapterHospitalManageDepartmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.subfragment.FragmentHospitalHome
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.adapter.AdapterNursesReviewAndRating

class FragmentHospitalManageDepartment: BaseFragment<FragmentHospitalManageDepartmentBinding, FragmentHospitalManageDepartmentViewModel>(),
    FragmentHospitalManageDepartmentNavigator {
    private var fragmentHospitalManageDepartmentBinding: FragmentHospitalManageDepartmentBinding? = null
    private var fragmentHospitalManageDepartmentViewModel: FragmentHospitalManageDepartmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_department
    override val viewModel: FragmentHospitalManageDepartmentViewModel
        get() {
            fragmentHospitalManageDepartmentViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalManageDepartmentViewModel::class.java!!)
            return fragmentHospitalManageDepartmentViewModel as FragmentHospitalManageDepartmentViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalManageDepartment {
            val args = Bundle()
            val fragment = FragmentHospitalManageDepartment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalManageDepartmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageDepartmentBinding = viewDataBinding
        setUpViewReviewAndRatinglistingRecyclerview()

        fragmentHospitalManageDepartmentBinding?.txtAddHpospitalSpeciality?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForAddSpeciality(context!!,object:
                DialogClickCallback {
                override fun onConfirm() {

                }

                override fun onDismiss() {
                }
            })

        })


    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageDepartmentBinding!!.recyclerViewHospitalManageDepartmentSpecialities != null)
        val recyclerView = fragmentHospitalManageDepartmentBinding!!.recyclerViewHospitalManageDepartmentSpecialities
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalManageDepartmentRecyclerview(context!!)
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