package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorTodaysAppointmentBinding
import com.rootscare.serviceprovider.databinding.FragmentDoctorUpcomingAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter.AdapterDoctorTodaysAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointmentNavigator
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointmentViewModel
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.adapter.AdapterDoctorUpcommingAppointment
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import java.util.ArrayList

class FragmentTodaysAppointment : BaseFragment<FragmentDoctorTodaysAppointmentBinding, FragmentTodaysAppointmentViewModel>(),
    FragmentTodaysAppointmentNavigator {

    private var contactListAdapter:AdapterDoctorTodaysAppointmentRecyclerview?=null

    private var fragmentDoctorTodaysAppointmentBinding: FragmentDoctorTodaysAppointmentBinding? = null
    private var fragmentTodaysAppointmentViewModel: FragmentTodaysAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_todays_appointment
    override val viewModel: FragmentTodaysAppointmentViewModel
        get() {
            fragmentTodaysAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentTodaysAppointmentViewModel::class.java!!)
            return fragmentTodaysAppointmentViewModel as FragmentTodaysAppointmentViewModel
        }
    companion object {
        fun newInstance(): FragmentTodaysAppointment {
            val args = Bundle()
            val fragment = FragmentTodaysAppointment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTodaysAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorTodaysAppointmentBinding = viewDataBinding

        if(isNetworkConnected){
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest= GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId=fragmentTodaysAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentTodaysAppointmentViewModel!!.apidoctortodayappointmentlist(getDoctorUpcommingAppointmentRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(todaysAppointList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorTodaysAppointmentBinding!!.recyclerViewDoctorTodaysAppointment != null)
        val recyclerView = fragmentDoctorTodaysAppointmentBinding!!.recyclerViewDoctorTodaysAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterDoctorTodaysAppointmentRecyclerview(todaysAppointList,context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView2= object : OnClickOfDoctorAppointment {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorAppointmentDetails.newInstance(contactListAdapter?.todaysAppointList!![position]!!.id!!, "doctor"))
            }

            override fun onAcceptBtnClick(position: String, text: String) {
                apiHitForCompleted(contactListAdapter?.todaysAppointList!![position.toInt()]!!.id!!, position.toInt())
            }

            override fun onRejectBtnBtnClick(id: String, text: String) {

            }
        }

    }

    override fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?) {

        baseActivity?.hideLoading()
        if (getDoctorTodaysAppointmentResponse?.code.equals("200")){
            if(getDoctorTodaysAppointmentResponse?.result!=null && getDoctorTodaysAppointmentResponse?.result?.size>0){
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility=View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility=View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorTodaysAppointmentResponse?.result)
            }else{
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility=View.GONE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.setText("Doctor Today's Appointment List Not Found.")
            }

        }else{
            fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility=View.GONE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.setText(getDoctorTodaysAppointmentResponse?.message)
            Toast.makeText(activity, getDoctorTodaysAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessMarkAsComplete(response: CommonResponse, position: Int) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")){
            contactListAdapter?.todaysAppointList!![position]?.appointmentStatus = "Completed"
            contactListAdapter?.notifyItemChanged(position)
        }
    }

    private fun apiHitForCompleted(id:String, position:Int){
        var request = CommonUserIdRequest()
        request.id = id
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentTodaysAppointmentViewModel!!.apiHitForMarkAsComplete(
                request, position
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

}