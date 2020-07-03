package com.rootscare.serviceprovider.ui.doctor.doctormyappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorAppointmentListBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment.FragmentPastAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment.FragmentRequestedAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.FragmentTodaysAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointment
import com.rootscare.serviceprovider.ui.home.HomeActivity

class FragmentMyAppointment: BaseFragment<FragmentDoctorAppointmentListBinding, FragmentMyAppointmentViewModel>(),
    FragmentMyAppointmentNavigator {
    private var fragmentDoctorMyAppointmentBinding: FragmentDoctorAppointmentListBinding? = null
    private var fragmentMyAppointmentViewModel: FragmentMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_appointment_list
    override val viewModel: FragmentMyAppointmentViewModel
        get() {
            fragmentMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentMyAppointmentViewModel::class.java)
            return fragmentMyAppointmentViewModel as FragmentMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentMyAppointment {
            val args = Bundle()
            val fragment = FragmentMyAppointment()
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
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentUpcommingAppointment.newInstance())
        })

        fragmentDoctorMyAppointmentBinding?.btnDoctorTodaysAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentTodaysAppointment.newInstance())
        })

        fragmentDoctorMyAppointmentBinding?.btnDoctorRequestedAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentRequestedAppointment.newInstance())
        })
        fragmentDoctorMyAppointmentBinding?.btnDoctorPastAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentPastAppointment.newInstance())
        })
    }
//    // Set up recycler view for service listing if available
//    private fun setUpDoctorMyAppointmentlistingRecyclerview() {
////        trainerList: ArrayList<TrainerListItem?>?
//        assert(fragmentDoctorMyAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment != null)
//        val recyclerView = fragmentDoctorMyAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment
//        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
//        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.setHasFixedSize(true)
////        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
////        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
//        val contactListAdapter = AddapterDoctorMyAppointmentListRecyclerview(context!!)
//        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorAppointmentDetails.newInstance())
//            }
//
//        }
//
//    }
}
