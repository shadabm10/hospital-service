package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesMyAppointmentBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.adapter.AddapterDoctorMyAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileNavigator
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfileViewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter.AdapterNursesMyAppointmentListrecyclerview
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.FragmentNursesAppointmentDetails

class FragmentNursesMyAppointment: BaseFragment<FragmentNursesMyAppointmentBinding, FragmentNursesMyAppointmentViewModel>(),
    FragmentNursesMyAppointmentNavigator {
    private var fragmentNursesMyAppointmentBinding: FragmentNursesMyAppointmentBinding? = null
    private var fFragmentNursesMyAppointmentViewModel: FragmentNursesMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_my_appointment
    override val viewModel: FragmentNursesMyAppointmentViewModel
        get() {
            fFragmentNursesMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentNursesMyAppointmentViewModel::class.java!!)
            return fFragmentNursesMyAppointmentViewModel as FragmentNursesMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesMyAppointment {
            val args = Bundle()
            val fragment = FragmentNursesMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fFragmentNursesMyAppointmentViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesMyAppointmentBinding = viewDataBinding
        setUpNursesMyAppointmentlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpNursesMyAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesMyAppointmentBinding!!.recyclerViewRootscareNursesMyappointment != null)
        val recyclerView = fragmentNursesMyAppointmentBinding!!.recyclerViewRootscareNursesMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNursesMyAppointmentListrecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentNursesAppointmentDetails.newInstance())
            }

        }

    }

}