package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverHomeBinding
import com.rootscare.serviceprovider.databinding.FragmentCaregiverProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.adapter.AdapterCaregiverFeesListRecyclerview
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.adapter.AdapterCaregiverUploadImportantDocument
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.profileedit.FragmentCaregiverProfileEdit
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHome
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHomeNavigator
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHomeViewModel
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterNursesUploadDocument
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile

class FragmentCaregiverProfile: BaseFragment<FragmentCaregiverProfileBinding, FragmentCaregiverProfileViewModel>(),
    FragmentCaregiverProfileNavigator {
    private var fragmentCaregiverProfileBinding: FragmentCaregiverProfileBinding? = null
    private var fragmentCaregiverProfileViewModel: FragmentCaregiverProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_profile
    override val viewModel: FragmentCaregiverProfileViewModel
        get() {
            fragmentCaregiverProfileViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverProfileViewModel::class.java!!)
            return fragmentCaregiverProfileViewModel as FragmentCaregiverProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverProfile {
            val args = Bundle()
            val fragment = FragmentCaregiverProfile()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverProfileBinding = viewDataBinding
//        setUpViewSeeAllNursesCategorieslistingRecyclerview()
        setUpViewNursesFeeslistingRecyclerview()
        setUpViewPrescriptionlistingRecyclerview()
        fragmentCaregiverProfileBinding?.btnCaregiverEditProfile?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverProfileEdit.newInstance())
        })



    }


    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverProfileBinding!!.recyclerViewCaregiverFeesListing != null)
        val recyclerView = fragmentCaregiverProfileBinding!!.recyclerViewCaregiverFeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverFeesListRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverProfileBinding!!.recyclerViewCaregiverImportentDocument != null)
        val recyclerView = fragmentCaregiverProfileBinding!!.recyclerViewCaregiverImportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverUploadImportantDocument(context!!)
        recyclerView.adapter = contactListAdapter


    }

}