package com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorEditProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentDoctorProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfile
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileNavigator
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileViewModel

class FragmentEditDoctorProfile: BaseFragment<FragmentDoctorEditProfileBinding, FragmentEditDoctorProfileViewModel>(),
    FragmentEditDoctorProfileNavigator {
    private var fragmentDoctorEditProfileBinding: FragmentDoctorEditProfileBinding? = null
    private var fragmentEditDoctorProfileViewModel: FragmentEditDoctorProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_edit_profile
    override val viewModel: FragmentEditDoctorProfileViewModel
        get() {
            fragmentEditDoctorProfileViewModel = ViewModelProviders.of(this).get(
                FragmentEditDoctorProfileViewModel::class.java!!)
            return fragmentEditDoctorProfileViewModel as FragmentEditDoctorProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentEditDoctorProfile {
            val args = Bundle()
            val fragment = FragmentEditDoctorProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentEditDoctorProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorEditProfileBinding = viewDataBinding
    }
}