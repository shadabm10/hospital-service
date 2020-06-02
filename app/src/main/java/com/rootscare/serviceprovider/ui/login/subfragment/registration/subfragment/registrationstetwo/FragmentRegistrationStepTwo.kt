package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationBinding
import com.rootscare.serviceprovider.databinding.FragmentRegistrationStepTwoBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistrationNavigator
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistrationviewModel

class FragmentRegistrationStepTwo : BaseFragment<FragmentRegistrationStepTwoBinding, FragmentRegistrationStepTwoViewModel>(),
    FragmentRegistrationStepTwoNavigator {
    private var fragmentRegistrationStepTwoBinding: FragmentRegistrationStepTwoBinding? = null
    private var fragmentRegistrationStepTwoViewModel: FragmentRegistrationStepTwoViewModel? = null
    override val bindingVariable: Int
        get() =BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_step_two
    override val viewModel: FragmentRegistrationStepTwoViewModel
        get() {
            fragmentRegistrationStepTwoViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepTwoViewModel::class.java!!)
            return fragmentRegistrationStepTwoViewModel as FragmentRegistrationStepTwoViewModel
        }
    companion object {
        val TAG = FragmentRegistrationStepTwo::class.java.simpleName
        fun newInstance(): FragmentRegistrationStepTwo {
            val args = Bundle()
            val fragment = FragmentRegistrationStepTwo()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationStepTwoViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationStepTwoBinding = viewDataBinding
    }

}