package com.rootscare.serviceprovider.ui.hospital.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalHomeBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.FragmentHospitalManageAppointments
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.FragmentHospitalManageDepartment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctor
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.FragmentHospitalOrderManagement
import com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.FragmentHospitalPaymentTransaction
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollection
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReport
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHome
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHomeNavigator
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHomeViewModel

class FragmentHospitalHome: BaseFragment<FragmentHospitalHomeBinding, FragmentHospitalHomeViewModel>(),
    FragmentHospitalHomeNavigator {
    private var fragmentHospitalHomeBinding: FragmentHospitalHomeBinding? = null
    private var fragmentHospitalHomeViewModel: FragmentHospitalHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_home
    override val viewModel: FragmentHospitalHomeViewModel
        get() {
            fragmentHospitalHomeViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalHomeViewModel::class.java!!)
            return fragmentHospitalHomeViewModel as FragmentHospitalHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalHome {
            val args = Bundle()
            val fragment = FragmentHospitalHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalHomeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalHomeBinding = viewDataBinding
        fragmentHospitalHomeBinding?.cardViewManageDepartment?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalManageDepartment.newInstance())
       })

        fragmentHospitalHomeBinding?.cardViewManageDoctor?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmenthospitalManageDoctor.newInstance())
        })
        fragmentHospitalHomeBinding?.cardViewManageAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalManageAppointments.newInstance())
        })

        fragmentHospitalHomeBinding?.cardViewManageNotification?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalManageNotification.newInstance())
        })

        fragmentHospitalHomeBinding?.cardViewOrderManagement?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalOrderManagement.newInstance())
        })
        fragmentHospitalHomeBinding?.cardViewSampleCollection?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalSampleCollection.newInstance())
        })

        fragmentHospitalHomeBinding?.cardViewUploadPathologyReport?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalUploadPathologyReport.newInstance())
        })

        fragmentHospitalHomeBinding?.cardViewPaymentTransaction?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalPaymentTransaction.newInstance())
        })

        fragmentHospitalHomeBinding?.cardViewAllBooking?.setOnClickListener(View.OnClickListener {
            (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentHospitalManageAppointments.newInstance())
        })


    }

}