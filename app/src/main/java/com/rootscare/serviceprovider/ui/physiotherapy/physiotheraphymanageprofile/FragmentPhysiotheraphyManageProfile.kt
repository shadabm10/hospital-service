package com.rootscare.serviceprovider.ui.physiotherapy.physiotheraphymanageprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLabtechnicianManageProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentPhysiotheraphyManageProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile.FragmentLabtechnicianManageProfile
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile.FragmentLabtechnicianManageProfileNavigator
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile.FragmentLabtechnicianManageProfileViewModel

class FragmentPhysiotheraphyManageProfile: BaseFragment<FragmentPhysiotheraphyManageProfileBinding, FragmentPhysiotheraphyManageProfileViewModel>(),
    FragmentPhysiotheraphyManageProfileNavigator {
    private var fragmentPhysiotheraphyManageProfileBinding: FragmentPhysiotheraphyManageProfileBinding? = null
    private var fragmentPhysiotheraphyManageProfileViewModel: FragmentPhysiotheraphyManageProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotheraphy_manage_profile
    override val viewModel: FragmentPhysiotheraphyManageProfileViewModel
        get() {
            fragmentPhysiotheraphyManageProfileViewModel = ViewModelProviders.of(this).get(
                FragmentPhysiotheraphyManageProfileViewModel::class.java!!)
            return fragmentPhysiotheraphyManageProfileViewModel as FragmentPhysiotheraphyManageProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentPhysiotheraphyManageProfile {
            val args = Bundle()
            val fragment = FragmentPhysiotheraphyManageProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotheraphyManageProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotheraphyManageProfileBinding = viewDataBinding
    }
}