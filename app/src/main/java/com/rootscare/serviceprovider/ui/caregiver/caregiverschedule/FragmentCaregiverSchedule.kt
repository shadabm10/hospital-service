package com.rootscare.serviceprovider.ui.caregiver.caregiverschedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverHomeBinding
import com.rootscare.serviceprovider.databinding.FragmentCaregiverScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHome
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHomeNavigator
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHomeViewModel

class FragmentCaregiverSchedule: BaseFragment<FragmentCaregiverScheduleBinding, FragmentCaregiverScheduleViewModel>(),
    FragmentCaregiverScheduleNavigator {
    private var fragmentCaregiverScheduleBinding: FragmentCaregiverScheduleBinding? = null
    private var fragmentCaregiverScheduleViewModel: FragmentCaregiverScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_schedule
    override val viewModel: FragmentCaregiverScheduleViewModel
        get() {
            fragmentCaregiverScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverScheduleViewModel::class.java!!)
            return fragmentCaregiverScheduleViewModel as FragmentCaregiverScheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverSchedule {
            val args = Bundle()
            val fragment = FragmentCaregiverSchedule()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverScheduleViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverScheduleBinding = viewDataBinding
    }


}