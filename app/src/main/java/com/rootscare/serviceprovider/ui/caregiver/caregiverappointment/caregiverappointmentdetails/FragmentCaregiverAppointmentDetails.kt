package com.rootscare.serviceprovider.ui.caregiver.caregiverappointment.caregiverappointmentdetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverAppointmentDetailsBinding
import com.rootscare.serviceprovider.databinding.FragmentCaregiverHomeBinding
import com.rootscare.serviceprovider.databinding.FragmentCaregiverMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverappointment.FragmentCaregiverMyAppointmentNavigator
import com.rootscare.serviceprovider.ui.caregiver.caregiverappointment.FragmentCaregiverMyAppointmentViewModel
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHome
import com.rootscare.serviceprovider.ui.caregiver.home.subfragment.FragmentCaregiverHomeViewModel

class FragmentCaregiverAppointmentDetails: BaseFragment<FragmentCaregiverAppointmentDetailsBinding, FragmentCaregiverAppointmentDetailsViewModel>(),
    FragmentCaregiverAppointmentDetailsNavigator {
    private var fragmentCaregiverAppointmentDetailsBinding: FragmentCaregiverAppointmentDetailsBinding? = null
    private var fragmentCaregiverAppointmentDetailsViewModel: FragmentCaregiverAppointmentDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_appointment_details
    override val viewModel: FragmentCaregiverAppointmentDetailsViewModel
        get() {
            fragmentCaregiverAppointmentDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverAppointmentDetailsViewModel::class.java!!)
            return fragmentCaregiverAppointmentDetailsViewModel as FragmentCaregiverAppointmentDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentCaregiverAppointmentDetails {
            val args = Bundle()
            val fragment = FragmentCaregiverAppointmentDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverAppointmentDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverAppointmentDetailsBinding = viewDataBinding
    }
}