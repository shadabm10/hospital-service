package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.myscheduleaddhospital.AddHospitalRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.MyScheduleHospitalResponse
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.ResultItem
import com.rootscare.interfaces.DialogClickCallbackWithFields
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.adapter.AdapterDoctorMyScheduleRecyclerView
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.FragmentdoctorManageSchedule
import com.rootscare.serviceprovider.ui.home.HomeActivity
import java.util.*

class FragmentDoctorMyschedule : BaseFragment<FragmentDoctorMyScheduleBinding, FragmentDoctorMyscheduleViewModel>(),
    FragmentDoctorMyscheduleNavigator {

    private var contactListAdapter: AdapterDoctorMyScheduleRecyclerView? = null
    private var localTempDataList: LinkedList<ResultItem>? = null

    private var fragmentDoctorMyScheduleBinding: FragmentDoctorMyScheduleBinding? = null
    private var fragmentDoctorMyscheduleViewModel: FragmentDoctorMyscheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_my_schedule
    override val viewModel: FragmentDoctorMyscheduleViewModel
        get() {
            fragmentDoctorMyscheduleViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorMyscheduleViewModel::class.java
            )
            return fragmentDoctorMyscheduleViewModel as FragmentDoctorMyscheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentDoctorMyschedule {
            val args = Bundle()
            val fragment = FragmentDoctorMyschedule()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorMyscheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorMyScheduleBinding = viewDataBinding
        setUpDoctorMySchedulelistingRecyclerview()
        fragmentDoctorMyScheduleBinding?.btnAddHospitalOrClinic?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForAddHospitalOrClinic(activity!!, object :
                DialogClickCallbackWithFields {
                override fun onConfirm(hospitalName: String?, address: String?) {
                    addHospitalHitApi(hospitalName!!, address!!)
                }

                override fun onDismiss() {
                }
            })
        })
        apiHitFetchHospitalList()


        fragmentDoctorMyScheduleBinding?.edittextSearchHospital?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (localTempDataList != null && localTempDataList?.size!! > 0 && contactListAdapter != null) {
                    filter(s.toString(), localTempDataList!!, contactListAdapter!!)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMySchedulelistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorMyScheduleBinding!!.recyclerViewDoctorMyscheduleListing != null)
        val recyclerView = fragmentDoctorMyScheduleBinding!!.recyclerViewDoctorMyscheduleListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterDoctorMyScheduleRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentdoctorManageSchedule.newInstance(contactListAdapter?.result!![position])
                )
            }

        }

    }


    private fun apiHitFetchHospitalList(){
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId =
                fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserId
            fragmentDoctorMyscheduleViewModel!!.getHospitalListFromApi(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun addHospitalHitApi(hospitalName:String, address:String) {
        baseActivity?.showLoading()
        var request = AddHospitalRequest()
        request.doctor_id = fragmentDoctorMyscheduleViewModel?.appSharedPref?.loginUserId
        request.name = hospitalName
        request.address = address
        fragmentDoctorMyscheduleViewModel?.addHospital(request)
    }


    override fun onSuccessHospitalList(response: MyScheduleHospitalResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                hideNoData()
                localTempDataList = response.result
                contactListAdapter?.result = localTempDataList!!
                contactListAdapter?.notifyDataSetChanged()
            } else {
                showNoData()
            }
        } else {
            showNoData()
        }
    }

    override fun onnSuccessAddHospital(response: CommonResponse) {
//        baseActivity?.hideLoading()
        apiHitFetchHospitalList()
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }


    private fun filter(
        text: String,
        projectlist: LinkedList<ResultItem>,
        adapter: AdapterDoctorMyScheduleRecyclerView
    ) { //new array list that will hold the filtered data
        var filterdNames: LinkedList<ResultItem> = LinkedList()
        if (TextUtils.isEmpty(text.trim())) {
            filterdNames = projectlist
        } else {
            for (projectDataType in projectlist) {
                if (projectDataType.name?.toLowerCase()?.contains(text.toLowerCase())!!) {
                    filterdNames.add(projectDataType)
                }
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames)
        if (filterdNames.size > 0) {
            hideNoData()
        } else {
            showNoData()
        }
    }

    private fun showNoData() {
        with(fragmentDoctorMyScheduleBinding!!) {
            recyclerViewDoctorMyscheduleListing.visibility = View.GONE
            tvNoDate.visibility = View.VISIBLE
        }
    }

    private fun hideNoData() {
        with(fragmentDoctorMyScheduleBinding!!) {
            recyclerViewDoctorMyscheduleListing.visibility = View.VISIBLE
            tvNoDate.visibility = View.GONE
        }
    }
}