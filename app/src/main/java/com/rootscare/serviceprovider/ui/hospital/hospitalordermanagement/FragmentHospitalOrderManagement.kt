package com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalOrderManagementBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter.AdapterHospitalCancelledorderRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter.AdapterHospitalPastOrderRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter.AdapterHospitalUpcomingOrderRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.subfragment.FragmentHospitalOrderDetails

class FragmentHospitalOrderManagement: BaseFragment<FragmentHospitalOrderManagementBinding, FragmentHospitalOrderManagementViewModel>(),
    FragmentHospitalOrderManagementNavigator {
    private var fragmentHospitalOrderManagementBinding: FragmentHospitalOrderManagementBinding? = null
    private var fragmentHospitalOrderManagementViewModel: FragmentHospitalOrderManagementViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_order_management
    override val viewModel: FragmentHospitalOrderManagementViewModel
        get() {
            fragmentHospitalOrderManagementViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalOrderManagementViewModel::class.java!!)
            return fragmentHospitalOrderManagementViewModel as FragmentHospitalOrderManagementViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalOrderManagement {
            val args = Bundle()
            val fragment = FragmentHospitalOrderManagement()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalOrderManagementViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalOrderManagementBinding = viewDataBinding
        setUpViewPastOrderlistingRecyclerview()
        fragmentHospitalOrderManagementBinding?.btnHospitalUpcomingOrder?.setOnClickListener(View.OnClickListener {
            fragmentHospitalOrderManagementBinding?.llHospitalPastOrder?.visibility=View.VISIBLE
            fragmentHospitalOrderManagementBinding?.llHospitalUpcomingOrder?.visibility=View.GONE
            fragmentHospitalOrderManagementBinding?.llHospitalCancelledOrder?.visibility=View.GONE
            setUpViewPastOrderlistingRecyclerview()
        })

        fragmentHospitalOrderManagementBinding?.btnHospitalUpcomingOrder?.setOnClickListener(View.OnClickListener {
            fragmentHospitalOrderManagementBinding?.llHospitalPastOrder?.visibility=View.GONE
            fragmentHospitalOrderManagementBinding?.llHospitalUpcomingOrder?.visibility=View.VISIBLE
            fragmentHospitalOrderManagementBinding?.llHospitalCancelledOrder?.visibility=View.GONE
            setUpViewUpcomingOrderlistingRecyclerview()
        })

        fragmentHospitalOrderManagementBinding?.btnHospitalCancelledOrder?.setOnClickListener(View.OnClickListener {
            fragmentHospitalOrderManagementBinding?.llHospitalPastOrder?.visibility=View.GONE
            fragmentHospitalOrderManagementBinding?.llHospitalUpcomingOrder?.visibility=View.GONE
            fragmentHospitalOrderManagementBinding?.llHospitalCancelledOrder?.visibility=View.VISIBLE
            setUpViewCancelledOrderlistingRecyclerview()
        })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewPastOrderlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalPastOrder != null)
        val recyclerView = fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalPastOrder
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalPastOrderRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentHospitalOrderDetails.newInstance())
            }

        }


    }

    // Set up recycler view for service listing if available
    private fun setUpViewUpcomingOrderlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalUpcomingOrder != null)
        val recyclerView = fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalUpcomingOrder
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalUpcomingOrderRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
     contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentHospitalOrderDetails.newInstance())
            }

        }


    }

    // Set up recycler view for service listing if available
    private fun setUpViewCancelledOrderlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalCancelledOrder != null)
        val recyclerView = fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalCancelledOrder
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalCancelledorderRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentHospitalOrderDetails.newInstance())
            }

        }


    }


}