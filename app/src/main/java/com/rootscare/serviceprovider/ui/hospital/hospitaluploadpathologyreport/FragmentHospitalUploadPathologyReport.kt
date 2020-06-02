package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalSamplecollectionBinding
import com.rootscare.serviceprovider.databinding.FragmentHospitalUploadPathologyReportBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollection
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollectionNavigator
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollectionViewModel
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.adapter.AdapterHospitalSampleCollectionRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment.FragmentHospitalSampleCollectionDetails
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.adapter.AdapteruploadPathologyReportUserRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment.FragmentPathReportDocumentUpload

class FragmentHospitalUploadPathologyReport: BaseFragment<FragmentHospitalUploadPathologyReportBinding, FragmentHospitalUploadPathologyReportViewModel>(),
    FragmentHospitalUploadPathologyReportNavigator {
    private var fragmentHospitalUploadPathologyReportBinding: FragmentHospitalUploadPathologyReportBinding? = null
    private var fragmentHospitalUploadPathologyReportViewModel: FragmentHospitalUploadPathologyReportViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_upload_pathology_report
    override val viewModel: FragmentHospitalUploadPathologyReportViewModel
        get() {
            fragmentHospitalUploadPathologyReportViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalUploadPathologyReportViewModel::class.java!!)
            return fragmentHospitalUploadPathologyReportViewModel as FragmentHospitalUploadPathologyReportViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalUploadPathologyReport {
            val args = Bundle()
            val fragment = FragmentHospitalUploadPathologyReport()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalUploadPathologyReportViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalUploadPathologyReportBinding = viewDataBinding
        setUpViewSampleCollectionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSampleCollectionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalUploadPathologyReportBinding!!.recyclerViewHospitalUploadpathologytestreportList != null)
        val recyclerView = fragmentHospitalUploadPathologyReportBinding!!.recyclerViewHospitalUploadpathologytestreportList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapteruploadPathologyReportUserRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HospitalHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentPathReportDocumentUpload.newInstance())
            }

        }


    }

}