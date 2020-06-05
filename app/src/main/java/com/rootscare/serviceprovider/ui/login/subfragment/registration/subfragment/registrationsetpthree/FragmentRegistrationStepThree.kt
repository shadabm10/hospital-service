package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse
import com.rootscare.interfaces.DialogClickCallback
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
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_stepthree
    override val viewModel: FragmentRegistrationStepThreeViewModel
        get() {
            fragmentRegistrationStepThreeViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepThreeViewModel::class.java!!)
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
        fragmentRegistrationStepthreeBinding?.btnRooscareServiceproviderRegistrationSubmit?.setOnClickListener(
            View.OnClickListener {
                if(checkValidationForRegStepThree()){
                    AppData?.registrationModelData?.description=fragmentRegistrationStepthreeBinding?.edtRegDescription?.text?.toString()
                    AppData?.registrationModelData?.experience=fragmentRegistrationStepthreeBinding?.edtRegExperience?.text?.toString()
                    AppData?.registrationModelData?.availableTime=fragmentRegistrationStepthreeBinding?.edtRootscareRegistrationAvailableTime?.text?.toString()
                    AppData?.registrationModelData?.fees=fragmentRegistrationStepthreeBinding?.edtRegFees?.text?.toString()
                    AppData?.registrationModelData?.department=fragmentRegistrationStepthreeBinding?.edtRegDepartment?.text?.toString()
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        registrationApiCall()
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }

                }
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
        if (fragmentRegistrationStepthreeBinding?.edtRegDepartment?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your department!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        return true
    }

    override fun successRegistrationResponse(registrationResponse: RegistrationResponse?) {
        baseActivity?.hideLoading()
        if(registrationResponse?.code.equals("200")){
            Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()

            CommonDialog.showDialog(this!!.activity!!,object : DialogClickCallback{
                override fun onConfirm() {
                    AppData.registrationModelData= RegistrationModel()
                    (activity as LoginActivity?)!!.setCurrentItem(0, true)
                }

                override fun onDismiss() {
                }

            },"Registration", registrationResponse?.message!!)


        }else{
            Toast.makeText(activity, registrationResponse?.message, Toast.LENGTH_SHORT).show()
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
        val user_type = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.userType?.toLowerCase())
        val first_name = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.firstName)
        val last_name = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.lastName)
        val email = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.emailAddress)
        val mobile_number = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.mobileNumber)
        val dob = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.dob)
        val gender = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.gender)
        val password = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.password)
        val confirm_password = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.confirmPassword)

        var imageMultipartBody:MultipartBody.Part?=null
        if (AppData?.registrationModelData?.imageFile != null) {
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.imageFile)
            imageMultipartBody = MultipartBody.Part.createFormData("image", AppData?.registrationModelData?.imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
        }
        var certificateMultipartBody:MultipartBody.Part?=null
        if (AppData?.registrationModelData?.certificateFile != null) {
            val certificate = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.certificateFile)
            certificateMultipartBody = MultipartBody.Part.createFormData("certificate", AppData?.registrationModelData?.certificateFile?.name, certificate)
        }
        val qualification = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.qualification)
        val passing_year = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.passingYear)
        val institute = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.institude)
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.description)
        val experience = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.experience)
        val available_time = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.availableTime)
        val fees = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.fees)
        val department = RequestBody.create(MediaType.parse("multipart/form-data"), AppData?.registrationModelData?.department)

        fragmentRegistrationStepThreeViewModel?.apieditpatientprofilepersonal(user_type,first_name,last_name,email,
            mobile_number,dob,gender,password,confirm_password,imageMultipartBody,
            certificateMultipartBody!!,qualification,passing_year,institute,description,experience,available_time,fees,department)

    }

}