package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.pastappointment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorPastAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.pastappointment.adapter.AdapterPastAppointmentListForNurse
import java.util.*


class FragmentPastAppointmentForNurse :
    BaseFragment<FragmentDoctorPastAppointmentBinding, FragmentPastAppointmentForNurseViewModel>(),
    FragmentPastAppointmentForNurseNavigator {
    private var lastPosition:Int? = null
    private var fragmentDoctorRequestedAppointmentBinding: FragmentDoctorPastAppointmentBinding? =
        null
    private var fragmentRequestedAppointmentViewModel: FragmentPastAppointmentForNurseViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_past_appointment
    override val viewModel: FragmentPastAppointmentForNurseViewModel
        get() {
            fragmentRequestedAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentPastAppointmentForNurseViewModel::class.java
            )
            return fragmentRequestedAppointmentViewModel as FragmentPastAppointmentForNurseViewModel
        }

    companion object {
        fun newInstance(): FragmentPastAppointmentForNurse {
            val args = Bundle()
            val fragment = FragmentPastAppointmentForNurse()
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
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId =
                fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentRequestedAppointmentViewModel!!.apidoctorappointmentPastList(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(requestedappointmentList: LinkedList<com.rootscare.data.model.api.response.doctor.appointment.pastappointment.ResultItem>?) {
        assert(fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorPastAppointment != null)
        val recyclerView =
            fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterPastAppointmentListForNurse(requestedappointmentList, context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(position: Int) {
                lastPosition = position
                (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter.upcomingAppointmentList!![position].id!!, "nurse")
                )
            }
        }
        if (lastPosition!=null){
            Handler().postDelayed({
                activity?.runOnUiThread {
                    val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
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


    override fun responseListPastAppointment(getDoctorRequestAppointmentResponse: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse.code.equals("200")) {
            if (getDoctorRequestAppointmentResponse.result != null && getDoctorRequestAppointmentResponse.result?.size!! > 0) {
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                    View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorRequestAppointmentResponse.result)
            } else {
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                    View.GONE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                View.GONE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = getDoctorRequestAppointmentResponse.message
            Toast.makeText(
                activity,
                getDoctorRequestAppointmentResponse.message,
                Toast.LENGTH_SHORT
            ).show()
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