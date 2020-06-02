package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesAppointmentDetailsBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointmentNavigator
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointmentViewModel

class FragmentNursesAppointmentDetails: BaseFragment<FragmentNursesAppointmentDetailsBinding, FragmentNursesAppointmentDetailsViewModel>(),
    FragmentNursesAppointmentDetailsNavigator {
    private var fragmentNursesAppointmentDetailsBinding: FragmentNursesAppointmentDetailsBinding? = null
    private var fragmentNursesAppointmentDetailsViewModel: FragmentNursesAppointmentDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_appointment_details
    override val viewModel: FragmentNursesAppointmentDetailsViewModel
        get() {
            fragmentNursesAppointmentDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentNursesAppointmentDetailsViewModel::class.java!!)
            return fragmentNursesAppointmentDetailsViewModel as FragmentNursesAppointmentDetailsViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesAppointmentDetails {
            val args = Bundle()
            val fragment = FragmentNursesAppointmentDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesAppointmentDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesAppointmentDetailsBinding = viewDataBinding
    }
}