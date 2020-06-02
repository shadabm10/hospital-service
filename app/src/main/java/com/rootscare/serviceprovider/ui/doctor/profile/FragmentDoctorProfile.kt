package com.rootscare.serviceprovider.ui.doctor.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter.AdapterReviewAndRatingRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctorImportantDocumentrecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHome
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHomeNavigator
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHomeVirewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterNursesUploadDocument

class FragmentDoctorProfile: BaseFragment<FragmentDoctorProfileBinding, FragmentDoctorProfileViewModel>(),
    FragmentDoctorProfileNavigator {
    private var fragmentDoctorProfileBinding: FragmentDoctorProfileBinding? = null
    private var fragmentDoctorProfileViewModel: FragmentDoctorProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_profile
    override val viewModel: FragmentDoctorProfileViewModel
        get() {
            fragmentDoctorProfileViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorProfileViewModel::class.java!!)
            return fragmentDoctorProfileViewModel as FragmentDoctorProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentDoctorProfile {
            val args = Bundle()
            val fragment = FragmentDoctorProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorProfileBinding = viewDataBinding
        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentEditDoctorProfile.newInstance())
        })
        setUpViewPrescriptionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorimportentDocument != null)
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorimportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctorImportantDocumentrecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }
}