package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentSampleCpllectionDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentHospitalSampleCollectionDetails : BaseFragment<FragmentSampleCpllectionDetailsBinding, FragmentHospitalSampleCollectionDetailsViewModel>(),
    FragmentHospitalSampleCollectionDetailsnavigator{
    private var fragmentSampleCpllectionDetailsBinding: FragmentSampleCpllectionDetailsBinding? = null
    private var fragmentHospitalSampleCollectionDetailsViewModel: FragmentHospitalSampleCollectionDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_sample_cpllection_details
    override val viewModel: FragmentHospitalSampleCollectionDetailsViewModel
        get() {
            fragmentHospitalSampleCollectionDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalSampleCollectionDetailsViewModel::class.java!!)
            return fragmentHospitalSampleCollectionDetailsViewModel as FragmentHospitalSampleCollectionDetailsViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalSampleCollectionDetails {
            val args = Bundle()
            val fragment = FragmentHospitalSampleCollectionDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalSampleCollectionDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSampleCpllectionDetailsBinding = viewDataBinding
    }

}