package com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.dialog.CommonDialog
import com.myfilepickesexample.FileUtil
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.api.response.deaprtmentlist.ResultItem
import com.rootscare.data.model.api.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.api.response.doctor.profileresponse.QualificationDataItem
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.data.model.api.response.registrationresponse.RegistrationResponse
import com.rootscare.dialog.certificateupload.CertificateUploadFragment
import com.rootscare.interfaces.OnDepartmentDropDownListItemClickListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorEditProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.adapter.CertificateListAdapter
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import kotlinx.android.synthetic.main.fragment_doctor_edit_profile.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class FragmentEditDoctorProfile :
    BaseFragment<FragmentDoctorEditProfileBinding, FragmentEditDoctorProfileViewModel>(),
    FragmentEditDoctorProfileNavigator {

    private val TAG = "FragmentEditDoctorProfi"
    private var certificateListAdapter: CertificateListAdapter? = null

    private val IMAGE_DIRECTORY = "/demonuts"
    private val PICKFILE_RESULT_CODE = 4
    private val GALLERY = 1
    private val CAMERA = 2

    private var departmentList: ArrayList<ResultItem?>? = ArrayList()
    private var departmentId = ""
    private var departTitle = ""
    private var choosenYear = 1980
    private var selectedGender = ""

    private var monthOfDob: String = ""
    private var dayOfDob: String = ""

    //    private var yearOfPassingToSubmit: String? = null
//    private var dobToSubmit: String? = null
    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var certificatefileFile: File? = null
    private var imageFile: File? = null
    private var userType:String?=null

    private var fragmentDoctorEditProfileBinding: FragmentDoctorEditProfileBinding? = null
    private var fragmentEditDoctorProfileViewModel: FragmentEditDoctorProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_edit_profile
    override val viewModel: FragmentEditDoctorProfileViewModel
        get() {
            fragmentEditDoctorProfileViewModel = ViewModelProviders.of(this).get(
                FragmentEditDoctorProfileViewModel::class.java
            )
            return fragmentEditDoctorProfileViewModel as FragmentEditDoctorProfileViewModel
        }

    companion object {
        fun newInstance(userType:String): FragmentEditDoctorProfile {
            val args = Bundle()
            args.putString("userType",userType)
            val fragment = FragmentEditDoctorProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentEditDoctorProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorEditProfileBinding = viewDataBinding

        userType = arguments?.getString("userType")

        with(fragmentDoctorEditProfileBinding!!) {

            addCertificatePortionsSetUp()


            textViewDepartment.setOnClickListener(View.OnClickListener {
                CommonDialog.showDialogForDeaprtmentDropDownList(
                    activity!!,
                    departmentList,
                    "Select Department",
                    object :
                        OnDepartmentDropDownListItemClickListener {
                        override fun onConfirm(departmentList: ArrayList<ResultItem?>?) {
                            departTitle = ""
                            departmentId = ""
                            var p = 0
                            for (i in 0 until departmentList?.size!!) {
                                if (departmentList.get(i)?.isChecked.equals("true")) {
                                    if (p == 0) {
                                        departTitle = departmentList.get(i)?.title!!
                                        departmentId = departmentList.get(i)?.id!!
                                        p++

//                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
//                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
                                    } else {
                                        departTitle =
                                            departTitle + "," + departmentList.get(i)?.title
                                        departmentId =
                                            departmentId + "," + departmentList.get(i)?.id

//                                Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
//                                Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")
                                    }
                                }
                            }

                            Log.d(FragmentLogin.TAG, "--SELECT DEPARTMENT:-- ${departTitle}")
                            Log.d(FragmentLogin.TAG, "--SELECT ID:-- ${departmentId}")

                            textViewDepartment.setText(departTitle)
                        }

                    })
            })

            /*textViewPassingYear.setOnClickListener(View.OnClickListener {
                val builder = MonthPickerDialog.Builder(
                    activity,
                    MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
                        textViewPassingYear.setText(selectedYear.toString())
                    },
                    choosenYear,
                    0
                );
                builder.showYearOnly()
                    .setYearRange(1980, 2090)
                    .build()
                    .show();
            })*/


            textViewDOB.setOnClickListener(View.OnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    activity!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        // Display Selected date in textbox
                        // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                        if ((monthOfYear + 1) < 10) {
                            monthOfDob = "0" + (monthOfYear + 1)
                        } else {
                            monthOfDob = (monthOfYear + 1).toString()
                        }

                        if (dayOfMonth < 10) {
                            dayOfDob = "0" + dayOfMonth

                        } else {
                            dayOfDob = dayOfMonth.toString()
                        }
//                        dobToSubmit = "" + year + "-" + monthOfDob + "-" + dayOfDob
                        textViewDOB.setText("" + year + "-" + monthOfDob + "-" + dayOfDob)
                    },
                    year,
                    month,
                    day
                )

                dpd.show()
            })


            //File Selection Button Click
//            textViewCertificate.setOnClickListener(View.OnClickListener {
//                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
//                chooseFile.type = "*/*"
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file")
//                startActivityForResult(
//                    chooseFile,
//                    PICKFILE_RESULT_CODE
//                )
//            })

            //Image Selection Button Click
            imgDoctorProfile.setOnClickListener(View.OnClickListener {
                showPictureDialog()
            })

            radioYesOrNo.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    if (checkedId == R.id.radioBtnMale) {
                        selectedGender = "Male"
                    }
                    if (checkedId == R.id.radioBtnFemale) {
                        selectedGender = "Female"
                    }
                    /*if (checkedId == R.id.radioBtnOther) {
                        selectedGender = "Other"
                    }*/
                }
            })
            radioYesOrNo.radioBtnMale.isChecked = true

            btnSubmit.setOnClickListener {
                submitDetailsForEditProfile()
            }

        }



        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentEditDoctorProfileViewModel?.apidepartmentlist()
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
        fetchDOCUserDetails()

    }

    private fun fetchDOCUserDetails() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var commonUserIdRequest = CommonUserIdRequest()
            commonUserIdRequest.id = fragmentEditDoctorProfileViewModel?.appSharedPref?.loginUserId
            fragmentEditDoctorProfileViewModel!!.apidoctorprofile(commonUserIdRequest, userType)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?) {
        baseActivity?.hideLoading()
        if (departmentListResponse?.code.equals("200")) {
            if (departmentListResponse?.result != null && departmentListResponse.result.size > 0) {
                departmentList = ArrayList()
                departmentList = departmentListResponse.result
            }
        }
    }

    override fun onSuccessEditProfile(response: LoginResponse) {
//        baseActivity?.hideLoading()
        certificateListAdapter?.qualificationDataList?.clear()
        Handler().postDelayed({
            fetchDOCUserDetails()
        }, 500)
    }

    override fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?) {
        baseActivity?.hideLoading()
        if (getDoctorProfileResponse?.code.equals("200")) {
            if (getDoctorProfileResponse?.result != null) {
                with(fragmentDoctorEditProfileBinding!!) {
                    if (getDoctorProfileResponse.result.firstName != null && !getDoctorProfileResponse.result.firstName.equals("")) {
                        ediitextFirstName.setText(getDoctorProfileResponse.result.firstName)
                    } else {
                        ediitextFirstName.setText("")
                    }
                    if (getDoctorProfileResponse.result.lastName != null && !getDoctorProfileResponse.result.lastName.equals("")) {
                        ediitextLastName.setText(getDoctorProfileResponse.result.lastName)
                    } else {
                        ediitextLastName.setText("")
                    }
                    if (getDoctorProfileResponse.result.firstName != null && !getDoctorProfileResponse.result.firstName.equals("") && getDoctorProfileResponse.result.lastName != null && !getDoctorProfileResponse.result.lastName.equals(
                            ""
                        )
                    ) {
                        textViewDocNameTilte.setText(getDoctorProfileResponse.result.firstName + " " + getDoctorProfileResponse.result.lastName)
                    } else {
                        textViewDocNameTilte.setText("")
                    }

                    if (getDoctorProfileResponse.result.email != null && !getDoctorProfileResponse.result.email.equals("")) {
                        ediitextEmail.setText(getDoctorProfileResponse.result.email)
                        textViewDocEmailTilte.setText(getDoctorProfileResponse.result.email)
                    } else {
                        ediitextEmail.setText("")
                        textViewDocEmailTilte.setText("")
                    }
                    if (getDoctorProfileResponse.result.image != null && !getDoctorProfileResponse.result.image.equals("")) {
                        val options: RequestOptions =
                            RequestOptions()
                                .centerCrop()
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.profile_no_image)
                                .priority(Priority.HIGH)
                        Glide
                            .with(activity!!)
                            .load(getString(R.string.api_base) + "uploads/images/" + getDoctorProfileResponse.result.image)
                            .apply(options)
                            .into(imgDoctorProfile)
                    }
                    if (getDoctorProfileResponse.result.dob != null && !getDoctorProfileResponse.result.dob.equals("")) {
                        textViewDOB.setText(getDoctorProfileResponse.result.dob)
                    } else {
                        textViewDOB.setText("")
                    }
                    if (getDoctorProfileResponse.result.phoneNumber != null && !getDoctorProfileResponse.result.phoneNumber.equals("")) {
                        ediitextMobileNumber.setText(getDoctorProfileResponse.result.phoneNumber)
                    } else {
                        ediitextMobileNumber.setText("")
                    }
                    if (getDoctorProfileResponse.result.gender != null && !getDoctorProfileResponse.result.gender.equals("")) {
                        if (getDoctorProfileResponse.result.gender.trim().toLowerCase().equals("male")) {
                            radioYesOrNo.check(R.id.radioBtnMale)
                            selectedGender = "Male"
                        } else if (getDoctorProfileResponse.result.gender.trim().toLowerCase().equals("female")) {
                            radioYesOrNo.check(R.id.radioBtnFemale)
                            selectedGender = "Female"
                        }/*else{
                            radioYesOrNo.check(R.id.radioBtnOther)
                            selectedGender = "Other"
                        }*/
                    }
                    /*if(getDoctorProfileResponse.result.qualification!=null && !getDoctorProfileResponse.result.qualification.equals("")){
                        ediitextQualification.setText(getDoctorProfileResponse.result.qualification)
                    }else{
                        ediitextQualification.setText("")
                    }
                    if(getDoctorProfileResponse.result.passingYear!=null && !getDoctorProfileResponse.result.passingYear.equals("")){
                        textViewPassingYear.setText(getDoctorProfileResponse.result.passingYear)
                    }else{
                        textViewPassingYear.setText("")
                    }
                    if(getDoctorProfileResponse.result.institute!=null && !getDoctorProfileResponse.result.institute.equals("")){
                        ediitextInstitute.setText(getDoctorProfileResponse.result.institute)
                    }else{
                        ediitextInstitute.setText("")
                    }*/
                    if (getDoctorProfileResponse.result.description != null && !getDoctorProfileResponse.result.description.equals("")) {
                        ediitextDescription.setText(getDoctorProfileResponse.result.description)
                    } else {
                        ediitextDescription.setText("")
                    }
                    if (getDoctorProfileResponse.result.experience != null && !getDoctorProfileResponse.result.experience.equals("")) {
                        ediitextExperience.setText(getDoctorProfileResponse.result.experience)
                    } else {
                        ediitextExperience.setText("")
                    }
                    if (getDoctorProfileResponse.result.availableTime != null && !getDoctorProfileResponse.result.availableTime.equals("")) {
                        ediitexAvailableTime.setText(getDoctorProfileResponse.result.availableTime)
                    } else {
                        ediitexAvailableTime.setText("")
                    }
                    if (userType.equals("nurse")){
                        if (getDoctorProfileResponse.result.dailyRate != null && !getDoctorProfileResponse.result.dailyRate.equals("")) {
                            ediitextFees.setText(getDoctorProfileResponse.result.dailyRate)
                        } else {
                            ediitextFees.setText("")
                        }
                    }else{
                        if (getDoctorProfileResponse.result.fees != null && !getDoctorProfileResponse.result.fees.equals("")) {
                            ediitextFees.setText(getDoctorProfileResponse.result.fees)
                        } else {
                            ediitextFees.setText("")
                        }
                    }

                    if (getDoctorProfileResponse.result.department != null && getDoctorProfileResponse.result.department.size > 0) {
                        textViewDepartment.setText("")
                        departTitle = ""
                        departmentId = ""
                        for (i in 0 until getDoctorProfileResponse.result.department.size) {
                            if (getDoctorProfileResponse.result.department[i]?.title!=null &&
                                    getDoctorProfileResponse.result.department[i]?.id!=null){
                                if (i == 0) {
                                    departTitle += getDoctorProfileResponse.result.department[i]?.title
                                    departmentId += getDoctorProfileResponse.result.department[i]?.id
                                } else {
                                    departTitle += "," + getDoctorProfileResponse.result.department[i]?.title
                                    departmentId += "," + getDoctorProfileResponse.result.department[i]?.id
                                }
                            }
                            textViewDepartment.setText(departTitle)
                            for (j in 0 until departmentList?.size!!) {
                                if (getDoctorProfileResponse.result.department[i]?.id.equals(departmentList!![j]?.id)) {
                                    departmentList!![j]?.isChecked = "true"
                                }
                            }
                        }
                    } else {
                        textViewDepartment.setText("")
                    }
                    /*if(getDoctorProfileResponse.result.qualificationCertificate!=null && !getDoctorProfileResponse.result.qualificationCertificate.equals("")){
                        textViewCertificate.setText(getDoctorProfileResponse.result.qualificationCertificate)
                    }else{
                        textViewCertificate.setText("")
                    }*/

                    if (getDoctorProfileResponse.result.qualificationData != null && getDoctorProfileResponse.result.qualificationData.size > 0) {
                        for (item in getDoctorProfileResponse.result.qualificationData) {
                            item.isOldData = true
                        }
                        certificateListAdapter?.qualificationDataList?.addAll(getDoctorProfileResponse.result.qualificationData)
                        certificateListAdapter?.notifyDataSetChanged()
                    }

                }

            }
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
//            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    bitmapToFile(bitmap)
                    Glide.with(activity!!).load(bitmap).apply(RequestOptions.circleCropTransform())
                        .into(fragmentDoctorEditProfileBinding!!.imgDoctorProfile)
//                    Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

                } catch (e: IOException) {
                    e.printStackTrace()
//                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }


        } else if (requestCode == CAMERA) {
//            val contentURI = data!!.data
            val thumbnail = data?.extras?.get("data") as Bitmap
            saveImage(thumbnail)
            bitmapToFile(thumbnail)
            Glide.with(activity!!).load(thumbnail).apply(RequestOptions.circleCropTransform())
                .into(fragmentDoctorEditProfileBinding!!.imgDoctorProfile)
//            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()


        } else if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == -1) {
                fileUri = data!!.data
                filePath = fileUri!!.path
                // tvItemPath.setText(filePath)
                try {
                    val file = FileUtil.from(this!!.activity!!, fileUri!!)
                    certificatefileFile = file
                    Log.d(
                        "file",
                        "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists()
                    )
                    /*fragmentDoctorEditProfileBinding?.textViewCertificate?.setText(
                        getFileName(this!!.activity!!, fileUri!!)
                    )*/
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor =
                context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
        )

        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg")
            )
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");


            f.createNewFile()
            // imageFile=f

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())


            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(activity)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        imageFile = file
        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //updateProfileImageApiCall(imageFile!!)
        // Return the saved bitmap uri
//        fragmentRegistrationStepTwoBinding?.txtImageSelectName?.setText(file.name)
        return Uri.parse(file.absolutePath)


    }


    private fun submitDetailsForEditProfile() {
        with(fragmentDoctorEditProfileBinding!!) {
            if (fragmentEditDoctorProfileViewModel?.appSharedPref?.loginUserId != null && checkValidationForRegStepOne()) {
                baseActivity?.showLoading()
                baseActivity?.hideKeyboard()

                val user_id = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    fragmentEditDoctorProfileViewModel?.appSharedPref?.loginUserId!!
                )
                val first_name = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextFirstName.text?.trim().toString()
                )
                val last_name = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextLastName.text?.trim().toString()
                )
                val email = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextEmail.text?.trim().toString()
                )
                val mobile_number = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextMobileNumber.text?.trim().toString()
                )
                val dob = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    textViewDOB.text?.trim().toString()
                )
                val gender = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    selectedGender
                )
                /*val qualification = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextQualification.text?.trim().toString()
                )
                val passingYear = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    textViewPassingYear.text?.trim().toString()
                )
                val institute = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextInstitute.text?.trim().toString()
                )*/
                var qualificationStr = ""
                var passingYearStr = ""
                var instituteStr = ""
                if (certificateListAdapter?.qualificationDataList != null && certificateListAdapter?.qualificationDataList!!.size > 0) {
                    val tempList: ArrayList<QualificationDataItem> = ArrayList()
                    for (i in 0 until certificateListAdapter?.qualificationDataList?.size!!) {
                        if (!certificateListAdapter?.qualificationDataList!![i].isOldData){
                            tempList.add(certificateListAdapter?.qualificationDataList!![i])
                        }
                    }
                    for (i in 0 until tempList.size) {
                        if (i == 0 ) {
                            qualificationStr += tempList[i].qualification!!
                            passingYearStr += tempList[i].passingYear!!
                            instituteStr += tempList[i].institute!!
                        } else {
                            qualificationStr += ",${tempList[i].qualification!!}"
                            passingYearStr += ",${tempList[i].passingYear!!}"
                            instituteStr += ",${tempList[i].institute!!}"
                        }

                    }
                    /*for (i in 0 until certificateListAdapter?.qualificationDataList!!.size) {
                        if (i == 0 ) {
                            qualificationStr += certificateListAdapter?.qualificationDataList!![i].qualification!!
                            passingYearStr += certificateListAdapter?.qualificationDataList!![i].passingYear!!
                            instituteStr += certificateListAdapter?.qualificationDataList!![i].institute!!
                        } else {
                            qualificationStr += ",${certificateListAdapter?.qualificationDataList!![i].qualification!!}"
                            passingYearStr += ",${certificateListAdapter?.qualificationDataList!![i].passingYear!!}"
                            instituteStr += ",${certificateListAdapter?.qualificationDataList!![i].institute!!}"
                        }

                    }*/
                }
                var qualification:RequestBody?=null
                if (!TextUtils.isEmpty(qualificationStr.trim())) {
                    qualification = RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        qualificationStr
                    )
                }
                var passingYear:RequestBody?=null
                if (!TextUtils.isEmpty(qualificationStr.trim())) {
                    passingYear = RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        passingYearStr
                    )
                }
                var institute:RequestBody?=null
                if (!TextUtils.isEmpty(qualificationStr.trim())) {
                    institute = RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        instituteStr
                    )
                }
                val description = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextDescription.text?.trim().toString()
                )
                val experience = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextExperience.text?.trim().toString()
                )
                val availableTime = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitexAvailableTime.text?.trim().toString()
                )
                val fees = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    ediitextFees.text?.trim().toString()
                )
                val department = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    departmentId.trim().toString()
                )

                var imageMultipartBody: MultipartBody.Part? = null
                var image:RequestBody?=null
                if (imageFile != null){
                    image= RequestBody.create(MediaType.parse("multipart/form-data"), imageFile!!)
                    imageMultipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
                }else{
//                    image = RequestBody.create(MediaType.parse("multipart/form-data"), "")
//                    imageMultipartBody = MultipartBody.Part.createFormData("image", "", image)
                }

                var certificateMultipartBody: ArrayList<MultipartBody.Part>?=null
                if ((certificateListAdapter?.qualificationDataList != null && certificateListAdapter?.qualificationDataList?.size!! > 0)) {
                    val tempList: ArrayList<QualificationDataItem> = ArrayList()
                    for (i in 0 until certificateListAdapter?.qualificationDataList?.size!!) {
                        if (!certificateListAdapter?.qualificationDataList!![i].isOldData) {
                            tempList.add(certificateListAdapter?.qualificationDataList!![i])
                        }
                    }
                    if (tempList.size > 0) {
                        certificateMultipartBody = ArrayList()
                        for (item in tempList) {
                            if (item.certificateFileTemporay != null && !item.isOldData) {
                                val certificate = RequestBody.create(MediaType.parse("multipart/form-data"), item.certificateFileTemporay!!)
                                certificateMultipartBody.add(
                                    MultipartBody.Part.createFormData(
                                        "certificate[]",
                                        item.certificateFileTemporay?.name,
                                        certificate
                                    )
                                )
                            }
                        }
                    }
                }

                fragmentEditDoctorProfileViewModel?.apiHitForUdateProfileWithProfileAndCertificationImage(
                    user_id,
                    first_name,
                    last_name,
                    email,
                    mobile_number,
                    dob,
                    gender,
                    qualification,
                    passingYear,
                    institute,
                    description,
                    experience,
                    availableTime,
                    fees,
                    department,
                    imageMultipartBody,
                    certificateMultipartBody
                )
            }
        }
    }

    //Start of All field Validation
    private fun checkValidationForRegStepOne(): Boolean {
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextFirstName?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your first name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextLastName?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your last name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextEmail?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your email!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var email = fragmentDoctorEditProfileBinding?.ediitextEmail?.text?.toString()
        if (!email!!.matches(emailPattern.toRegex())) {
//            activityLoginBinding?.edtEmailOrPhone?.setError("Please enter valid email id")
            Toast.makeText(activity, "Please enter valid email id!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.textViewDOB?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please select your dob!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (TextUtils.isEmpty(selectedGender.trim())) {
            Toast.makeText(activity, "Please select gender!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextMobileNumber?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your Mobile Number!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextDescription?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your description!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextExperience?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your experience!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitexAvailableTime?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter available time!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(
                fragmentDoctorEditProfileBinding?.ediitextFees?.text?.toString()?.trim()
            )
        ) {
            Toast.makeText(activity, "Please enter your fees!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (TextUtils.isEmpty(departmentId.trim())) {
            Toast.makeText(activity, "Please enter your department!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        return true
    }


    private fun addCertificatePortionsSetUp() {
        with(fragmentDoctorEditProfileBinding!!) {
            recyclerViewCertificates.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
            certificateListAdapter = CertificateListAdapter(activity!!)
            recyclerViewCertificates.adapter = certificateListAdapter
            var fragment = CertificateUploadFragment.newInstance()
            fragment.listener = object : CertificateUploadFragment.PassDataCallBack {
                override fun onPassData(data: QualificationDataItem) {
                    certificateListAdapter?.qualificationDataList?.add(data)
                    certificateListAdapter?.notifyDataSetChanged()
                }

            }
            imageViewaddCertificate.setOnClickListener {
                baseActivity?.openDialogFragment(fragment)
            }
        }
    }
}