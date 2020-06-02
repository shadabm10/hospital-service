package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalUploadPathologyReportBinding
import com.rootscare.serviceprovider.databinding.FragmentPatReportDocumentUploadBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReport
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReportNavigator
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReportViewModel
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.adapter.AdapteruploadPathologyReportUserRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment.adapter.AdapterUploadDocumentReportRecyclerview

class FragmentPathReportDocumentUpload: BaseFragment<FragmentPatReportDocumentUploadBinding, FragmentPathReportDocumentUploadViewModel>(),
    FragmentPathReportDocumentUploadNavigator {
    private var fragmentPatReportDocumentUploadBinding: FragmentPatReportDocumentUploadBinding? = null
    private var fragmentPathReportDocumentUploadViewModel: FragmentPathReportDocumentUploadViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_pat_report_document_upload
    override val viewModel: FragmentPathReportDocumentUploadViewModel
        get() {
            fragmentPathReportDocumentUploadViewModel = ViewModelProviders.of(this).get(
                FragmentPathReportDocumentUploadViewModel::class.java!!)
            return fragmentPathReportDocumentUploadViewModel as FragmentPathReportDocumentUploadViewModel
        }

    companion object {
        fun newInstance(): FragmentPathReportDocumentUpload {
            val args = Bundle()
            val fragment = FragmentPathReportDocumentUpload()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPathReportDocumentUploadViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPatReportDocumentUploadBinding = viewDataBinding
        setUpViewSampleCollectionlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSampleCollectionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentPatReportDocumentUploadBinding!!.recyclerViewRootscareReviewandrating != null)
        val recyclerView = fragmentPatReportDocumentUploadBinding!!.recyclerViewRootscareReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterUploadDocumentReportRecyclerview(context!!)
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