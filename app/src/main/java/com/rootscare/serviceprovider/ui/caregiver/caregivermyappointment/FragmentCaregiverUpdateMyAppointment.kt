package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.newappointment.FragmentRequestedAppointmentForCaregiver
import com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.pastappointment.FragmentPastAppointmentForCaregiver
import com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.todayappointment.FragmentTodaysAppointmentForCaregiver
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointmentNavigator
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointmentViewModel
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.newappointment.FragmentRequestedAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.pastappointment.FragmentPastAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment.FragmentTodaysAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointmentForNurse

class FragmentCaregiverUpdateMyAppointment : BaseFragment<FragmentNursesMyAppointmentBinding, FragmentNursesMyAppointmentViewModel>(),
    FragmentNursesMyAppointmentNavigator {
    private var fragmentDoctorMyAppointmentBinding: FragmentNursesMyAppointmentBinding? = null
    private var fragmentMyAppointmentViewModel: FragmentNursesMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_my_appointment
    override val viewModel: FragmentNursesMyAppointmentViewModel
        get() {
            fragmentMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentNursesMyAppointmentViewModel::class.java
            )
            return fragmentMyAppointmentViewModel as FragmentNursesMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverUpdateMyAppointment {
            val args = Bundle()
            val fragment = FragmentCaregiverUpdateMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMyAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorMyAppointmentBinding = viewDataBinding
        fragmentDoctorMyAppointmentBinding?.btnDoctorUpcommingAppointment?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentRequestedAppointmentForCaregiver.newInstance()
            )
        })

        fragmentDoctorMyAppointmentBinding?.btnDoctorTodaysAppointment?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentTodaysAppointmentForCaregiver.newInstance()
            )
        })

        fragmentDoctorMyAppointmentBinding?.btnDoctorRequestedAppointment?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentRequestedAppointmentForCaregiver.newInstance()
            )
        })
        fragmentDoctorMyAppointmentBinding?.btnDoctorPastAppointment?.setOnClickListener(View.OnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentPastAppointmentForCaregiver.newInstance()
            )
        })
    }
}