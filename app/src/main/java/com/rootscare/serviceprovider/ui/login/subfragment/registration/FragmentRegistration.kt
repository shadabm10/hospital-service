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
import com.rootscare.serviceprovider.ui.base.BaseFragment
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

        fragmentRegistrationBinding?.txtRegPassingYear?.setOnClickListener(View.OnClickListener {
            val builder = MonthPickerDialog.Builder(activity, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->

                fragmentRegistrationBinding?.txtRegPassingYear?.setText(selectedYear.toString())

            }, choosenYear, 0);

            builder.showYearOnly()
                .setYearRange(1990, 2090)
                .build()
                .show();
        })

        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSteponeContinue?.setOnClickListener(
            View.OnClickListener {

                fragmentRegistrationBinding?.llRegistrationSetpOne?.visibility=View.GONE
                fragmentRegistrationBinding?.llRegistrationSetpTwo?.visibility=View.VISIBLE
                fragmentRegistrationBinding?.llRegistrationSetpThree?.visibility=View.GONE
            })

        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSteptwoContinue?.setOnClickListener(
            View.OnClickListener {
                fragmentRegistrationBinding?.llRegistrationSetpOne?.visibility=View.GONE
                fragmentRegistrationBinding?.llRegistrationSetpTwo?.visibility=View.GONE
                fragmentRegistrationBinding?.llRegistrationSetpThree?.visibility=View.VISIBLE
            })

        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSubmit?.setOnClickListener(
            View.OnClickListener {
//                fragmentRegistrationBinding?.llRegistrationSetpOne?.visibility=View.GONE
//                fragmentRegistrationBinding?.llRegistrationSetpTwo?.visibility=View.GONE
//                fragmentRegistrationBinding?.llRegistrationSetpThree?.visibility=View.VISIBLE

            })

        //Image Selection Button Click
        fragmentRegistrationBinding?.llRegImageSelect?.setOnClickListener(View.OnClickListener {
            showPictureDialog()
        })

        //File Selection Button Click
        fragmentRegistrationBinding?.llRegCertificateUpload?.setOnClickListener(View.OnClickListener {
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
                    fragmentRegistrationBinding?.txtImageSelectName?.setText(getFileName(this!!.activity!!,
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
                    val file = from(this!!.activity!!, fileUri!!)
                    certificatefileFile=file
                    Log.d(
                        "file",
                        "File...:::: uti - " + file.path + " file -" + file + " : " + file.exists()
                    )
                    fragmentRegistrationBinding?.txtCertificate?.setText(
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
            (Environment.getExternalStorageDirectory()).toString() + FragmentRegistration.IMAGE_DIRECTORY)

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
        fragmentRegistrationBinding?.txtImageSelectName?.setText(file.name)
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





}