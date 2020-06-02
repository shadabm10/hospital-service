package com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageAppointmentBinding
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageDoctorBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter.AdapterHospitalCancelledAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter.AdapterHospitalPastAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter.AdapterHospitalUpcomingAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.FragmentHospitalManageDepartment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.FragmentHospitalManageDepartmentViewModel
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.adapter.AdapterHospitalManageDepartmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctorNavigator
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctorViewModel

class FragmentHospitalManageAppointments: BaseFragment<FragmentHospitalManageAppointmentBinding, FragmentHospitalManageAppointmentsViewModel>(),
    FragmentHospitalManageAppointmentsNavigator {
    private var fragmentHospitalManageAppointmentBinding: FragmentHospitalManageAppointmentBinding? = null
    private var fragmentHospitalManageAppointmentsViewModel: FragmentHospitalManageAppointmentsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_appointment
    override val viewModel: FragmentHospitalManageAppointmentsViewModel
        get() {
            fragmentHospitalManageAppointmentsViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalManageAppointmentsViewModel::class.java!!)
            return fragmentHospitalManageAppointmentsViewModel as FragmentHospitalManageAppointmentsViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalManageAppointments {
            val args = Bundle()
            val fragment = FragmentHospitalManageAppointments()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalManageAppointmentsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageAppointmentBinding = viewDataBinding
        setUpViewPastAppointlistingRecyclerview()
        fragmentHospitalManageAppointmentBinding?.btnHospitalPastAppointment?.setOnClickListener(
            View.OnClickListener {
                fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility=View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility=View.GONE
                fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility=View.GONE
                setUpViewPastAppointlistingRecyclerview()
            })
        fragmentHospitalManageAppointmentBinding?.btnHospitalUpcomingAppointment?.setOnClickListener(
            View.OnClickListener {
                fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility=View.GONE
                fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility=View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility=View.GONE
                setUpViewUpcomingAppontmentlistingRecyclerview()
            })

        fragmentHospitalManageAppointmentBinding?.btnHospitalCancelledAppointment?.setOnClickListener(
            View.OnClickListener {
                fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility=View.GONE
                fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility=View.GONE
                fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility=View.VISIBLE
                setUpViewCancelledAppointmentlistingRecyclerview()
            })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewPastAppointlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalPastAppointment != null)
        val recyclerView = fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalPastAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }

    // Set up recycler view for service listing if available
    private fun setUpViewUpcomingAppontmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalUpcomingAppointment != null)
        val recyclerView = fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalUpcomingAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalUpcomingAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }

    // Set up recycler view for service listing if available
    private fun setUpViewCancelledAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalCancelledAppointment != null)
        val recyclerView = fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalCancelledAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalCancelledAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }

}