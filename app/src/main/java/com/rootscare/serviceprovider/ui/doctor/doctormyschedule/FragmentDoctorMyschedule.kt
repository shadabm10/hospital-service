package com.rootscare.serviceprovider.ui.doctor.doctormyschedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyScheduleBinding
import com.rootscare.serviceprovider.databinding.FragmentReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.adapter.AddapterDoctorMyAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.adapter.AdapterDoctorMyScheduleRecyclerView
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.FragmentdoctorManageSchedule
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRatingNavigator
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRatingViewModel
import com.rootscare.serviceprovider.ui.home.HomeActivity

class FragmentDoctorMyschedule: BaseFragment<FragmentDoctorMyScheduleBinding, FragmentDoctorMyscheduleViewModel>(),
    FragmentDoctorMyscheduleNavigator {
    private var fragmentDoctorMyScheduleBinding: FragmentDoctorMyScheduleBinding? = null
    private var fragmentDoctorMyscheduleViewModel: FragmentDoctorMyscheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_my_schedule
    override val viewModel: FragmentDoctorMyscheduleViewModel
        get() {
            fragmentDoctorMyscheduleViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorMyscheduleViewModel::class.java!!)
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
            CommonDialog.showDialogForAddHospitalOrClinic(this!!.activity!!,object:
                DialogClickCallback{
                override fun onConfirm() {

                }

                override fun onDismiss() {
                }
            })
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
        val contactListAdapter = AdapterDoctorMyScheduleRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentdoctorManageSchedule.newInstance())
            }

        }

    }

}