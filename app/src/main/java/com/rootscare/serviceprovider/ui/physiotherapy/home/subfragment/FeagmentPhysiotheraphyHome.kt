package com.rootscare.serviceprovider.ui.physiotherapy.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentPhysiotheraphyHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverschedule.FragmentCaregiverSchedule
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHome
import com.rootscare.serviceprovider.ui.labtechnician.home.LabTechnicianHomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile.FragmentLabtechnicianManageProfile
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointment
import com.rootscare.serviceprovider.ui.physiotherapy.appointmentlist.FragmentPhysiotherapyAppointmentList
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.physiotheraphymanageprofile.FragmentPhysiotheraphyManageProfile

class FeagmentPhysiotheraphyHome: BaseFragment<FragmentPhysiotheraphyHomeBinding, FeagmentPhysiotheraphyHomeViewModel>(), FeagmentPhysiotheraphyHomeNavigator  {
    private var fragmentPhysiotheraphyHomeBinding: FragmentPhysiotheraphyHomeBinding? = null
    private var feagmentPhysiotheraphyHomeViewModel: FeagmentPhysiotheraphyHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotheraphy_home
    override val viewModel: FeagmentPhysiotheraphyHomeViewModel
        get() {
            feagmentPhysiotheraphyHomeViewModel = ViewModelProviders.of(this).get(
                FeagmentPhysiotheraphyHomeViewModel::class.java!!)
            return feagmentPhysiotheraphyHomeViewModel as FeagmentPhysiotheraphyHomeViewModel
        }

    companion object {
        fun newInstance(): FeagmentPhysiotheraphyHome {
            val args = Bundle()
            val fragment = FeagmentPhysiotheraphyHome()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feagmentPhysiotheraphyHomeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotheraphyHomeBinding = viewDataBinding

        fragmentPhysiotheraphyHomeBinding?.cardViewManageProfile?.setOnClickListener(View.OnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentPhysiotheraphyManageProfile.newInstance())
        })

        //BELOW SECTION NEED TO CHANGE WHEN API IMPLEMENTATION WILL BE DONE

        fragmentPhysiotheraphyHomeBinding?.cardViewMyAppointment?.setOnClickListener(View.OnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentPhysiotherapyAppointmentList.newInstance())
        })
        //Need To change when actual developement will be done
        fragmentPhysiotheraphyHomeBinding?.cardViewReviewAndRating?.setOnClickListener(View.OnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentReviewAndRating.newInstance())
        })

        fragmentPhysiotheraphyHomeBinding?.cardViewMySchedule?.setOnClickListener(View.OnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverSchedule.newInstance())
        })

    }
}