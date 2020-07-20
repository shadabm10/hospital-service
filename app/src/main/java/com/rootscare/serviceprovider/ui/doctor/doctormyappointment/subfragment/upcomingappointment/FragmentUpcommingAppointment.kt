package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.upcomingappointment.ResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorUpcomingAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.adapter.AdapterDoctorUpcommingAppointment
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import java.lang.reflect.Field
import java.util.*


class FragmentUpcommingAppointment: BaseFragment<FragmentDoctorUpcomingAppointmentBinding, FragmentUpcommingAppointmentViewModel>(),
    FragmentUpcommingAppointmentNavigator {

    private var contactListAdapter:AdapterDoctorUpcommingAppointment?=null
    private var yearForReopen:Int?=null
    private var monthForReopen:Int?=null
    private var dayForReopen:Int?=null
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
                FragmentUpcommingAppointmentViewModel::class.java
            )
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


            val dpd = DatePickerDialog(this.activity!!, DatePickerDialog.OnDateSetListener { view, yearLocal, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                yearForReopen = yearLocal
                monthForReopen = monthOfYear
                dayForReopen = dayOfMonth
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
                fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text = "" + yearLocal + "-" + monthOfDob+ "-" + dayOfDob
                if(fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString()!=null && !fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString().equals("")){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        var filterAppointmentRequest= FilterAppointmentRequest()
                        filterAppointmentRequest.userId=fragmentUpcommingAppointmentViewModel?.appSharedPref?.loginUserId
//                        filterAppointmentRequest.userId="18"
                        filterAppointmentRequest.appointmentDate =fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString()
                        fragmentUpcommingAppointmentViewModel!!.apifilterdoctorupcomingappointmentlist(filterAppointmentRequest)
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            }, year, month, day)
            if (yearForReopen!=null && monthForReopen!=null && dayForReopen!=null){
                dpd.updateDate(yearForReopen!!,monthForReopen!!, dayForReopen!!)
            }
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
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
        contactListAdapter = AdapterDoctorUpcommingAppointment(upcomingAppointmentList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickOfDoctorAppointment2 {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter?.upcomingAppointmentList!![position]!!.id!!, "doctor"))
            }

            override fun onAcceptBtnClick(id: String, text: String) {

            }

            override fun onUploadBtnClick(id: String, text: String) {

            }

            override fun onRejectBtnBtnClick(position: String, text: String) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            var updateAppointmentRequest = UpdateAppointmentRequest()
                            updateAppointmentRequest.id = contactListAdapter?.upcomingAppointmentList!![position.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentUpcommingAppointmentViewModel!!.apiupdatedoctorappointmentrequest(updateAppointmentRequest, position.toInt())
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to reject this appointment?")
            }

        }

    }

    override fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?) {
        baseActivity?.hideLoading()
        if (doctorUpcomingAppointmentResponse?.code.equals("200")){

            if(doctorUpcomingAppointmentResponse?.result!=null && doctorUpcomingAppointmentResponse.result.size >0){
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility=View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility=View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(doctorUpcomingAppointmentResponse.result)
            }else{
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility=View.GONE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.text = "No Upcoming Appointment List Found for this Doctor."
            }

        }else{
            fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility=View.GONE
            fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.text = doctorUpcomingAppointmentResponse?.message
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

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    ) {
        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            contactListAdapter?.upcomingAppointmentList!![position]?.acceptanceStatus = "reject"
            contactListAdapter?.notifyItemChanged(position)
            contactListAdapter?.upcomingAppointmentList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.upcomingAppointmentList?.size!!)
            if (contactListAdapter?.upcomingAppointmentList?.size == 0){
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility = View.GONE
            }
        }
    }


}