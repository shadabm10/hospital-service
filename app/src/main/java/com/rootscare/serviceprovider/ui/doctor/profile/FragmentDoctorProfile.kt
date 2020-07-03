package com.rootscare.serviceprovider.ui.doctor.profile

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
import com.rootscare.serviceprovider.databinding.FragmentDoctorProfileBinding
import com.rootscare.serviceprovider.databinding.FragmentHomeBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter.AdapterReviewAndRatingRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctorImportantDocumentrecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsReviewListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsSpecilityListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHome
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHomeNavigator
import com.rootscare.serviceprovider.ui.home.subfragment.FragmentHomeVirewModel
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterNursesUploadDocument

class FragmentDoctorProfile: BaseFragment<FragmentDoctorProfileBinding, FragmentDoctorProfileViewModel>(),
    FragmentDoctorProfileNavigator {
    private var fragmentDoctorProfileBinding: FragmentDoctorProfileBinding? = null
    private var fragmentDoctorProfileViewModel: FragmentDoctorProfileViewModel? = null
   var  initialReviewRatingList: ArrayList<ReviewRatingItem?>?=null
    var  finalReviewRatingList: ArrayList<ReviewRatingItem?>?=null
    var doctorFirstName=""
    var doctorLastName=""
    var doctorEmail=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_profile
    override val viewModel: FragmentDoctorProfileViewModel
        get() {
            fragmentDoctorProfileViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorProfileViewModel::class.java!!)
            return fragmentDoctorProfileViewModel as FragmentDoctorProfileViewModel
        }
    companion object {
        fun newInstance(): FragmentDoctorProfile {
            val args = Bundle()
            val fragment = FragmentDoctorProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorProfileBinding = viewDataBinding
        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentEditDoctorProfile.newInstance("doctor"))
        })
// GET PROFILE API CALL
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var commonUserIdRequest=CommonUserIdRequest()
            commonUserIdRequest.id=fragmentDoctorProfileViewModel?.appSharedPref?.loginUserId
            fragmentDoctorProfileViewModel!!.apidoctorprofile(commonUserIdRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        //Add Read More Click
        fragmentDoctorProfileBinding?.tvReviewratingReadmore?.setOnClickListener(View.OnClickListener {

            if(fragmentDoctorProfileBinding?.tvReviewratingReadmore?.text!!.equals("Read More")){
                if (finalReviewRatingList!=null && finalReviewRatingList!!.size>0){
                    setReviewRatingListing(finalReviewRatingList)
                    fragmentDoctorProfileBinding?.tvReviewratingReadmore?.setText("Read Less")
                }

            }else if (fragmentDoctorProfileBinding?.tvReviewratingReadmore?.text!!.equals("Read Less")){
                if (initialReviewRatingList!=null && initialReviewRatingList!!.size>0){
                    setReviewRatingListing(initialReviewRatingList)
                    fragmentDoctorProfileBinding?.tvReviewratingReadmore?.setText("Read More")
                }
            }
        })

    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview(qualificationDataList: ArrayList<QualificationDataItem>?) {
        assert(fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorimportentDocument != null)
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorimportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctorImportantDocumentrecyclerview(qualificationDataList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setReviewRatingListing(reviewRatingList: ArrayList<ReviewRatingItem?>?) {
        assert(fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorReview != null)
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorReview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctordetailsReviewListRecyclerview(reviewRatingList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setSpecilityDataListing(departmentList: ArrayList<DepartmentItem?>?) {
        assert(fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorSpecility != null)
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorSpecility
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
                fragmentDoctorProfileBinding?.tvDoctorName?.setText(doctorFirstName+" "+doctorLastName)
                if(getDoctorProfileResponse?.result?.email!=null && !getDoctorProfileResponse?.result?.email.equals("")){
                    doctorEmail=getDoctorProfileResponse?.result?.email
                }else{
                    doctorEmail=""
                }

                fragmentDoctorProfileBinding?.tvDoctorEmail?.setText(doctorEmail)

                val options: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile_no_image)
                        .priority(Priority.HIGH)
                Glide
                    .with(this@FragmentDoctorProfile)
                    .load(getString(R.string.api_base) + "uploads/images/" + getDoctorProfileResponse?.result?.image)
                    .apply(options)
                    .into(fragmentDoctorProfileBinding?.imgDoctorProfile!!)

                if(getDoctorProfileResponse?.result?.avgRating!=null && !getDoctorProfileResponse?.result?.avgRating.equals("")){
                    fragmentDoctorProfileBinding?.tvReviews?.setText(getDoctorProfileResponse?.result?.avgRating+" "+"reviews")
                    fragmentDoctorProfileBinding?.ratingBarteacherFeedback?.rating=getDoctorProfileResponse?.result?.avgRating.toFloat()
                }

                if(getDoctorProfileResponse?.result?.qualification!=null && !getDoctorProfileResponse?.result?.qualification.equals("")){
                    fragmentDoctorProfileBinding?.tvDoctorQualification?.setText(getDoctorProfileResponse?.result?.qualification)
                }else{
                    fragmentDoctorProfileBinding?.tvDoctorQualification?.setText("")
                }

                if (getDoctorProfileResponse?.result?.address!=null && !getDoctorProfileResponse?.result?.address.equals("")){
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.setText(getDoctorProfileResponse?.result?.address)
                }else{
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.setText("")
                }
                if (getDoctorProfileResponse?.result?.description!=null && !getDoctorProfileResponse?.result?.description.equals("")){
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.append("\nDescription: "+getDoctorProfileResponse?.result?.description)
                }
                if (getDoctorProfileResponse?.result?.experience!=null && !getDoctorProfileResponse?.result?.experience.equals("")){
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.append("\n\nExperience: "+getDoctorProfileResponse?.result?.experience +" Years")
                }
                if (getDoctorProfileResponse?.result?.fees!=null && !getDoctorProfileResponse?.result?.fees.equals("")){
                    fragmentDoctorProfileBinding?.tvDoctorFees?.setText("SAR"+" "+getDoctorProfileResponse?.result?.fees)
                }else{
                    fragmentDoctorProfileBinding?.tvDoctorFees?.setText("")
                }

                if(getDoctorProfileResponse?.result?.qualificationData!=null && getDoctorProfileResponse?.result?.qualificationData.size>0){
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility=View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDate?.visibility=View.GONE
                    setUpViewPrescriptionlistingRecyclerview(getDoctorProfileResponse?.result?.qualificationData)
                }else{
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility=View.GONE
                    fragmentDoctorProfileBinding?.tvNoDate?.visibility=View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDate?.setText("No important document found!")
                }

                if(getDoctorProfileResponse?.result?.department!=null && getDoctorProfileResponse?.result?.department.size>0){
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility=View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorSpecility?.visibility=View.GONE
                    setSpecilityDataListing(getDoctorProfileResponse?.result?.department)
                }else{
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility=View.GONE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorSpecility?.visibility=View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorSpecility?.setText("No specility found!")

                }
                initialReviewRatingList= ArrayList<ReviewRatingItem?>()
                finalReviewRatingList= ArrayList<ReviewRatingItem?>()

                if(getDoctorProfileResponse?.result?.reviewRating!=null && getDoctorProfileResponse?.result?.reviewRating.size>0){
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorReview?.visibility=View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.visibility=View.GONE
                    finalReviewRatingList=getDoctorProfileResponse?.result?.reviewRating

                    if(getDoctorProfileResponse?.result?.reviewRating?.size>1){
                        fragmentDoctorProfileBinding?.tvReviewratingReadmore?.visibility=View.VISIBLE
                        var reviewRatingItem=ReviewRatingItem()
                        reviewRatingItem.rating=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.rating
                        reviewRatingItem.review=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.review
                        reviewRatingItem.reviewBy=getDoctorProfileResponse?.result?.reviewRating?.get(0)?.reviewBy
                        initialReviewRatingList?.add(reviewRatingItem)
                        setReviewRatingListing(initialReviewRatingList)
                    }else{

                        fragmentDoctorProfileBinding?.tvReviewratingReadmore?.visibility=View.GONE
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
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorReview?.visibility=View.GONE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.visibility=View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.setText("No review found")
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