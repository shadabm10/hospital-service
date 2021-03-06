package com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.response.doctor.review.ReviewResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter.AdapterReviewAndRatingRecyclerview
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.adapter.AdapterNursesReviewAndRating

class FragmentNursesReviewAndRating: BaseFragment<FragmentNursesReviewAndRatingBinding, FragmentNursesReviewAndRatingViewModel>(),
    FragmentNursesReviewAndRatingNavigator {

    private var contactListAdapter: AdapterNursesReviewAndRating? = null

    private var fragmentNursesReviewAndRatingBinding: FragmentNursesReviewAndRatingBinding? = null
    private var fragmentNursesReviewAndRatingViewModel: FragmentNursesReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_review_and_rating
    override val viewModel: FragmentNursesReviewAndRatingViewModel
        get() {
            fragmentNursesReviewAndRatingViewModel = ViewModelProviders.of(this).get(
                FragmentNursesReviewAndRatingViewModel::class.java!!)
            return fragmentNursesReviewAndRatingViewModel as FragmentNursesReviewAndRatingViewModel
        }
    companion object {
        fun newInstance(): FragmentNursesReviewAndRating {
            val args = Bundle()
            val fragment = FragmentNursesReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesReviewAndRatingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesReviewAndRatingBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })
        setUpViewReviewAndRatinglistingRecyclerview()

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId =
                fragmentNursesReviewAndRatingViewModel?.appSharedPref?.loginUserId
            fragmentNursesReviewAndRatingViewModel!!.getReviewFromApi(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesReviewAndRatingBinding!!.recyclerViewNursesReviewandrating != null)
        val recyclerView = fragmentNursesReviewAndRatingBinding!!.recyclerViewNursesReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterNursesReviewAndRating(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }
    }

    override fun onSuccessReview(response: ReviewResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentNursesReviewAndRatingBinding?.recyclerViewNursesReviewandrating?.visibility = View.VISIBLE
                fragmentNursesReviewAndRatingBinding?.tvNoDate?.visibility = View.GONE
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            }else{
                fragmentNursesReviewAndRatingBinding?.recyclerViewNursesReviewandrating?.visibility = View.INVISIBLE
                fragmentNursesReviewAndRatingBinding?.tvNoDate?.visibility = View.VISIBLE
            }
        }else{
            fragmentNursesReviewAndRatingBinding?.recyclerViewNursesReviewandrating?.visibility = View.INVISIBLE
            fragmentNursesReviewAndRatingBinding?.tvNoDate?.visibility = View.VISIBLE
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}