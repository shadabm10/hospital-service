package com.rootscare.serviceprovider.ui.nurses.nursespaymenthistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.response.doctor.payment.PaymentResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorPaymenthistoryBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesPaymentHistoryBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.FragmentDoctorPaymentHistory
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.FragmentDoctorPaymentHistoryNavigator
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.FragmentDoctorPaymentHistoryViewModel
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.adapter.AdapterDoctorPaymentHistoryRecyclerview
import com.rootscare.serviceprovider.ui.nurses.nursespaymenthistory.adapter.AdapterNursePaymentHistoryRecyclerview

class FragmentNursesPaymentHistory : BaseFragment<FragmentNursesPaymentHistoryBinding, FragmentNursesPaymentHistoryViewModel>(),
    FragmentNursesPaymentHistoryNavigator {

    private var contactListAdapter: AdapterNursePaymentHistoryRecyclerview? = null

    private var fragmentNursesPaymentHistoryBinding: FragmentNursesPaymentHistoryBinding? = null
    private var fragmentNursesPaymentHistoryViewModel: FragmentNursesPaymentHistoryViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_payment_history
    override val viewModel: FragmentNursesPaymentHistoryViewModel
        get() {
            fragmentNursesPaymentHistoryViewModel = ViewModelProviders.of(this).get(
                FragmentNursesPaymentHistoryViewModel::class.java!!
            )
            return fragmentNursesPaymentHistoryViewModel as FragmentNursesPaymentHistoryViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesPaymentHistory {
            val args = Bundle()
            val fragment = FragmentNursesPaymentHistory()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesPaymentHistoryViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesPaymentHistoryBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })
        setUpPaymentHistorylistingRecyclerview()
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId = fragmentNursesPaymentHistoryViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId = "11"    // for testing
            fragmentNursesPaymentHistoryViewModel!!.getPaymentHistoryFromApi(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpPaymentHistorylistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesPaymentHistoryBinding!!.recyclerViewNursesPaymenthistory != null)
        val recyclerView = fragmentNursesPaymentHistoryBinding!!.recyclerViewNursesPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterNursePaymentHistoryRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
    }

    override fun onSuccessPaymentList(response: PaymentResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentNursesPaymentHistoryBinding?.recyclerViewNursesPaymenthistory?.visibility = View.VISIBLE
                fragmentNursesPaymentHistoryBinding?.tvNoDate?.visibility = View.GONE
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            }else{
                fragmentNursesPaymentHistoryBinding?.recyclerViewNursesPaymenthistory?.visibility = View.INVISIBLE
                fragmentNursesPaymentHistoryBinding?.tvNoDate?.visibility = View.VISIBLE
            }
        }else{
            fragmentNursesPaymentHistoryBinding?.recyclerViewNursesPaymenthistory?.visibility = View.INVISIBLE
            fragmentNursesPaymentHistoryBinding?.tvNoDate?.visibility = View.VISIBLE
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}