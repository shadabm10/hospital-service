package com.rootscare.serviceprovider.ui.caregiver.caregiverpaymenthistory

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverPaymenthistoryBinding
import com.rootscare.serviceprovider.databinding.FragmentCaregiverProfileEditBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesPaymentHistoryBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverpaymenthistory.adapter.AdapterCaregiverPaymentHistoryRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.adapter.AdapterDoctorPaymentHistoryRecyclerview
import com.rootscare.serviceprovider.ui.nurses.nursespaymenthistory.FragmentNursesPaymentHistory
import com.rootscare.serviceprovider.ui.nurses.nursespaymenthistory.FragmentNursesPaymentHistoryViewModel


class FragmentCaregiverPaymentHistory : BaseFragment<FragmentCaregiverPaymenthistoryBinding, FragmentCaregiverPaymentHistoryViewModel>(),
    FragmentCaregiverPaymentHistoryNavigator {
    private var fragmentCaregiverPaymenthistoryBinding: FragmentCaregiverPaymenthistoryBinding? = null
    private var fragmentCaregiverPaymentHistoryViewModel: FragmentCaregiverPaymentHistoryViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_paymenthistory
    override val viewModel: FragmentCaregiverPaymentHistoryViewModel
        get() {
            fragmentCaregiverPaymentHistoryViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverPaymentHistoryViewModel::class.java!!)
            return fragmentCaregiverPaymentHistoryViewModel as FragmentCaregiverPaymentHistoryViewModel
        }
    companion object {
        fun newInstance(): FragmentCaregiverPaymentHistory {
            val args = Bundle()
            val fragment = FragmentCaregiverPaymentHistory()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverPaymentHistoryViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverPaymenthistoryBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })
        setUpPaymentHistorylistingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpPaymentHistorylistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverPaymenthistoryBinding!!.recyclerViewCaregiverPaymenthistory != null)
        val recyclerView = fragmentCaregiverPaymenthistoryBinding!!.recyclerViewCaregiverPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverPaymentHistoryRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }
}