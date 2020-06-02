package com.rootscare.serviceprovider.ui.nurses.nurseprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesHomeBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHome
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHomeNavigator
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHomeViewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterNursesUploadDocument
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile

class FragmentNursesProfile: BaseFragment<FragmentNursesProfileBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {
    private var fragmentNursesProfileBinding: FragmentNursesProfileBinding? = null
    private var fragmentNursesProfileViewModel: FragmentNursesProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_profile
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            fragmentNursesProfileViewModel = ViewModelProviders.of(this).get(
                FragmentNursesProfileViewModel::class.java!!)
            return fragmentNursesProfileViewModel as FragmentNursesProfileViewModel
        }


    companion object {
        fun newInstance(): FragmentNursesProfile {
            val args = Bundle()
            val fragment = FragmentNursesProfile()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesProfileBinding = viewDataBinding
        setUpViewPrescriptionlistingRecyclerview()

        fragmentNursesProfileBinding?.btnNursesEditProfile?.setOnClickListener(View.OnClickListener {
            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesEditProfile.newInstance())
        })
    }


    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareNursesimportentDocument != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareNursesimportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNursesUploadDocument(context!!)
        recyclerView.adapter = contactListAdapter


    }
}