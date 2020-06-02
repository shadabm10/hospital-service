package com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalPaymentTransactionBinding
import com.rootscare.serviceprovider.databinding.FragmentHospitalSamplecollectionBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.adapter.AdapterHospitalPaymentTransactionRecyclerView
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollection
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollectionNavigator
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollectionViewModel
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.adapter.AdapterHospitalSampleCollectionRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment.FragmentHospitalSampleCollectionDetails

class FragmentHospitalPaymentTransaction: BaseFragment<FragmentHospitalPaymentTransactionBinding, FragmentHospitalPaymentTransactionViewModel>(),
    FragmentHospitalPaymentTransactionNavigator {
    private var fragmentHospitalPaymentTransactionBinding: FragmentHospitalPaymentTransactionBinding? = null
    private var fragmentHospitalPaymentTransactionViewModel: FragmentHospitalPaymentTransactionViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_payment_transaction
    override val viewModel: FragmentHospitalPaymentTransactionViewModel
        get() {
            fragmentHospitalPaymentTransactionViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalPaymentTransactionViewModel::class.java!!)
            return fragmentHospitalPaymentTransactionViewModel as FragmentHospitalPaymentTransactionViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalPaymentTransaction {
            val args = Bundle()
            val fragment = FragmentHospitalPaymentTransaction()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalPaymentTransactionViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalPaymentTransactionBinding = viewDataBinding
        setUpViewSampleCollectionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSampleCollectionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalPaymentTransactionBinding!!.recyclerViewHospitalPaymenthistory != null)
        val recyclerView = fragmentHospitalPaymentTransactionBinding!!.recyclerViewHospitalPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalPaymentTransactionRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentHospitalSampleCollectionDetails.newInstance())
//            }
//
//        }


    }

}