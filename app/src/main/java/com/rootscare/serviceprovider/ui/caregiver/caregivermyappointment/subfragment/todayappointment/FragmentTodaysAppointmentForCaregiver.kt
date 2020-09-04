package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.todayappointment

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
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorTodaysAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.todayappointment.adapter.AdapterCaregiverTodaysAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment.adapter.AdapterNurseTodaysAppointmentRecyclerview
import java.util.*

class FragmentTodaysAppointmentForCaregiver : BaseFragment<FragmentDoctorTodaysAppointmentBinding, FragmentTodaysAppointmentForNurseViewModel>(),
    FragmentTodaysAppointmentForNurseNavigator {
    private var lastPosition:Int? = null
    private var uploadedImageItemPosition: Int? = null
    private var contactListAdapter: AdapterCaregiverTodaysAppointmentRecyclerview? = null

    //    private var fileNameForTempUse: String = ""
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null

    private var fragmentDoctorTodaysAppointmentBinding: FragmentDoctorTodaysAppointmentBinding? = null
    private var fragmentTodaysAppointmentViewModel: FragmentTodaysAppointmentForNurseViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_todays_appointment
    override val viewModel: FragmentTodaysAppointmentForNurseViewModel
        get() {
            fragmentTodaysAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentTodaysAppointmentForNurseViewModel::class.java
            )
            return fragmentTodaysAppointmentViewModel as FragmentTodaysAppointmentForNurseViewModel
        }

    companion object {
        private val TAG = FragmentTodaysAppointmentForCaregiver::class.java.simpleName
        fun newInstance(): FragmentTodaysAppointmentForCaregiver {
            val args = Bundle()
            val fragment = FragmentTodaysAppointmentForCaregiver()
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

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId = fragmentTodaysAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentTodaysAppointmentViewModel!!.apiNurseTodayAppointmentList(getDoctorUpcommingAppointmentRequest)
        } else {
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
        contactListAdapter = AdapterCaregiverTodaysAppointmentRecyclerview(todaysAppointList, context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView2 = object : OnClickOfDoctorAppointment2 {
            override fun onItemClick(position: Int) {
                lastPosition = position
                (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter?.todaysAppointList!![position]!!.id!!, "nurse","today")
                )
            }

            override fun onAcceptBtnClick(position: String, text: String) {
                apiHitForCompleted(contactListAdapter?.todaysAppointList!![position.toInt()]!!.id!!, position.toInt())
            }

            override fun onUploadBtnClick(position: String, text: String) {
//                showPictureDialog()
                uploadedImageItemPosition = position.toInt()
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
                            updateAppointmentRequest.id = contactListAdapter?.todaysAppointList!![position.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentTodaysAppointmentViewModel!!.apiUpdateNurseAppointmentRequest(updateAppointmentRequest, position.toInt())
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to reject this appointment?")
            }
        }

        if (lastPosition!=null) {
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

    override fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?) {

        baseActivity?.hideLoading()
        if (getDoctorTodaysAppointmentResponse?.code.equals("200")) {
            if (getDoctorTodaysAppointmentResponse?.result != null && getDoctorTodaysAppointmentResponse?.result?.size > 0) {
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorTodaysAppointmentResponse?.result)
            } else {
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.setText("Doctor Today's Appointment List Not Found.")
            }

        } else {
            fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
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
        if (response.code.equals("200")) {
            contactListAdapter?.todaysAppointList!![position]?.appointmentStatus = "Completed"
            contactListAdapter?.notifyItemChanged(position)
            /*contactListAdapter?.todaysAppointList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.todaysAppointList?.size!!)
            if (contactListAdapter?.todaysAppointList?.size == 0){
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            }*/
        }
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    ) {

        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            contactListAdapter?.todaysAppointList!![position]?.acceptanceStatus = "reject"
            contactListAdapter?.notifyItemChanged(position)
            contactListAdapter?.todaysAppointList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.todaysAppointList?.size!!)
            if (contactListAdapter?.todaysAppointList?.size == 0) {
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            }
        }
    }

    private fun apiHitForCompleted(id: String, position: Int) {
        var request = CommonUserIdRequest()
        request.id = id
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentTodaysAppointmentViewModel!!.apiHitForMarkAsCompleteForNurse(
                request, position
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }


}