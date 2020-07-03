package com.rootscare.serviceprovider.ui.nurses.nurseprofile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.response.doctor.profileresponse.DepartmentItem
import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.api.response.doctor.profileresponse.QualificationDataItem
import com.rootscare.data.model.api.response.doctor.profileresponse.ReviewRatingItem
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesHomeBinding
import com.rootscare.serviceprovider.databinding.FragmentNursesProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfile
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctorImportantDocumentrecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsReviewListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsSpecilityListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHome
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHomeNavigator
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHomeViewModel
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterNursesUploadDocument
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile

class FragmentNursesProfile: BaseFragment<FragmentNursesProfileBinding, FragmentNursesProfileViewModel>(),
    FragmentNursesProfileNavigator {
    private var fragmentNursesProfileBinding: FragmentNursesProfileBinding? = null
    private var fragmentNursesProfileViewModel: FragmentNursesProfileViewModel? = null
    var  initialReviewRatingList: ArrayList<ReviewRatingItem?>?=null
    var  finalReviewRatingList: ArrayList<ReviewRatingItem?>?=null
    var doctorFirstName=""
    var doctorLastName=""
    var doctorEmail=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_profile
    override val viewModel: FragmentNursesProfileViewModel
        get() {
            fragmentNursesProfileViewModel = ViewModelProviders.of(this).get(
                FragmentNursesProfileViewModel::class.java!!)
            return fragmentNursesProfileViewModel as FragmentNursesProfileViewModel
        }


    companion object {
        fun newInstance(): FragmentNursesProfile {
            val args = Bundle()
            val fragment = FragmentNursesProfile()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesProfileBinding = viewDataBinding
//        setUpViewPrescriptionlistingRecyclerview()

        if(isNetworkConnected){
            baseActivity?.showLoading()
            var commonUserIdRequest= CommonUserIdRequest()
            commonUserIdRequest.id=fragmentNursesProfileViewModel?.appSharedPref?.loginUserId
            fragmentNursesProfileViewModel!!.apinurseprofile(commonUserIdRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        fragmentNursesProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentNursesEditProfile.newInstance())
                FragmentEditDoctorProfile.newInstance("nurse"))
        })

        fragmentNursesProfileBinding?.tvReviewratingReadmore?.setOnClickListener(View.OnClickListener {

            if(fragmentNursesProfileBinding?.tvReviewratingReadmore?.text!!.equals("Read More")){
                if (finalReviewRatingList!=null && finalReviewRatingList!!.size>0){
                    setReviewRatingListing(finalReviewRatingList)
                    fragmentNursesProfileBinding?.tvReviewratingReadmore?.setText("Read Less")
                }

            }else if (fragmentNursesProfileBinding?.tvReviewratingReadmore?.text!!.equals("Read Less")){
                if (initialReviewRatingList!=null && initialReviewRatingList!!.size>0){
                    setReviewRatingListing(initialReviewRatingList)
                    fragmentNursesProfileBinding?.tvReviewratingReadmore?.setText("Read More")
                }
            }
        })

    }


    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview(qualificationDataList: ArrayList<QualificationDataItem>?) {
        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorimportentDocument != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorimportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctorImportantDocumentrecyclerview(qualificationDataList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setReviewRatingListing(reviewRatingList: ArrayList<ReviewRatingItem?>?) {
        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorReview != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorReview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctordetailsReviewListRecyclerview(reviewRatingList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setSpecilityDataListing(departmentList: ArrayList<DepartmentItem?>?) {
        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorSpecility != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorSpecility
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctordetailsSpecilityListRecyclerview(departmentList,context!!)
        recyclerView.adapter = contactListAdapter


    }

    override fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?) {
        baseActivity?.hideLoading()
        if (getDoctorProfileResponse?.code.equals("200")){
            if(getDoctorProfileResponse?.result!=null){

                if(getDoctorProfileResponse?.result?.firstName!=null && !getDoctorProfileResponse?.result?.firstName.equals("")){
                    doctorFirstName=getDoctorProfileResponse?.result?.firstName
                }else{
                    doctorFirstName=""
                }

                if(getDoctorProfileResponse?.result?.lastName!=null && !getDoctorProfileResponse?.result?.lastName.equals("")){
                    doctorLastName=getDoctorProfileResponse?.result?.lastName
                }else{
                    doctorLastName=""
                }
                fragmentNursesProfileBinding?.tvDoctorName?.setText(doctorFirstName+" "+doctorLastName)
                if(getDoctorProfileResponse?.result?.email!=null && !getDoctorProfileResponse?.result?.email.equals("")){
                    doctorEmail=getDoctorProfileResponse?.result?.email
                }else{
                    doctorEmail=""
                }

                fragmentNursesProfileBinding?.tvDoctorEmail?.setText(doctorEmail)

                val options: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile_no_image)
                        .priority(Priority.HIGH)
                Glide
                    .with(activity!!)
                    .load(getString(R.string.api_base) + "uploads/images/" + getDoctorProfileResponse?.result?.image)
                    .apply(options)
                    .into(fragmentNursesProfileBinding?.imgDoctorProfile!!)

                if(getDoctorProfileResponse?.result?.avgRating!=null && !getDoctorProfileResponse?.result?.avgRating.equals("")){
                    fragmentNursesProfileBinding?.tvReviews?.setText(getDoctorProfileResponse?.result?.avgRating+" "+"reviews")
                    fragmentNursesProfileBinding?.ratingBarteacherFeedback?.rating=getDoctorProfileResponse?.result?.avgRating.toFloat()
                }

                if(getDoctorProfileResponse?.result?.qualification!=null && !getDoctorProfileResponse?.result?.qualification.equals("")){
                    fragmentNursesProfileBinding?.tvDoctorQualification?.setText(getDoctorProfileResponse?.result?.qualification)
                }else{
                    fragmentNursesProfileBinding?.tvDoctorQualification?.setText("")
                }

                if (getDoctorProfileResponse?.result?.address!=null && !getDoctorProfileResponse?.result?.address.equals("")){
                    fragmentNursesProfileBinding?.tvDoctorAddress?.setText(getDoctorProfileResponse?.result?.address)
                }else{
                    fragmentNursesProfileBinding?.tvDoctorAddress?.setText("")
                }
                if (getDoctorProfileResponse?.result?.description!=null && !getDoctorProfileResponse?.result?.description.equals("")){
                    fragmentNursesProfileBinding?.tvDoctorAddress?.append("\n"+getDoctorProfileResponse?.result?.description)
                }
                if (getDoctorProfileResponse?.result?.experience!=null && !getDoctorProfileResponse?.result?.experience.equals("")){
                    fragmentNursesProfileBinding?.tvDoctorAddress?.append("\n\nExperience: "+getDoctorProfileResponse?.result?.experience +" Years")
                }
                if (getDoctorProfileResponse?.result?.dailyRate!=null && !getDoctorProfileResponse?.result?.dailyRate.equals("")){
                    fragmentNursesProfileBinding?.tvDoctorFees?.setText("SAR"+" "+getDoctorProfileResponse?.result?.dailyRate)
                }else{
                    fragmentNursesProfileBinding?.tvDoctorFees?.setText("")
                }

                if(getDoctorProfileResponse?.result?.qualificationData!=null && getDoctorProfileResponse?.result?.qualificationData.size>0){
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility=View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDate?.visibility=View.GONE
                    setUpViewPrescriptionlistingRecyclerview(getDoctorProfileResponse?.result?.qualificationData)
                }else{
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility=View.GONE
                    fragmentNursesProfileBinding?.tvNoDate?.visibility=View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDate?.setText("No important document found!")
                }

                if(getDoctorProfileResponse?.result?.department!=null && getDoctorProfileResponse?.result?.department.size>0){
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility=View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDataDoctorSpecility?.visibility=View.GONE
                    setSpecilityDataListing(getDoctorProfileResponse?.result?.department)
                }else{
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility=View.GONE
                    fragmentNursesProfileBinding?.tvNoDataDoctorSpecility?.visibility=View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDataDoctorSpecility?.setText("No specility found!")

                }
                initialReviewRatingList= ArrayList<ReviewRatingItem?>()
                finalReviewRatingList= ArrayList<ReviewRatingItem?>()

                if(getDoctorProfileResponse?.result?.reviewRating!=null && getDoctorProfileResponse?.result?.reviewRating.size>0){
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorReview?.visibility=View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDataDoctorReview?.visibility=View.GONE
                    finalReviewRatingList=getDoctorProfileResponse?.result?.reviewRating

                    if(getDoctorProfileResponse?.result?.reviewRating?.size>1){
                        fragmentNursesProfileBinding?.tvReviewratingReadmore?.visibility=View.VISIBLE
                        var reviewRatingItem=ReviewRatingItem()
                        reviewRatingItem.rating=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.rating
                        reviewRatingItem.review=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.review
                        reviewRatingItem.reviewBy=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.reviewBy
                        initialReviewRatingList?.add(reviewRatingItem)
                        setReviewRatingListing(initialReviewRatingList)
                    }else{

                        fragmentNursesProfileBinding?.tvReviewratingReadmore?.visibility=View.GONE
                        finalReviewRatingList= ArrayList<ReviewRatingItem?>()
                        for (i in 0 until getDoctorProfileResponse?.result?.reviewRating?.size) {
                            var reviewRatingItem=ReviewRatingItem()
                            reviewRatingItem.rating=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.rating
                            reviewRatingItem.review=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.review
                            reviewRatingItem.reviewBy=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.reviewBy
                            finalReviewRatingList?.add(reviewRatingItem)
                            setReviewRatingListing(finalReviewRatingList)
                        }

                    }
                }else{
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorReview?.visibility=View.GONE
                    fragmentNursesProfileBinding?.tvNoDataDoctorReview?.visibility=View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDataDoctorReview?.setText("No review found")
                }
            }else{
                Toast.makeText(activity, getDoctorProfileResponse?.message, Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(activity, getDoctorProfileResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorGetDoctorProfileResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}