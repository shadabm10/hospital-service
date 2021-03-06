package com.rootscare.serviceprovider.ui.nurses.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule.FragmentNursesManageRate
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.FragmentNursesReviewAndRating

class FragmentNurseHome : BaseFragment<FragmentNursesHomeBinding, FragmentNurseHomeViewModel>(),
    FragmentNurseHomeNavigator {
    private var fragmentNursesHomeBinding: FragmentNursesHomeBinding? = null
    private var fragmentNurseHomeViewModel: FragmentNurseHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_home
    override val viewModel: FragmentNurseHomeViewModel
        get() {
            fragmentNurseHomeViewModel = ViewModelProviders.of(this).get(
                FragmentNurseHomeViewModel::class.java!!
            )
            return fragmentNurseHomeViewModel as FragmentNurseHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentNurseHome {
            val args = Bundle()
            val fragment = FragmentNurseHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNurseHomeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesHomeBinding = viewDataBinding
        fragmentNursesHomeBinding?.cardViewNurseshomeManageProfile?.setOnClickListener(View.OnClickListener {
            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesProfile.newInstance()
            )
        })

        fragmentNursesHomeBinding?.cardViewNurseshomeReviewAndRating?.setOnClickListener(View.OnClickListener {
            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesReviewAndRating.newInstance()
            )
        })

        fragmentNursesHomeBinding?.cardViewNurseshomeMyAppointment?.setOnClickListener(View.OnClickListener {
            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesMyAppointment.newInstance()
            )
        })

        fragmentNursesHomeBinding?.cardViewNurseshomeMySchedule?.setOnClickListener(View.OnClickListener {
            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesManageRate.newInstance()
            )
        })
    }


}