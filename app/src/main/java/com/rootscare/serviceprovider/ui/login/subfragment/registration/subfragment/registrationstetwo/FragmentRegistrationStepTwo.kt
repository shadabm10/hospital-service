package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.latikaseafood.utils.DateTimeUtils
import com.myfilepickesexample.FileUtil
import com.rootscare.data.model.api.response.doctor.profileresponse.QualificationDataItem
import com.rootscare.dialog.certificateupload.CertificateUploadFragment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationBinding
import com.rootscare.serviceprovider.databinding.FragmentRegistrationStepTwoBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.adapter.CertificateListAdapter
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistrationNavigator
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistrationviewModel
import com.rootscare.utils.ManagePermissions
import com.rootscare.utils.MyImageCompress
import com.unversal.imagecropper.CropImage
import com.unversal.imagecropper.CropImageView
import java.io.*
import java.util.*

class FragmentRegistrationStepTwo : BaseFragment<FragmentRegistrationStepTwoBinding, FragmentRegistrationStepTwoViewModel>(),
    FragmentRegistrationStepTwoNavigator {
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null
    private var fragmentRegistrationStepTwoBinding: FragmentRegistrationStepTwoBinding? = null
    private var fragmentRegistrationStepTwoViewModel: FragmentRegistrationStepTwoViewModel? = null
    private var certificateListAdapter: CertificateListAdapter? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    val PICKFILE_RESULT_CODE = 4
    private var fileUri: Uri? = null
    private var filePath: String? = null
    private var selectedGender = ""
    var imageFile: File? = null
    var certificatefileFile: File? = null
    var monthOfDob: String = ""
    var dayOfDob: String = ""
    var choosenYear = 1980
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_step_two
    override val viewModel: FragmentRegistrationStepTwoViewModel
        get() {
            fragmentRegistrationStepTwoViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepTwoViewModel::class.java)
            return fragmentRegistrationStepTwoViewModel as FragmentRegistrationStepTwoViewModel
        }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        val TAG = FragmentRegistrationStepTwo::class.java.simpleName
        fun newInstance(): FragmentRegistrationStepTwo {
            val args = Bundle()
            val fragment = FragmentRegistrationStepTwo()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationStepTwoViewModel!!.navigator = this


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationStepTwoBinding = viewDataBinding

        addCertificatePortionsSetUp()

        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()


        fragmentRegistrationStepTwoBinding?.imageViewBack?.setOnClickListener {
            (activity as LoginActivity).onBackPressed()
        }

        //Continue Button click
        fragmentRegistrationStepTwoBinding?.btnRooscareServiceproviderRegistrationSteptwoContinue?.setOnClickListener(
            View.OnClickListener {
                if (checkValidationForRegStepTwo()) {
                    AppData.boolSForRefreshLayout = false
                    AppData.registrationModelData?.imageFile = imageFile
                    AppData.registrationModelData?.imageName = fragmentRegistrationStepTwoBinding?.txtImageSelectName?.text?.toString()
                    AppData.registrationModelData?.certificateFile = certificatefileFile
                    AppData.registrationModelData?.qualificationDataList = certificateListAdapter?.qualificationDataList!!
                    if (certificateListAdapter?.qualificationDataList != null && certificateListAdapter?.qualificationDataList!!.size > 0) {
                        var qualification = ""
                        var passingYear = ""
                        var institute = ""
                        for (i in 0 until certificateListAdapter?.qualificationDataList?.size!!) {
                            if (i == 0) {
                                qualification += certificateListAdapter?.qualificationDataList!![i].qualification!!
                                passingYear += certificateListAdapter?.qualificationDataList!![i].passingYear!!
                                institute += certificateListAdapter?.qualificationDataList!![i].institute!!
                            } else {
                                qualification += ",${certificateListAdapter?.qualificationDataList!![i].qualification!!}"
                                passingYear += ",${certificateListAdapter?.qualificationDataList!![i].passingYear!!}"
                                institute += ",${certificateListAdapter?.qualificationDataList!![i].institute!!}"
                            }
                        }
                        AppData.registrationModelData?.qualification = qualification
                        AppData.registrationModelData?.passingYear = passingYear
                        AppData.registrationModelData?.institude = institute
                    }
                    /*AppData?.registrationModelData?.certificatename=fragmentRegistrationStepTwoBinding?.txtCertificate?.text?.toString()
                    AppData?.registrationModelData?.qualification=fragmentRegistrationStepTwoBinding?.edtRegHighestQualification?.text?.toString()
                    AppData?.registrationModelData?.passingYear=fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.text?.toString()
                    AppData?.registrationModelData?.institude=fragmentRegistrationStepTwoBinding?.edtRegInstitute?.text?.toString()*/
                    (activity as LoginActivity?)!!.setCurrentItem(3, true)
                }

            })

        //Select Passing Year
        /*fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.setOnClickListener(View.OnClickListener {
            val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->

                fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.setText(selectedYear.toString())

            }, choosenYear, 0);

            builder.showYearOnly()
                .setYearRange(1980, 2090)
                .build()
                .show();
        })*/

        //Image Selection Button Click
        fragmentRegistrationStepTwoBinding?.llRegImageSelect?.setOnClickListener(View.OnClickListener {
            openCamera()
        })

        //File Selection Button Click
//        fragmentRegistrationStepTwoBinding?.llRegCertificateUpload?.setOnClickListener(View.OnClickListener {
//            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
//            chooseFile.type = "*/*"
//            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
//            startActivityForResult(
//                chooseFile,
//                PICKFILE_RESULT_CODE
//            )
//        })
    }
    //IMAGE SELECTION AND GET IMAGE PATH

    private fun openCamera() {
//        mCameraIntentHelper!!.startCameraIntent()
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")
            .setOutputCompressQuality(10)
            .start(activity!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
          if (requestCode == PICKFILE_RESULT_CODE) {
                if (resultCode == -1) {
                    fileUri = data!!.data
                    filePath = fileUri!!.path
                    // tvItemPath.setText(filePath)
                    try {
                        val file = FileUtil.from(this.activity!!, fileUri!!)
                        certificatefileFile = file
                        Log.d(
                            "file",
                            "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists()
                        )
//                    fragmentRegistrationStepTwoBinding?.txtCertificate?.setText(
//                        getFileName(this!!.activity!!, fileUri!!)
//                    )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                imageSelectionModel = AddlabTestImageSelectionModel()
                val result = CropImage.getActivityResult(data)

                imageSelectionModel?.imageDataFromCropLibrary = result
                val resultUri = result.uri
                if (resultUri != null) { // Get file from cache directory
//                        FileNameInputDialog(activity!!, object : FileNameInputDialog.CallbackAfterDateTimeSelect {
//                            override fun selectDateTime(dateTime: String) {
//
//                            }
//                        }).show(activity!!.supportFragmentManager)
                    val fileCacheDir = File(activity?.cacheDir, resultUri.lastPathSegment)
                    if (fileCacheDir.exists()) {
//                                    imageSelectionModel?.file = fileCacheDir
                        imageSelectionModel?.file = MyImageCompress.compressImageFilGottenFromCache(activity, resultUri, 10)
                        imageSelectionModel?.filePath = resultUri.toString()
                        imageSelectionModel?.rawFileName = resultUri.lastPathSegment
                        var tempNameExtension = ""
                        if (resultUri.lastPathSegment?.contains(".jpg")!!) {
                            tempNameExtension = ".jpg"
                        } else if (resultUri.lastPathSegment?.contains(".png")!!) {
                            tempNameExtension = ".png"
                        }
                        imageSelectionModel?.fileName =
                            "${resultUri.lastPathSegment}_${DateTimeUtils.getFormattedDate(
                                Date(),
                                "dd/MM/yyyy_HH:mm:ss"
                            )}${tempNameExtension}"
//                                    imageSelectionModel.fileNameAsOriginal = "${dateTime}${tempNameExtension}"
                        imageSelectionModel?.fileNameAsOriginal = "${resultUri.lastPathSegment}"
                        if (activity?.contentResolver?.getType(resultUri) == null) {
                            imageSelectionModel?.type = "image"
                        } else {
                            imageSelectionModel?.type = activity?.contentResolver?.getType(resultUri)
                        }
                        fragmentRegistrationStepTwoBinding?.txtImageSelectName?.text = imageSelectionModel?.fileName
                        /*val options: RequestOptions =
                            RequestOptions()
                                .centerCrop()
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.profile_no_image)
                                .error(R.drawable.profile_no_image)
                                .priority(Priority.HIGH)
                        Glide
                            .with(activity!!)
                            .load(imageSelectionModel?.file?.absolutePath)
                            .apply(options)
                            .into(fragmentDoctorEditProfileBinding?.imgDoctorProfile!!)*/
                        imageFile = imageSelectionModel?.file  // as imageFile is used at the timeof api hit

//                                    if (imageSelectionModel?.file!=null && imageSelectionModel?.file?.exists()!!) {
//                                        uploadPrescription(imageSelectionModel!!)
//                                    }
                        Log.d("check_path", ": $resultUri")
                        Log.d("check_file_get", ": $fileCacheDir")
                    } else {
                        Log.d("file_does_not_exists", ": " + true)
                    }
                    hideKeyboard()
                }

            }
        }
    }

// End of Image Selection and Save Image Path

    // Receive the permissions request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    Toast.makeText(activity, "Permissions granted.", Toast.LENGTH_SHORT).show()
                    //  toast("Permissions granted.")
                } else {
                    Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_SHORT).show()
                    // toast("Permissions denied.")
                }
                return
            }
        }


    }


    /* private fun getFileName(context: Context, uri: Uri): String? {
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
     }*/
//End of select certificate file
    //Start of All field Validation

    private fun checkValidationForRegStepTwo(): Boolean {
        if (fragmentRegistrationStepTwoBinding?.txtImageSelectName?.text?.toString().equals("")) {
            Toast.makeText(activity, "Please select your profile image!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (certificateListAdapter?.qualificationDataList?.size != null && certificateListAdapter?.qualificationDataList?.size == 0) {
            Toast.makeText(activity!!, "Please add atleast one certificate", Toast.LENGTH_SHORT).show()
            return false
        }
        /*if (fragmentRegistrationStepTwoBinding?.txtCertificate?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select your highest education certificate!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepTwoBinding?.edtRegHighestQualification?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please enter your highest education qualification!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select your passing year!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }

        if (fragmentRegistrationStepTwoBinding?.txtCertificate?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select your institute name!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }*/

        return true
    }


    private fun addCertificatePortionsSetUp() {
        with(fragmentRegistrationStepTwoBinding!!) {
            certificateListAdapter?.qualificationDataList?.clear()
            recyclerViewCertificates.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(activity, 1, androidx.recyclerview.widget.GridLayoutManager.VERTICAL, false)
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

    /*override fun onResume() {
        super.onResume()
        if(AppData.registrationModelData!=null){

        }else{
            fragmentRegistrationStepTwoBinding?.txtImageSelectName?.text = ""
            certificateListAdapter?.qualificationDataList?.clear()
            certificateListAdapter?.notifyDataSetChanged()
        }

    }*/

}