package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalSamplecollectionBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.adapter.AdapterHospitalSampleCollectionRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment.FragmentHospitalSampleCollectionDetails

class FragmentHospitalSampleCollection: BaseFragment<FragmentHospitalSamplecollectionBinding, FragmentHospitalSampleCollectionViewModel>(),
    FragmentHospitalSampleCollectionNavigator {
    private var fragmentHospitalSamplecollectionBinding: FragmentHospitalSamplecollectionBinding? = null
    private var fragmentHospitalSampleCollectionViewModel: FragmentHospitalSampleCollectionViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_samplecollection
    override val viewModel: FragmentHospitalSampleCollectionViewModel
        get() {
            fragmentHospitalSampleCollectionViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalSampleCollectionViewModel::class.java!!)
            return fragmentHospitalSampleCollectionViewModel as FragmentHospitalSampleCollectionViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalSampleCollection {
            val args = Bundle()
            val fragment = FragmentHospitalSampleCollection()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalSampleCollectionViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalSamplecollectionBinding = viewDataBinding
        setUpViewSampleCollectionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSampleCollectionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalSamplecollectionBinding!!.recyclerViewHospitalSamplecollectionList != null)
        val recyclerView = fragmentHospitalSamplecollectionBinding!!.recyclerViewHospitalSamplecollectionList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalSampleCollectionRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentHospitalSampleCollectionDetails.newInstance())
            }

        }


    }

}