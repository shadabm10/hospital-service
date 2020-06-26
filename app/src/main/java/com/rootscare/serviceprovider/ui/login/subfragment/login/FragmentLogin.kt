package com.rootscare.serviceprovider.ui.login.subfragment.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.data.model.api.request.loginrequest.LoginRequest
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLoginBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.home.LabTechnicianHomeActivity
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.regex.Pattern


class FragmentLogin : BaseFragment<FragmentLoginBinding, FragmentLoginViewModel>(), FragmentLoginNavigator{
    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private var fragmentLoginViewModel: FragmentLoginViewModel? = null
    private var isLoginRemeberCheck=false

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_login
    override val viewModel: FragmentLoginViewModel
        get() {
            fragmentLoginViewModel = ViewModelProviders.of(this).get(FragmentLoginViewModel::class.java!!)
            return fragmentLoginViewModel as FragmentLoginViewModel
        }

    companion object {
        val TAG = FragmentLogin::class.java.simpleName
        fun newInstance(): FragmentLogin {
            val args = Bundle()
            val fragment = FragmentLogin()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLoginViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLoginBinding = viewDataBinding
        fragmentLoginViewModel?.appSharedPref?.isloginremember="false"
        fragmentLoginBinding?.checkboxLoginremember?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isLoginRemeberCheck=true
                fragmentLoginViewModel?.appSharedPref?.isloginremember="true"
                //Do Whatever you want in isChecked
            }else{
                isLoginRemeberCheck=false
                fragmentLoginViewModel?.appSharedPref?.isloginremember="false"
            }
        }


        fragmentLoginBinding?.txtForgotPassword?.setOnClickListener(View.OnClickListener {
            (activity as LoginActivity?)!!.setCurrentItem(4, true)
        })

        var dropdownlist= ArrayList<String?>()
        dropdownlist.add("Doctor")
        dropdownlist.add("Nurse")
        dropdownlist.add("Caregiver")
        dropdownlist.add("Hospital")
        dropdownlist.add("Babysitter")
        dropdownlist.add("Physiotherapy")
        dropdownlist.add("Lab-Technician")
        fragmentLoginBinding?.txtRootscareLoginUserType?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Select User Type",dropdownlist,object:
                DropDownDialogCallBack{
                override fun onConfirm(text: String) {
                    fragmentLoginBinding?.txtRootscareLoginUserType?.setText(text)
                }

            })
        })


        fragmentLoginBinding?.btnRootscareLogin?.setOnClickListener(View.OnClickListener {

            val useremail=fragmentLoginBinding?.edtRootscareProviderEmail?.text?.toString()
            val password= fragmentLoginBinding?.edtRootscareProviderPassword?.text?.toString()

                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

                    if(checkValidation(useremail!!, password!!)) {
                        val requestUserLogin = LoginRequest()
                        requestUserLogin.userType=fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString()?.toLowerCase()
                        requestUserLogin.email = useremail
                        requestUserLogin.password = password
                        hideKeyboard()
                        if(isNetworkConnected){
                            baseActivity?.showLoading()
                            fragmentLoginViewModel!!.apiserviceproviderlogin(requestUserLogin)
                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }

                    }




        })
    }

    //Validation checking for email and password
    private fun checkValidation(email: String,password: String): Boolean {
        if (fragmentLoginBinding?.txtRootscareLoginUserType?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select user type for login!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if(email.equals("")){
            Toast.makeText(activity, "Please enter your email!", Toast.LENGTH_SHORT).show()
            return false;
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPassword(password) ) {
            Toast.makeText(activity, "Please enter Password!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        return true
    }
    //Password validation method
    fun isValidPassword(s: String): Boolean {
        val PASSWORD_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}")

//        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches()

        return !TextUtils.isEmpty(s)
    }

    override fun successLoginResponse(loginResponse: LoginResponse?) {
        baseActivity?.hideLoading()

//        "message": "User Type must be from the list. (doctor,hospital,nurse,caregiver,babysitter,physiotherapy,lab-technician)",
        if(loginResponse?.code.equals("200")){
//            startActivity(activity?.let { HomeActivity.newIntent(it) })
//            activity?.finish()
            if(loginResponse?.result!=null){
                if(loginResponse?.result?.userType?.toLowerCase().equals("doctor")){
                    startActivity(activity?.let { HomeActivity.newIntent(it) })
                    activity?.finish()
                }else if(loginResponse?.result?.userType?.toLowerCase().equals("nurse")){
                    startActivity(activity?.let { NursrsHomeActivity.newIntent(it) })
                    activity?.finish()
                }else if(loginResponse?.result?.userType?.toLowerCase().equals("caregiver")){
                    startActivity(activity?.let { CaregiverHomeActivity.newIntent(it) })
                    activity?.finish()
                }
                else if(loginResponse?.result?.userType?.toLowerCase().equals("babysitter")){
                    startActivity(activity?.let { CaregiverHomeActivity.newIntent(it) })
                    activity?.finish()
                }

                else if(loginResponse?.result?.userType?.toLowerCase().equals("hospital")){
                    startActivity(activity?.let { HospitalHomeActivity.newIntent(it) })
                    activity?.finish()
                }

                else if(loginResponse?.result?.userType?.toLowerCase().equals("lab-technician")){
                    startActivity(activity?.let { LabTechnicianHomeActivity.newIntent(it) })
                    activity?.finish()
                }

                else if(loginResponse?.result?.userType?.toLowerCase().equals("Physiotherapy")){
                    startActivity(activity?.let { PhysiotherapyHomeActivity.newIntent(it) })
                    activity?.finish()
                }
            }else{
                Toast.makeText(activity, loginResponse?.message, Toast.LENGTH_SHORT).show()
            }









        }else{
            Toast.makeText(activity, loginResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorLoginResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}