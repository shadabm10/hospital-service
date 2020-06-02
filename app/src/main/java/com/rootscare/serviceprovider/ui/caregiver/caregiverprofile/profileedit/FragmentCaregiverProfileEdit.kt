package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.profileedit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentCaregiverProfileEditBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.FragmentCaregiverProfile
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.FragmentCaregiverProfileNavigator
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.FragmentCaregiverProfileViewModel
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile

class FragmentCaregiverProfileEdit: BaseFragment<FragmentCaregiverProfileEditBinding, FragmentCaregiverProfileEditViewModel>(),
    FragmentCaregiverProfileEditNavigator {
    private var fragmentCaregiverProfileEditBinding: FragmentCaregiverProfileEditBinding? = null
    private var fragmentCaregiverProfileEditViewModel: FragmentCaregiverProfileEditViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_profile_edit
    override val viewModel: FragmentCaregiverProfileEditViewModel
        get() {
            fragmentCaregiverProfileEditViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverProfileEditViewModel::class.java!!)
            return fragmentCaregiverProfileEditViewModel as FragmentCaregiverProfileEditViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverProfileEdit {
            val args = Bundle()
            val fragment = FragmentCaregiverProfileEdit()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverProfileEditViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverProfileEditBinding = viewDataBinding


    }
}