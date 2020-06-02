package com.rootscare.serviceprovider.ui.caregiver.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregigerreviewandrating.FragmenntCaregiverReviewAndRating
import com.rootscare.serviceprovider.ui.caregiver.caregiverappointment.FragmentCaregiverMyAppointment
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.FragmentCaregiverProfile
import com.rootscare.serviceprovider.ui.caregiver.caregiverschedule.FragmentCaregiverSchedule
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity

class FragmentCaregiverHome: BaseFragment<FragmentCaregiverHomeBinding, FragmentCaregiverHomeViewModel>(),
    FragmentCaregiverHomeNavigator {
    private var fragmentCaregiverHomeBinding: FragmentCaregiverHomeBinding? = null
    private var fragmentCaregiverHomeViewModel: FragmentCaregiverHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_home
    override val viewModel: FragmentCaregiverHomeViewModel
        get() {
            fragmentCaregiverHomeViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverHomeViewModel::class.java!!)
            return fragmentCaregiverHomeViewModel as FragmentCaregiverHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverHome {
            val args = Bundle()
            val fragment = FragmentCaregiverHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverHomeViewModel!!.navigator = this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverHomeBinding = viewDataBinding
        fragmentCaregiverHomeBinding?.cardViewCaregiverhomeManageProfile?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverProfile.newInstance())
        })

        fragmentCaregiverHomeBinding?.cardViewCaregiverhomeReviewAndRating?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmenntCaregiverReviewAndRating.newInstance())
        })

        fragmentCaregiverHomeBinding?.cardViewCaregiverhomeMyAppointment?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverMyAppointment.newInstance())
        })

        fragmentCaregiverHomeBinding?.cardViewCaregiverhomeMySchedule?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverSchedule.newInstance())
        })
    }

}