package com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter.AdapterReviewAndRatingRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfile
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileNavigator
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfileViewModel
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity

class FragmentReviewAndRating: BaseFragment<FragmentReviewAndRatingBinding, FragmentReviewAndRatingViewModel>(),
    FragmentReviewAndRatingNavigator {
    private var fragmentReviewAndRatingBinding: FragmentReviewAndRatingBinding? = null
    private var fragmentReviewAndRatingViewModel: FragmentReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_review_and_rating
    override val viewModel: FragmentReviewAndRatingViewModel
        get() {
            fragmentReviewAndRatingViewModel = ViewModelProviders.of(this).get(
                FragmentReviewAndRatingViewModel::class.java!!)
            return fragmentReviewAndRatingViewModel as FragmentReviewAndRatingViewModel
        }

    companion object {
        fun newInstance(): FragmentReviewAndRating {
            val args = Bundle()
            val fragment = FragmentReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentReviewAndRatingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentReviewAndRatingBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })
        setUpViewReviewAndRatinglistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentReviewAndRatingBinding!!.recyclerViewRootscareReviewandrating != null)
        val recyclerView = fragmentReviewAndRatingBinding!!.recyclerViewRootscareReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterReviewAndRatingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }
}