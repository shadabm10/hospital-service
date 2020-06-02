package com.rootscare.serviceprovider.ui.doctor.doctormyappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyAppointmentBinding
import com.rootscare.serviceprovider.databinding.FragmentDoctorProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.adapter.AddapterDoctorMyAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfile
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileNavigator
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileViewModel
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity

class FragmentMyAppointment: BaseFragment<FragmentDoctorMyAppointmentBinding, FragmentMyAppointmentViewModel>(),
    FragmentMyAppointmentNavigator {
    private var fragmentDoctorMyAppointmentBinding: FragmentDoctorMyAppointmentBinding? = null
    private var fragmentMyAppointmentViewModel: FragmentMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_my_appointment
    override val viewModel: FragmentMyAppointmentViewModel
        get() {
            fragmentMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentMyAppointmentViewModel::class.java!!)
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
        setUpDoctorMyAppointmentlistingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorMyAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment != null)
        val recyclerView = fragmentDoctorMyAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AddapterDoctorMyAppointmentListRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorAppointmentDetails.newInstance())
            }

        }

    }
}
