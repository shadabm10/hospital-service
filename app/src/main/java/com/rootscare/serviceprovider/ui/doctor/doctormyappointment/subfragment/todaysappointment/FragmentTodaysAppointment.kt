package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.latikaseafood.utils.DateTimeUtils
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorTodaysAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter.AdapterDoctorTodaysAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.utils.MyImageCompress
import com.sihatku.commondialog.inputfilename.FileNameInputDialog
import com.unversal.imagecropper.CropImage
import com.unversal.imagecropper.CropImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

class FragmentTodaysAppointment : BaseFragment<FragmentDoctorTodaysAppointmentBinding, FragmentTodaysAppointmentViewModel>(),
    FragmentTodaysAppointmentNavigator {

    private var uploadedImageItemPosition: Int? = null
    private var contactListAdapter: AdapterDoctorTodaysAppointmentRecyclerview? = null
//    private var fileNameForTempUse: String = ""
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null

    private var fragmentDoctorTodaysAppointmentBinding: FragmentDoctorTodaysAppointmentBinding? = null
    private var fragmentTodaysAppointmentViewModel: FragmentTodaysAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_todays_appointment
    override val viewModel: FragmentTodaysAppointmentViewModel
        get() {
            fragmentTodaysAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentTodaysAppointmentViewModel::class.java
            )
            return fragmentTodaysAppointmentViewModel as FragmentTodaysAppointmentViewModel
        }

    companion object {
        private val TAG = FragmentTodaysAppointment::class.java.simpleName
        fun newInstance(): FragmentTodaysAppointment {
            val args = Bundle()
            val fragment = FragmentTodaysAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTodaysAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorTodaysAppointmentBinding = viewDataBinding

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId = fragmentTodaysAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentTodaysAppointmentViewModel!!.apidoctortodayappointmentlist(getDoctorUpcommingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(todaysAppointList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorTodaysAppointmentBinding!!.recyclerViewDoctorTodaysAppointment != null)
        val recyclerView = fragmentDoctorTodaysAppointmentBinding!!.recyclerViewDoctorTodaysAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterDoctorTodaysAppointmentRecyclerview(todaysAppointList, context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView2 = object : OnClickOfDoctorAppointment2 {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorAppointmentDetails.newInstance(contactListAdapter?.todaysAppointList!![position]!!.id!!, "doctor")
                )
            }

            override fun onAcceptBtnClick(position: String, text: String) {
                apiHitForCompleted(contactListAdapter?.todaysAppointList!![position.toInt()]!!.id!!, position.toInt())
            }

            override fun onUploadBtnClick(position: String, text: String) {
                openCamera()
//                showPictureDialog()
                uploadedImageItemPosition = position.toInt()
            }

            override fun onRejectBtnBtnClick(position: String, text: String) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            var updateAppointmentRequest = UpdateAppointmentRequest()
                            updateAppointmentRequest.id = contactListAdapter?.todaysAppointList!![position.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentTodaysAppointmentViewModel!!.apiupdatedoctorappointmentrequest(updateAppointmentRequest, position.toInt())
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to reject this appointment?")
            }
        }

    }

    override fun successGetDoctorTodaysAppointmentResponse(getDoctorTodaysAppointmentResponse: GetDoctorTodaysAppointmentResponse?) {

        baseActivity?.hideLoading()
        if (getDoctorTodaysAppointmentResponse?.code.equals("200")) {
            if (getDoctorTodaysAppointmentResponse?.result != null && getDoctorTodaysAppointmentResponse?.result?.size > 0) {
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorTodaysAppointmentResponse?.result)
            } else {
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.setText("Doctor Today's Appointment List Not Found.")
            }

        } else {
            fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.setText(getDoctorTodaysAppointmentResponse?.message)
            Toast.makeText(activity, getDoctorTodaysAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessMarkAsComplete(response: CommonResponse, position: Int) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            contactListAdapter?.todaysAppointList!![position]?.appointmentStatus = "Completed"
            contactListAdapter?.notifyItemChanged(position)
            /*contactListAdapter?.todaysAppointList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.todaysAppointList?.size!!)
            if (contactListAdapter?.todaysAppointList?.size == 0){
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            }*/
        }
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    ) {

        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            contactListAdapter?.todaysAppointList!![position]?.acceptanceStatus = "reject"
            contactListAdapter?.notifyItemChanged(position)
            contactListAdapter?.todaysAppointList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.todaysAppointList?.size!!)
            if (contactListAdapter?.todaysAppointList?.size == 0) {
                fragmentDoctorTodaysAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorTodaysAppointmentBinding?.recyclerViewDoctorTodaysAppointment?.visibility = View.GONE
            }
        }
    }

    override fun onSuccessUploadPrescription(response: CommonResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            Toast.makeText(activity!!, response.message, Toast.LENGTH_SHORT).show()
            /*contactListAdapter?.todaysAppointList!![uploadedImageItemPosition!!]?.uploadPrescription = fileNameForTempUse
            contactListAdapter?.notifyItemChanged(uploadedImageItemPosition!!)
            MyImageCompress.deleteFileFromGivenPath(imageSelectionModel?.file) // auto delete after image upload*/
        }
    }

    private fun apiHitForCompleted(id: String, position: Int) {
        var request = CommonUserIdRequest()
        request.id = id
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentTodaysAppointmentViewModel!!.apiHitForMarkAsComplete(
                request, position
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun openCamera() {
//        mCameraIntentHelper!!.startCameraIntent()
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")
            .setOutputCompressQuality(50)
            .start(activity!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            imageSelectionModel = AddlabTestImageSelectionModel()
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                imageSelectionModel?.imageDataFromCropLibrary = result
                val resultUri = result.uri
                if (resultUri != null) { // Get file from cache directory
                    Log.d(TAG, "--check_urii--  $resultUri")
                    FileNameInputDialog(activity!!, object : FileNameInputDialog.CallbackAfterDateTimeSelect {
                        override fun selectDateTime(dateTime: String) {
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
                                    "${dateTime}_${DateTimeUtils.getFormattedDate(Date(), "dd/MM/yyyy_HH:mm:ss")}${tempNameExtension}"
//                                    imageSelectionModel.fileNameAsOriginal = "${dateTime}${tempNameExtension}"
                                imageSelectionModel?.fileNameAsOriginal = "${dateTime}"
                                if (activity?.contentResolver?.getType(resultUri) == null) {
                                    imageSelectionModel?.type = "image"
                                } else {
                                    imageSelectionModel?.type = activity?.contentResolver?.getType(resultUri)
                                }
                                if (imageSelectionModel?.file!=null && imageSelectionModel?.file?.exists()!!) {
                                    uploadPrescription(imageSelectionModel!!)
                                }
                                Log.d("check_path", ": $resultUri")
                                Log.d("check_file_get", ": $fileCacheDir")
                            } else {
                                Log.d("file_does_not_exists", ": " + true)
                            }
                            hideKeyboard()
                        }
                    }).show(activity!!.supportFragmentManager)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("check_error", ": " + error.message)
            }
        }
    }


    private fun uploadPrescription(imageSelectionModel: AddlabTestImageSelectionModel) {
        if (fragmentTodaysAppointmentViewModel?.appSharedPref?.loginUserId != null) {
            baseActivity?.showLoading()
            baseActivity?.hideKeyboard()
            val doctor_id = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                fragmentTodaysAppointmentViewModel?.appSharedPref?.loginUserId!!
            )
            val patient_id = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                contactListAdapter?.todaysAppointList!![uploadedImageItemPosition!!]?.patient_id!!
            )
            val appointment_id = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                contactListAdapter?.todaysAppointList!![uploadedImageItemPosition!!]?.id!!
            )
            val prescription_number = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                imageSelectionModel.fileNameAsOriginal
            )
//            fileNameForTempUse = imageSelectionModel.fileNameAsOriginal

            val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageSelectionModel.file)
            val imageMultipartBody: MultipartBody.Part? =
                MultipartBody.Part.createFormData("e_prescription", imageSelectionModel.file?.name, image)
            fragmentTodaysAppointmentViewModel?.uploadPrescription(
                patient_id,
                doctor_id,
                appointment_id,
                prescription_number,
                imageMultipartBody
            )
        }
    }









    /*private val IMAGE_DIRECTORY = "/demonuts"
    private val PICKFILE_RESULT_CODE = 4
    private val GALLERY = 1
    private val CAMERA = 2
    private var imageFile:File?=null
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
//                    val path = saveImage(bitmap)
                    bitmapToFile(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }


        } else if (requestCode == CAMERA) {
            val contentURI = data!!.data
            val thumbnail = data!!.extras!!.get("data") as Bitmap
//            saveImage(thumbnail)
            bitmapToFile(thumbnail)

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
        imageFile = file
        val uploadImageModel = AddlabTestImageSelectionModel()
        uploadImageModel.file = imageFile!!
        showPopUpForFileName(uploadImageModel)
        return Uri.parse(file.absolutePath)

    }

    private fun showPopUpForFileName(uploadImageModel: AddlabTestImageSelectionModel) {
        FileNameInputDialog(activity!!, object : FileNameInputDialog.CallbackAfterDateTimeSelect {
            override fun selectDateTime(dateTime: String) {
                uploadImageModel.fileNameAsOriginal = dateTime
                uploadPrescription(uploadImageModel)
            }
        }).show(activity!!.supportFragmentManager)
    }*/

}