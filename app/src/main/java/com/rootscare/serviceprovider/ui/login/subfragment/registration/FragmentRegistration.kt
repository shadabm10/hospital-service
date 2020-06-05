package com.rootscare.serviceprovider.ui.login.subfragment.registration

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.myfilepickesexample.FileUtil.from
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.utils.ManagePermissions
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.io.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class FragmentRegistration : BaseFragment<FragmentRegistrationBinding, FragmentRegistrationviewModel>(),
    FragmentRegistrationNavigator {
    private var fragmentRegistrationBinding: FragmentRegistrationBinding? = null
    private var fragmentRegistrationviewModel: FragmentRegistrationviewModel? = null

    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    val PICKFILE_RESULT_CODE = 4
    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var selectedGender=""
    var imageFile: File? = null
    var certificatefileFile: File? = null
    var monthOfDob: String=""
    var dayOfDob: String=""
    var choosenYear = 1990
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration
    override val viewModel: FragmentRegistrationviewModel
        get() {
            fragmentRegistrationviewModel = ViewModelProviders.of(this).get(FragmentRegistrationviewModel::class.java!!)
            return fragmentRegistrationviewModel as FragmentRegistrationviewModel
        }
    companion object {
        val TAG = FragmentRegistration::class.java.simpleName
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(): FragmentRegistration {
            val args = Bundle()
            val fragment = FragmentRegistration()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationviewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationBinding = viewDataBinding

        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()




        var dropdownlist= ArrayList<String?>()
        dropdownlist.add("Doctor")
        dropdownlist.add("Nurses")
        dropdownlist.add("Caregiver")
        dropdownlist.add("Hospital")
        dropdownlist.add("Babysitter")
        dropdownlist.add("Physiotherapy")
        dropdownlist.add("Lab Technician")
        fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Select User Type",dropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.setText(text)
                }

            })
        })

        fragmentRegistrationBinding?.radioBtnRegFemale?.setOnClickListener(View.OnClickListener {
            selectedGender="Female"
        })

        fragmentRegistrationBinding?.radioBtnRegMale?.setOnClickListener(View.OnClickListener {
            selectedGender="Male"
        })

        //Select Dob

        fragmentRegistrationBinding?.txtRegDob?.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this!!.activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                if((monthOfYear+1)<10){
                    monthOfDob= "0" + (monthOfYear+1)
                }else{
                    monthOfDob=(monthOfYear+1).toString()
                }

                if(dayOfMonth<10){
                    dayOfDob="0"+dayOfMonth

                }else{
                    dayOfDob=dayOfMonth.toString()
                }
                fragmentRegistrationBinding?.txtRegDob?.setText("" + year + "-" + monthOfDob+ "-" + dayOfDob)
            }, year, month, day)

            dpd.show()
        })



        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSteponeContinue?.setOnClickListener(
            View.OnClickListener {

                if(checkValidationForRegStepOne()){
                    AppData.registrationModelData?.userType=fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.text?.toString()
                    AppData.registrationModelData?.firstName=fragmentRegistrationBinding?.edtRegFirstname?.text?.toString()
                    AppData.registrationModelData?.lastName=fragmentRegistrationBinding?.edtRegLastname?.text?.toString()
                    AppData.registrationModelData?.emailAddress=fragmentRegistrationBinding?.edtRegEmailaddress?.text?.toString()
                    AppData.registrationModelData?.mobileNumber=fragmentRegistrationBinding?.edtRegPhonenumber?.text?.toString()
                    AppData.registrationModelData?.dob=fragmentRegistrationBinding?.txtRegDob?.text?.toString()
                    AppData.registrationModelData?.gender=selectedGender
                    AppData.registrationModelData?.password=fragmentRegistrationBinding?.edtRegPassword?.text?.toString()
                    AppData.registrationModelData?.confirmPassword=fragmentRegistrationBinding?.edtRegConfirmPassword?.text?.toString()
                    (activity as LoginActivity?)!!.setCurrentItem(2, true)
                }


            })



    }





    //Start of All field Validation
    private fun checkValidationForRegStepOne(): Boolean {
        if (fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select user type for registration!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationBinding?.edtRegFirstname?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your first name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationBinding?.edtRegLastname?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your last name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (fragmentRegistrationBinding?.edtRegEmailaddress?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your email!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var email=fragmentRegistrationBinding?.edtRegEmailaddress?.text?.toString()
        if (!email!!.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPassword(fragmentRegistrationBinding?.edtRegPhonenumber?.text?.toString()!!) ) {
            Toast.makeText(activity, "Please enter Password!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (fragmentRegistrationBinding?.txtRegDob?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select your dob!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (selectedGender.equals("") ) {
            Toast.makeText(activity, "Please select gender!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationBinding?.edtRegPassword?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your password!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationBinding?.edtRegConfirmPassword?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your confirm password!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (!fragmentRegistrationBinding?.edtRegPassword?.text?.toString().equals(fragmentRegistrationBinding?.edtRegConfirmPassword?.text?.toString()) ) {
            Toast.makeText(activity, "Password and confirm password not matched!", Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        super.onResume()
        fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.setText(AppData?.registrationModelData?.userType)
        fragmentRegistrationBinding?.txtRegDob?.setText(AppData?.registrationModelData?.dob)
        if (AppData?.registrationModelData?.gender.equals("Female")){
            selectedGender= AppData?.registrationModelData?.gender!!
            fragmentRegistrationBinding?.radioBtnRegFemale?.isChecked=true

        }else if(AppData?.registrationModelData?.gender.equals("Male")){
            selectedGender= AppData?.registrationModelData?.gender!!
            fragmentRegistrationBinding?.radioBtnRegMale?.isChecked=true
        }
    }





}