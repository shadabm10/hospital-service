package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.ResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorRequestedAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment.adapter.AdapterRequestedAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import java.util.ArrayList

class FragmentRequestedAppointment: BaseFragment<FragmentDoctorRequestedAppointmentBinding, FragmentRequestedAppointmentViewModel>(),
    FragmentRequestedAppointmentNavigator {
    private var lastPosition:Int? = null
    private var contactListAdapter:AdapterRequestedAppointmentListRecyclerview?=null
    private var booleanIsAcceptedClick = true

    private var fragmentDoctorRequestedAppointmentBinding: FragmentDoctorRequestedAppointmentBinding? = null
    private var fragmentRequestedAppointmentViewModel: FragmentRequestedAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_requested_appointment
    override val viewModel: FragmentRequestedAppointmentViewModel
        get() {
            fragmentRequestedAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentRequestedAppointmentViewModel::class.java)
            return fragmentRequestedAppointmentViewModel as FragmentRequestedAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentRequestedAppointment {
            val args = Bundle()
            val fragment = FragmentRequestedAppointment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRequestedAppointmentViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorRequestedAppointmentBinding = viewDataBinding
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest= GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId=fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentRequestedAppointmentViewModel!!.apidoctorappointmentrequestlist(getDoctorUpcommingAppointmentRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(requestedappointmentList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorRequestedAppointment != null)
        val recyclerView = fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorRequestedAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterRequestedAppointmentListRecyclerview(requestedappointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickOfDoctorAppointment {
            override fun onItemClick(position: Int) {
                lastPosition = position
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter?.requestedappointmentList!![position]!!.id!!, "doctor", "pending"))
            }

            override fun onAcceptBtnClick(position: String, text: String) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
//                homeViewModel?.appSharedPref?.deleteUserId()
                        booleanIsAcceptedClick = true
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var updateAppointmentRequest= UpdateAppointmentRequest()
//            getDoctorUpcommingAppointmentRequest.userId=fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
                            updateAppointmentRequest.id=contactListAdapter?.requestedappointmentList!![position.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus=text
                            fragmentRequestedAppointmentViewModel!!.apiupdatedoctorappointmentrequest(
                                updateAppointmentRequest,
                                position.toInt()
                            )
                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to accept this appointment?")


            }

            override fun onRejectBtnBtnClick(position: String, text: String) {

                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
//                homeViewModel?.appSharedPref?.deleteUserId()
                        booleanIsAcceptedClick = false
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            var updateAppointmentRequest= UpdateAppointmentRequest()
//            getDoctorUpcommingAppointmentRequest.userId=fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
                            updateAppointmentRequest.id=contactListAdapter?.requestedappointmentList!![position.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus=text
                            fragmentRequestedAppointmentViewModel!!.apiupdatedoctorappointmentrequest(updateAppointmentRequest, position.toInt())
                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to reject this appointment?")
            }

        }

        if (lastPosition!=null){
            Handler().postDelayed({
                activity?.runOnUiThread {
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 400)
        }
    }

    override fun successGetDoctorRequestAppointmentResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?) {
        baseActivity?.hideLoading()
        if(getDoctorRequestAppointmentResponse?.code.equals("200")){
            if(getDoctorRequestAppointmentResponse?.result!=null && getDoctorRequestAppointmentResponse?.result.size>0){
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility=View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility=View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorRequestAppointmentResponse?.result)
            }else{
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility=View.GONE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.setText("Doctor Appointment Request Not Found.")
            }

        }else{
            fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility=View.GONE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.setText(getDoctorRequestAppointmentResponse?.message)
            Toast.makeText(activity, getDoctorRequestAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    ) {
        baseActivity?.hideLoading()
        if(getDoctorRequestAppointmentResponse?.code.equals("200")){
            if (booleanIsAcceptedClick) {
                contactListAdapter?.requestedappointmentList!![position]?.acceptanceStatus = "accept"
            }else{
                contactListAdapter?.requestedappointmentList!![position]?.acceptanceStatus = "reject"
            }
            contactListAdapter?.notifyItemChanged(position)
            /*Toast.makeText(activity, getDoctorRequestAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
            if(isNetworkConnected){
                baseActivity?.showLoading()
                var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId=fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
                fragmentRequestedAppointmentViewModel!!.apidoctorappointmentrequestlist(getDoctorUpcommingAppointmentRequest)
            }else{
                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
            }*/
            contactListAdapter?.requestedappointmentList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.requestedappointmentList?.size!!)
            if (contactListAdapter?.requestedappointmentList?.size == 0){
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorRequestedAppointment?.visibility = View.GONE
            }
        }else{
            Toast.makeText(activity, getDoctorRequestAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}