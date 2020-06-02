package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesManageRateBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesMySdheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.FragmentNursesMySchedule
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.FragmentNursesMyScheduleNavigator
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.FragmentNursesMyScheduleViewModel

class FragmentNursesManageRate: BaseFragment<FragmentNursesManageRateBinding, FragmentNursesManageRateViewModel>(),
    FragmentNursesManageRateNavigator {
    private var fragmentNursesManageRateBinding: FragmentNursesManageRateBinding? = null
    private var fragmentNursesManageRateViewModel: FragmentNursesManageRateViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_manage_rate
    override val viewModel: FragmentNursesManageRateViewModel
        get() {
            fragmentNursesManageRateViewModel = ViewModelProviders.of(this).get(
                FragmentNursesManageRateViewModel::class.java!!)
            return fragmentNursesManageRateViewModel as FragmentNursesManageRateViewModel
        }
    companion object {
        fun newInstance(): FragmentNursesManageRate {
            val args = Bundle()
            val fragment = FragmentNursesManageRate()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesManageRateViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesManageRateBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })
    }

}