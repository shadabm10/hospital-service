package com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyAppointmentBinding
import com.rootscare.serviceprovider.databinding.FragmentLabtechnicianMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointmentNavigator
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointmentViewModel
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.adapter.AddapterDoctorMyAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.home.LabTechnicianHomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.adapter.AdapterLabTechnicianMyAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.subfragment.FragmentLabTechnicianMyAppointmentDetails

class FragmentLabtechnicianMyAppointment: BaseFragment<FragmentLabtechnicianMyAppointmentBinding, FragmentLabtechnicianMyAppointmentViewModel>(),
    FragmentLabtechnicianMyAppointmentNavigator {
    private var fragmentLabtechnicianMyAppointmentBinding: FragmentLabtechnicianMyAppointmentBinding? = null
    private var fragmentLabtechnicianMyAppointmentViewModel: FragmentLabtechnicianMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_labtechnician_my_appointment
    override val viewModel: FragmentLabtechnicianMyAppointmentViewModel
        get() {
            fragmentLabtechnicianMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentLabtechnicianMyAppointmentViewModel::class.java!!)
            return fragmentLabtechnicianMyAppointmentViewModel as FragmentLabtechnicianMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentLabtechnicianMyAppointment {
            val args = Bundle()
            val fragment = FragmentLabtechnicianMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLabtechnicianMyAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLabtechnicianMyAppointmentBinding = viewDataBinding
        setUpDoctorMyAppointmentlistingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentLabtechnicianMyAppointmentBinding!!.recyclerViewRootscareLabtechnicianMyappointment != null)
        val recyclerView = fragmentLabtechnicianMyAppointmentBinding!!.recyclerViewRootscareLabtechnicianMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterLabTechnicianMyAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as LabTechnicianHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentLabTechnicianMyAppointmentDetails.newInstance())
            }

        }

    }
}
