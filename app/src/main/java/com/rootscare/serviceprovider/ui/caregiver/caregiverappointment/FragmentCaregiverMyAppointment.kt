package com.rootscare.serviceprovider.ui.caregiver.caregiverappointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverappointment.adapter.AdapterCaregiverAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.caregiver.caregiverappointment.caregiverappointmentdetails.FragmentCaregiverAppointmentDetails
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity

class FragmentCaregiverMyAppointment : BaseFragment<FragmentCaregiverMyAppointmentBinding, FragmentCaregiverMyAppointmentViewModel>(),
    FragmentCaregiverMyAppointmentNavigator {
    private var fragmentCaregiverMyAppointmentBinding: FragmentCaregiverMyAppointmentBinding? = null
    private var fragmentCaregiverMyAppointmentViewModel: FragmentCaregiverMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_my_appointment
    override val viewModel: FragmentCaregiverMyAppointmentViewModel
        get() {
            fragmentCaregiverMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverMyAppointmentViewModel::class.java!!
            )
            return fragmentCaregiverMyAppointmentViewModel as FragmentCaregiverMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverMyAppointment {
            val args = Bundle()
            val fragment = FragmentCaregiverMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverMyAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverMyAppointmentBinding = viewDataBinding
        setUpNursesMyAppointmentlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpNursesMyAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverMyAppointmentBinding!!.recyclerViewCaregiverMyappointment != null)
        val recyclerView = fragmentCaregiverMyAppointmentBinding!!.recyclerViewCaregiverMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as CaregiverHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentCaregiverAppointmentDetails.newInstance()
                )
            }

        }

    }

}