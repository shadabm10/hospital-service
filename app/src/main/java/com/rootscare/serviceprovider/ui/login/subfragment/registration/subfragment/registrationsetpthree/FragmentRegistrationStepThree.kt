package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.api.response.deaprtmentlist.ResultItem
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.interfaces.OnDepartmentDropDownListItemClickListener
import com.rootscare.interfaces.onDepartItemClick
import com.rootscare.model.RegistrationModel
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationStepTwoBinding
import com.rootscare.serviceprovider.databinding.FragmentRegistrationStepthreeBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo.FragmentRegistrationStepTwo
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo.FragmentRegistrationStepTwoNavigator
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo.FragmentRegistrationStepTwoViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentRegistrationStepThree : BaseFragment<FragmentRegistrationStepthreeBinding, FragmentRegistrationStepThreeViewModel>(),
    FragmentRegistrationStepThreeNavigator {
    private var fragmentRegistrationStepthreeBinding: FragmentRegistrationStepthreeBinding? = null
    private var fragmentRegistrationStepThreeViewModel: FragmentRegistrationStepThreeViewModel? = null
    var departmentList: ArrayList<ResultItem?>?=null
    var departmentId=""
    var departTitle=""

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_stepthree
    override val viewModel: FragmentRegistrationStepThreeViewModel
        get() {
            fragmentRegistrationStepThreeViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepThreeViewModel::class.java)
            return fragmentRegistrationStepThreeViewModel as FragmentRegistrationStepThreeViewModel
        }

    companion object {
        val TAG = FragmentRegistrationStepThree::class.java.simpleName
        fun newInstance(): FragmentRegistrationStepThree {
            val args = Bundle()
            val fragment = FragmentRegistrationStepThree()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationStepThreeViewModel!!.navigator = this

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationStepthreeBinding = viewDataBinding

        fragmentRegistrationStepthreeBinding?.imageViewBack?.setOnClickListener {
            (activity as LoginActivity).onBackPressed()
        }

        fragmentRegistrationStepthreeBinding?.btnRooscareServiceproviderRegistrationSubmit?.setOnClickListener(
            View.OnClickListener {

                CommonDialog.showDialog(this.activity!!,object : DialogClickCallback{
                    override fun onConfirm() {
                        if(checkValidationForRegStepThree()){
                            AppData.registrationModelData?.description=fragmentRegistrationStepthreeBinding?.edtRegDescription?.text?.toString()
                            AppData.registrationModelData?.experience=fragmentRegistrationStepthreeBinding?.edtRegExperience?.text?.toString()
                            AppData.registrationModelData?.availableTime=fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.text?.toString()
                            AppData.registrationModelData?.fees=fragmentRegistrationStepthreeBinding?.edtRegFees?.text?.toString()
                            AppData.registrationModelData?.department=fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text?.toString()
                            if(isNetworkConnected){
                                baseActivity?.showLoading()
                                registrationApiCall()
                            }else{
                                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                    override fun onDismiss() {
                    }

                },"Confirmation", "Are you sure to register?")

            })
        if(isNetworkConnected){
//            baseActivity?.showLoading()
            fragmentRegistrationStepThreeViewModel?.apidepartmentlist()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }





        fragmentRegistrationStepthreeBinding?.txtRegDepartment?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDeaprtmentDropDownList(this.activity!!,departmentList,"Select Department",object:
                OnDepartmentDropDownListItemClickListener {
                override fun onConfirm(departmentList: ArrayList<ResultItem?>?) {
                    departTitle=""
                    departmentId=""
                    var p=0
                    for (i in 0 until departmentList?.size!!) {
                        if (departmentList.get(i)?.isChecked.equals("true")){
                            if (p==0){
                                departTitle= departmentList.get(i)?.title!!
                                departmentId= departmentList.get(i)?.id!!
                                p++

//                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
//                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
                            }else{
                                departTitle=departTitle+","+ departmentList.get(i)?.title
                            departmentId=departmentId+","+ departmentList.get(i)?.id

//                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
//                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
                            }
                        }
                    }

                    Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
                    Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")


                    fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text = departTitle
                }


            })
        })
    }

    private fun checkValidationForRegStepThree(): Boolean {
        if (fragmentRegistrationStepthreeBinding?.edtRegDescription?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your description!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepthreeBinding?.edtRegExperience?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your experience!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your available time in hour!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepthreeBinding?.edtRegFees?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your fees!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if ((activity as LoginActivity).isdeaprtmentMandatory!! && fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text?.toString()?.trim().equals("") ) {
            Toast.makeText(activity, "Please select your department!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        return true
    }

    override fun successRegistrationResponse(registrationResponse: RegistrationResponse?) {
        baseActivity?.hideLoading()
        if(registrationResponse?.code.equals("200")){
         //   Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()

            CommonDialog.showDialogForSingleButton(activity!!,object : DialogClickCallback{
                override fun onConfirm() {
                    AppData.registrationModelData = RegistrationModel()
                    AppData.boolSForRefreshLayout = true
                    (activity as LoginActivity?)!!.setCurrentItem(0, true)
                }

                override fun onDismiss() {
                }

            },"Registration", registrationResponse?.message!!)

        }else{
            Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?) {
        baseActivity?.hideLoading()
        if (departmentListResponse?.code.equals("200")){
            if(departmentListResponse?.result!=null && departmentListResponse.result.size>0){
                departmentList=ArrayList<ResultItem?>()
                departmentList= departmentListResponse.result
            }
        }
    }

    override fun errorRegistrationResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    //
    private fun registrationApiCall(){
        baseActivity?.showLoading()

        val user_type = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.userType?.toLowerCase())
        val first_name = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.firstName)
        val last_name = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.lastName)
        val email = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.emailAddress)
        val mobile_number = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.mobileNumber)
        val dob = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.dob)
        val gender = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.gender)
        val password = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.password)
        val confirm_password = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.confirmPassword)

        var imageMultipartBody:MultipartBody.Part?=null
        if (AppData.registrationModelData?.imageFile != null) {
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.imageFile)
            imageMultipartBody = MultipartBody.Part.createFormData("image", AppData.registrationModelData?.imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
        }
        /*var certificateMultipartBody:MultipartBody.Part?=null
        if (AppData?.registrationModelData?.certificateFile != null) {
            val certificate = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.certificateFile)
            certificateMultipartBody = MultipartBody.Part.createFormData("certificate", AppData?.registrationModelData?.certificateFile?.name, certificate)

        }*/
        val qualification = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.qualification)
        val passing_year = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.passingYear)
        val institute = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.institude)
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.description)
        val experience = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.experience)
        val available_time = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.availableTime)
        val fees = RequestBody.create(MediaType.parse("multipart/form-data"), AppData.registrationModelData?.fees)
        var department:RequestBody?=null
        if ((activity as LoginActivity).isdeaprtmentMandatory!!){
            department = RequestBody.create(MediaType.parse("multipart/form-data"), departmentId)
        }
        val certificateMultipartBody:MutableList<MultipartBody.Part> = ArrayList()
        if (AppData.registrationModelData?.qualificationDataList != null && AppData.registrationModelData?.qualificationDataList?.size!!>0) {
            var index = 0
            for (item in AppData.registrationModelData?.qualificationDataList!!){
                if (item.certificateFileTemporay!=null) {
                    val certificate = RequestBody.create(MediaType.parse("multipart/form-data"), item.certificateFileTemporay!!)
                    certificateMultipartBody.add(MultipartBody.Part.createFormData("certificate[]", item.certificateFileTemporay?.name, certificate))
                    index++
                }
            }
        }


        fragmentRegistrationStepThreeViewModel?.apieditpatientprofilepersonal(user_type,first_name,last_name,email,
            mobile_number,dob,gender,password,confirm_password,imageMultipartBody,
            certificateMultipartBody,qualification,passing_year,institute,description,experience,available_time,fees,department)

    }

    override fun onResume() {
        super.onResume()
        if (AppData.boolSForRefreshLayout) {
            fragmentRegistrationStepthreeBinding?.edtRegDescription?.setText("")
            fragmentRegistrationStepthreeBinding?.edtRegExperience?.setText("")
            fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.setText("")
            fragmentRegistrationStepthreeBinding?.edtRegFees?.setText("")
            fragmentRegistrationStepthreeBinding?.txtRegDepartment?.text = ""
        }
    }
}