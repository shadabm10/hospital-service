package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.upcomingappointment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorUpcomingAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.adapter.AdapterDoctorUpcommingAppointment
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import java.util.*


class FragmentUpcommingAppointment: BaseFragment<FragmentDoctorUpcomingAppointmentBinding, FragmentUpcommingAppointmentViewModel>(),
    FragmentUpcommingAppointmentNavigator {
    private var fragmentDoctorUpcomingAppointmentBinding: FragmentDoctorUpcomingAppointmentBinding? = null
    private var fragmentUpcommingAppointmentViewModel: FragmentUpcommingAppointmentViewModel? = null
    var monthOfDob: String=""
    var dayOfDob: String=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_upcoming_appointment
    override val viewModel: FragmentUpcommingAppointmentViewModel
        get() {
            fragmentUpcommingAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentUpcommingAppointmentViewModel::class.java!!)
            return fragmentUpcommingAppointmentViewModel as FragmentUpcommingAppointmentViewModel
        }
    companion object {
        fun newInstance(): FragmentUpcommingAppointment {
            val args = Bundle()
            val fragment = FragmentUpcommingAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentUpcommingAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorUpcomingAppointmentBinding = viewDataBinding



        if(isNetworkConnected){
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest= GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId=fragmentUpcommingAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentUpcommingAppointmentViewModel!!.apidoctorupcomingappointmentlis(getDoctorUpcommingAppointmentRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }


        fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this!!.activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                if((monthOfYear+1)<10){
                    monthOfDob= "0" + (monthOfYear+1)
                }else{
                    monthOfDob=(monthOfYear+1).toString()
                }

                if(dayOfMonth<10){
                    dayOfDob="0"+dayOfMonth

                }else{
                    dayOfDob=dayOfMonth.toString()
                }
                fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.setText("" + year + "-" + monthOfDob+ "-" + dayOfDob)
                if(fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString()!=null && !fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString().equals("")){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        var filterAppointmentRequest= FilterAppointmentRequest()
                        filterAppointmentRequest.userId=fragmentUpcommingAppointmentViewModel?.appSharedPref?.loginUserId
//                        filterAppointmentRequest.userId="18"
                        filterAppointmentRequest?.appointmentDate=fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString()
                        fragmentUpcommingAppointmentViewModel!!.apifilterdoctorupcomingappointmentlist(filterAppointmentRequest)
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            }, year, month, day)

            dpd.show()
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(upcomingAppointmentList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorUpcomingAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment != null)
        val recyclerView = fragmentDoctorUpcomingAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterDoctorUpcommingAppointment(upcomingAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorAppointmentDetails.newInstance(contactListAdapter.upcomingAppointmentList!![position]!!.id!!, "doctor"))
            }

        }

    }

    override fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?) {
        baseActivity?.hideLoading()
        if (doctorUpcomingAppointmentResponse?.code.equals("200")){

            if(doctorUpcomingAppointmentResponse?.result!=null && doctorUpcomingAppointmentResponse?.result?.size>0){
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility=View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility=View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(doctorUpcomingAppointmentResponse?.result)
            }else{
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility=View.GONE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.setText("No Upcoming Appointment List Found for this Doctor.")
            }

        }else{
            fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility=View.GONE
            fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.setText(doctorUpcomingAppointmentResponse?.message)
            Toast.makeText(activity, doctorUpcomingAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorDoctorUpcomingAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


}