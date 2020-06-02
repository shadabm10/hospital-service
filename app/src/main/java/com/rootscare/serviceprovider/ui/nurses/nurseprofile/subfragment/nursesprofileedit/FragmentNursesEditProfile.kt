package com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesEditProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileNavigator
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileViewModel

class FragmentNursesEditProfile: BaseFragment<FragmentNursesEditProfileBinding, FragmentNursesEditProfileViewModel>(),
    FragmentNursesEditProfileNavigator {
    private var fragmentNursesEditProfileBinding: FragmentNursesEditProfileBinding? = null
    private var fragmentNursesEditProfileViewModel: FragmentNursesEditProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_edit_profile
    override val viewModel: FragmentNursesEditProfileViewModel
        get() {
            fragmentNursesEditProfileViewModel = ViewModelProviders.of(this).get(
                FragmentNursesEditProfileViewModel::class.java!!)
            return fragmentNursesEditProfileViewModel as FragmentNursesEditProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesEditProfile {
            val args = Bundle()
            val fragment = FragmentNursesEditProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesEditProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesEditProfileBinding = viewDataBinding
    }



}