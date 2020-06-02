package com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorPaymenthistoryBinding
import com.rootscare.serviceprovider.databinding.FragmentReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.adapter.AdapterDoctorPaymentHistoryRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRatingNavigator
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRatingViewModel

class FragmentDoctorPaymentHistory: BaseFragment<FragmentDoctorPaymenthistoryBinding, FragmentDoctorPaymentHistoryViewModel>(),
    FragmentDoctorPaymentHistoryNavigator {
    private var fragmentDoctorPaymenthistoryBinding: FragmentDoctorPaymenthistoryBinding? = null
    private var fragmentDoctorPaymentHistoryViewModel: FragmentDoctorPaymentHistoryViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_paymenthistory
    override val viewModel: FragmentDoctorPaymentHistoryViewModel
        get() {
            fragmentDoctorPaymentHistoryViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorPaymentHistoryViewModel::class.java!!)
            return fragmentDoctorPaymentHistoryViewModel as FragmentDoctorPaymentHistoryViewModel
        }

    companion object {
        fun newInstance(): FragmentDoctorPaymentHistory {
            val args = Bundle()
            val fragment = FragmentDoctorPaymentHistory()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorPaymentHistoryViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorPaymenthistoryBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })
        setUpPaymentHistorylistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpPaymentHistorylistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorPaymenthistoryBinding!!.recyclerViewDoctorPaymenthistory != null)
        val recyclerView = fragmentDoctorPaymenthistoryBinding!!.recyclerViewDoctorPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctorPaymentHistoryRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }
}