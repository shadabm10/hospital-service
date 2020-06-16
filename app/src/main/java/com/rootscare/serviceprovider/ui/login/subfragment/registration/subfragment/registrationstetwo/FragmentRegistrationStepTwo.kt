package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo

import android.Manifest
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
import com.myfilepickesexample.FileUtil
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentRegistrationBinding
import com.rootscare.serviceprovider.databinding.FragmentRegistrationStepTwoBinding
import com.rootscare.serviceprovider.ui.base.AppData
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistrationNavigator
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistrationviewModel
import com.rootscare.utils.ManagePermissions
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.io.*
import java.util.*

class FragmentRegistrationStepTwo : BaseFragment<FragmentRegistrationStepTwoBinding, FragmentRegistrationStepTwoViewModel>(),
    FragmentRegistrationStepTwoNavigator {
    private var fragmentRegistrationStepTwoBinding: FragmentRegistrationStepTwoBinding? = null
    private var fragmentRegistrationStepTwoViewModel: FragmentRegistrationStepTwoViewModel? = null

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
    var choosenYear = 1980
    override val bindingVariable: Int
        get() =BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration_step_two
    override val viewModel: FragmentRegistrationStepTwoViewModel
        get() {
            fragmentRegistrationStepTwoViewModel = ViewModelProviders.of(this).get(FragmentRegistrationStepTwoViewModel::class.java!!)
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
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

        //Continue Button click
        fragmentRegistrationStepTwoBinding?.btnRooscareServiceproviderRegistrationSteptwoContinue?.setOnClickListener(
            View.OnClickListener {
                if(checkValidationForRegStepTwo()){
                    AppData?.registrationModelData?.imageFile=imageFile
                    AppData?.registrationModelData?.imageName=fragmentRegistrationStepTwoBinding?.txtImageSelectName?.text?.toString()
                    AppData?.registrationModelData?.certificateFile=certificatefileFile
                    AppData?.registrationModelData?.certificatename=fragmentRegistrationStepTwoBinding?.txtCertificate?.text?.toString()
                    AppData?.registrationModelData?.qualification=fragmentRegistrationStepTwoBinding?.edtRegHighestQualification?.text?.toString()
                    AppData?.registrationModelData?.passingYear=fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.text?.toString()
                    AppData?.registrationModelData?.institude=fragmentRegistrationStepTwoBinding?.edtRegInstitute?.text?.toString()
                    (activity as LoginActivity?)!!.setCurrentItem(3, true)
                }

            })

        //Select Passing Year
        fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.setOnClickListener(View.OnClickListener {
            val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->

                fragmentRegistrationStepTwoBinding?.txtRegPassingYear?.setText(selectedYear.toString())

            }, choosenYear, 0);

            builder.showYearOnly()
                .setYearRange(1980, 2090)
                .build()
                .show();
        })

        //Image Selection Button Click
        fragmentRegistrationStepTwoBinding?.llRegImageSelect?.setOnClickListener(View.OnClickListener {
            showPictureDialog()
        })

        //File Selection Button Click
        fragmentRegistrationStepTwoBinding?.llRegCertificateUpload?.setOnClickListener(View.OnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(
                chooseFile,
                PICKFILE_RESULT_CODE
            )
        })
    }
    //IMAGE SELECTION AND GET IMAGE PATH

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
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
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    bitmapToFile(bitmap)
                    fragmentRegistrationStepTwoBinding?.txtImageSelectName?.setText(getFileName(this!!.activity!!,
                        contentURI!!
                    ))
                    Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

//                    fragmentAddPatientForDoctorBookingBinding?.imgRootscarePatientProfileImage?.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }


        } else if (requestCode == CAMERA) {

            val contentURI = data!!.data
            val thumbnail = data!!.extras!!.get("data") as Bitmap
//            fragmentAddPatientForDoctorBookingBinding?.imgRootscarePatientProfileImage?.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            bitmapToFile(thumbnail)
//                fragmentRegistrationBinding?.txtImageSelectName?.setText(getFileName(this!!.activity!!,
//                    contentURI!!
//                ))
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()


        }else if(requestCode==PICKFILE_RESULT_CODE){
            if (resultCode == -1) {
                fileUri = data!!.data
                filePath = fileUri!!.path
                // tvItemPath.setText(filePath)
                try {
                    val file = FileUtil.from(this!!.activity!!, fileUri!!)
                    certificatefileFile=file
                    Log.d(
                        "file",
                        "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists()
                    )
                    fragmentRegistrationStepTwoBinding?.txtCertificate?.setText(
                        getFileName(this!!.activity!!, fileUri!!)
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + FragmentRegistrationStepTwo.IMAGE_DIRECTORY)

        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");


            f.createNewFile()
            // imageFile=f

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
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
        fragmentRegistrationStepTwoBinding?.txtImageSelectName?.setText(file.name)
        return Uri.parse(file.absolutePath)


    }
// End of Image Selection and Save Image Path

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
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
//End of select certificate file
    //Start of All field Validation

    private fun checkValidationForRegStepTwo(): Boolean {
        if (fragmentRegistrationStepTwoBinding?.txtImageSelectName?.text?.toString().equals("") ) {
            Toast.makeText(activity, "Please select your image!", Toast.LENGTH_SHORT).show()
//            activityLoginBinding?.edtPassword?.setError("Please enter Password")
            return false
        }
        if (fragmentRegistrationStepTwoBinding?.txtCertificate?.text?.toString().equals("") ) {
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
        }

        return true
    }
}