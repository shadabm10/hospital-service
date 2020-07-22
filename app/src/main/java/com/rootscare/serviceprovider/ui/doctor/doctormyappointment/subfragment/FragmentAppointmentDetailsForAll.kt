package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.dialog.CommonDialog
import com.latikaseafood.utils.DateTimeUtils
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.api.response.CommonResponse
import com.rootscare.data.model.api.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.dialog.inputfilename.FileNameInputDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorconsulting.FragmentDoctorConsulting
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.FragmentTodaysAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.showimagelarger.TransaprentPopUpActivityForImageShow
import com.rootscare.utils.MyImageCompress
import com.unversal.imagecropper.CropImage
import com.unversal.imagecropper.CropImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class FragmentAppointmentDetailsForAll : BaseFragment<FragmentDoctorAppointmentDetailsBinding, FragmentDoctorAppointmentDetailsViewModel>(),
    FragmentDoctorAppointmentDetailsNavigator {

    private var patientIdForPrescriptionUpload:String?=null
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null
    private var appointmentId: String? = null
    private var service_type: String? = null
    private var statusOfAppointmentForFlowChange: String? = null

    private var medicaPlayer: MediaPlayer? = null

    private var fragmentDoctorAppointmentDetailsBinding: FragmentDoctorAppointmentDetailsBinding? = null
    private var fragmentDoctorAppointmentDetailsViewModel: FragmentDoctorAppointmentDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_appointment_details
    override val viewModel: FragmentDoctorAppointmentDetailsViewModel
        get() {
            fragmentDoctorAppointmentDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorAppointmentDetailsViewModel::class.java
            )
            return fragmentDoctorAppointmentDetailsViewModel as FragmentDoctorAppointmentDetailsViewModel
        }

    companion object {
        private const val TAG = "FragmentAppointmentDeta"
        fun newInstance(appointmentId: String, service_type: String): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            appointmentId: String,
            service_type: String,
            statusOfAppointmentForFlowChange: String
        ): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            args.putString("statusOfAppointmentForFlowChange", statusOfAppointmentForFlowChange)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            appointmentId: String,
            service_type: String,
            statusOfAppointmentForFlowChange: String,
            patientIdForPrescriptionUploadFromTodayAppointment: String
        ): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            args.putString("statusOfAppointmentForFlowChange", statusOfAppointmentForFlowChange)
            args.putString("patientIdForPrescriptionUploadFromTodayAppointment",patientIdForPrescriptionUploadFromTodayAppointment)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorAppointmentDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorAppointmentDetailsBinding = viewDataBinding

        patientIdForPrescriptionUpload = arguments?.getString("statusOfAppointmentForFlowChange")
        statusOfAppointmentForFlowChange = arguments?.getString("statusOfAppointmentForFlowChange")
        appointmentId = arguments?.getString("appointmentId")
        service_type = arguments?.getString("service_type")
        apiHitFordetails()

        fragmentDoctorAppointmentDetailsBinding?.btnRootscareDoctorConsulting?.setOnClickListener(
            View.OnClickListener {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorConsulting.newInstance()
                )
            })
    }


    private fun apiHitFordetails() {
        val request = CommonUserIdRequest()
        request.id = appointmentId
        request.service_type = service_type
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentDoctorAppointmentDetailsViewModel!!.getDetails(
                request
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSuccessDetails(response: AppointmentDetailsResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null) {
                with(fragmentDoctorAppointmentDetailsBinding!!) {
                    crdviewDoctorappoitmentList.visibility = View.VISIBLE
                    if (service_type.equals("doctor")) {
                        if (response.result.doctorImage != null && !TextUtils.isEmpty(response.result.doctorImage.trim())) {
                            val options: RequestOptions =
                                RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_no_image)
                                    .priority(Priority.HIGH)
                            Glide
                                .with(activity!!)
                                .load(getString(R.string.api_base) + "uploads/images/" + response.result.doctorImage)
                                .apply(options)
                                .into(imageView)
                        }
                    } else if (service_type.equals("nurse")) {
                        if (response.result.nurseImage != null && !TextUtils.isEmpty(response.result.nurseImage.trim())) {
                            val options: RequestOptions =
                                RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_no_image)
                                    .priority(Priority.HIGH)
                            Glide
                                .with(activity!!)
                                .load(getString(R.string.api_base) + "uploads/images/" + response.result.nurseImage)
                                .apply(options)
                                .into(imageView)
                        }
                    }


                    if (service_type.equals("doctor")) {
                        if (response.result.doctorName != null && !TextUtils.isEmpty(response.result.doctorName.trim())) {
                            tvDoctorName.text = response.result.doctorName
                        }
                    } else if (service_type.equals("nurse")) {
                        if (response.result.nurseName != null && !TextUtils.isEmpty(response.result.nurseName.trim())) {
                            tvDoctorName.text = response.result.nurseName
                        }
                    }

                    if (response.result.patientName != null && !TextUtils.isEmpty(response.result.patientName.trim())) {
                        txtPatientName.text = response.result.patientName
                    }
                    if (patientIdForPrescriptionUpload==null) {
                        if (response.result.patient_id != null && !TextUtils.isEmpty(response.result.patient_id?.trim())) {
                            patientIdForPrescriptionUpload = response.result.patient_id
                        }
                    }
                    if (response.result.bookingDate != null && !TextUtils.isEmpty(response.result.bookingDate.trim())) {
                        txtBookingDate.text = response.result.bookingDate
                    }
                    if (response.result.patientContact != null && !TextUtils.isEmpty(response.result.patientContact.trim())) {
                        tvPhoneNumber.text = response.result.patientContact
                    }
                    if (response.result.appointmentDate != null && !TextUtils.isEmpty(response.result.appointmentDate.trim())) {
                        tvAppointmentDate.text = response.result.appointmentDate
                    }

                    if (response.result.fromTime != null && !TextUtils.isEmpty(response.result.fromTime.trim())) {
                        llAppointmentTime.visibility = View.VISIBLE
                        tvAppointmentTime.text = response.result.fromTime
                    }

                    if (response.result.appointmentStatus != null && !TextUtils.isEmpty(response.result.appointmentStatus.trim())) {
                        tvAppointmentStatus.text = response.result.appointmentStatus
                        if(appointmentId!=null) {
                            setUpAcceptRejectSection(response.result.appointmentStatus.toLowerCase(), appointmentId?.toInt()!!)
                        }
                    }
                    if (response.result.acceptanceStatus != null && !TextUtils.isEmpty(response.result.acceptanceStatus.trim())) {
                        tvAcceptanceStatus.text = response.result.acceptanceStatus
                    }


                    if (response.result.symptomRecording != null && !TextUtils.isEmpty(response.result.symptomRecording.trim())) {
                        llRecording.visibility = View.VISIBLE
                        val audioUrl = getString(R.string.api_base) + "uploads/images/" + response.result.symptomRecording
                        setUpAudiolayout(audioUrl)
                        if (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim())) {
                            tvSymptoms.visibility = View.VISIBLE
                            llOrPortion.visibility = View.VISIBLE
                            tvSymptoms.text = "Symptoms: ${response.result.symptomText}"
                        } else {
                            tvSymptoms.visibility = View.GONE
                        }
                    } else {
                        llRecording.visibility = View.GONE
                        if (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim())) {
                            tvSymptoms.visibility = View.VISIBLE
                            llOrPortion.visibility = View.GONE
                            tvSymptoms.text = "Symptoms: ${response.result.symptomText}"
                        } else {
                            tvSymptoms.visibility = View.GONE
                        }
                    }

                    if ((response.result.symptomRecording != null && !TextUtils.isEmpty(response.result.symptomRecording.trim())) ||
                        (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim()))
                    ) {
                        llAboutSymptoms.visibility = View.VISIBLE
                    } else {
                        llAboutSymptoms.visibility = View.GONE
                    }

                    if (response.result.acceptanceStatus != null && !TextUtils.isEmpty(response.result.acceptanceStatus.trim())) {
                        tvAcceptanceStatus.text = response.result.acceptanceStatus
                    }

                    if (response.result.prescription != null && response.result.prescription.size > 0) {
                        llPrescription.visibility = View.VISIBLE
                        prescriptionRecyclerView.layoutManager = LinearLayoutManager(activity!!)
                        val prescriptionList = response.result.prescription
                        val adapter =
                            ShowPrescriptionsImagesRecyclerviewAdapter(
                                prescriptionList,
                                activity!!
                            )
                        adapter.recyclerViewItemClickWithView2 = object : OnClickOfDoctorAppointment {
                            override fun onItemClick(position: Int) {
                                var imageUrl =
                                    context?.getString(R.string.api_base) + "uploads/images/" + adapter.todaysAppointList!![position].e_prescription!!
                                startActivity(TransaprentPopUpActivityForImageShow.newIntent(activity!!, imageUrl))
                            }

                            override fun onAcceptBtnClick(id: String, text: String) {

                            }

                            override fun onRejectBtnBtnClick(id: String, text: String) {

                            }

                        }
                        prescriptionRecyclerView.adapter = adapter
                    } else {
                        llPrescription.visibility = View.GONE
                    }

                }
            }
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }


    private var isPlaying = false
    private var seekposition: Int = 0
    private var totalDuaration: Int = 0
    private fun setUpAudiolayout(url: String) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
//            txtElaspsedTime.base = seekposition.toLong()
            setAudioFileToMediaPlayer(url)

            imgPlay.setOnClickListener {
                if (!isPlaying) {
                    startPlaying()
                    val resourceId: Int? = activity?.resources?.getIdentifier(
                        "pause",
                        "drawable", activity?.packageName
                    )
                    if (resourceId != null) {
                        imgPlay.setImageResource(resourceId)
                    }
                    isPlaying = true
                } else {
                    stopPlaying()
                    val resourceId: Int? = activity?.resources?.getIdentifier(
                        "play",
                        "drawable", activity?.packageName
                    )
                    if (resourceId != null) {
                        imgPlay.setImageResource(resourceId)
                    }
                    isPlaying = false
                }
            }

            //seekbar change listner
            seekBar.max = totalDuaration
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    seekposition = seekBar.progress
                    medicaPlayer?.seekTo(seekposition)
                    fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.base = SystemClock.elapsedRealtime() - seekposition
                }
            })

            medicaPlayer?.setOnCompletionListener(OnCompletionListener {
                resetAudioFileToMediaPlayer()
            })

        }
    }

    private fun setAudioFileToMediaPlayer(url: String) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            medicaPlayer = MediaPlayer()
            medicaPlayer?.setDataSource(url)
            medicaPlayer?.prepare()
            totalDuaration = medicaPlayer?.duration!!
            val minutes: Int = totalDuaration / 1000 / 60
            val seconds: Int = totalDuaration / 1000 % 60
            var tempMinutes = "00"
            var tempSeconds = "00"
            if (minutes != 0) {
                if (minutes >= 10) {
                    tempMinutes = minutes.toString()
                } else {
                    tempMinutes = "0$minutes"
                }
            }
            if (seconds != 0) {
                if (seconds >= 10) {
                    tempSeconds = seconds.toString()
                } else {
                    tempSeconds = "0$seconds"
                }
            }
            txtTime.text = "$tempMinutes : $tempSeconds"
        }
    }

    var handler: Handler? = null
    var runnable: Runnable? = null
    private fun startPlaying() {
        seekposition = medicaPlayer?.currentPosition!!
        medicaPlayer?.start()

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                try {
                    seekposition = medicaPlayer?.currentPosition!!
                    fragmentDoctorAppointmentDetailsBinding!!.seekBar.progress = seekposition
                    handler?.postDelayed(this, 1000)
                } catch (ed: IllegalStateException) {
                    ed.printStackTrace()
                }
            }
        }
        handler?.postDelayed(runnable!!, 0)
//        fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.start()
        fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.base = SystemClock.elapsedRealtime() - seekposition
        fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.start()
    }

    private fun stopPlaying() {
        if (medicaPlayer!==null) {
            seekposition = medicaPlayer?.currentPosition!!
            medicaPlayer?.pause()
            handler?.removeCallbacks(runnable!!)
            fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.stop()
        }
    }

    private fun resetAudioFileToMediaPlayer() {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            val resourceId: Int? = activity?.resources?.getIdentifier(
                "play",
                "drawable", activity?.packageName
            )
            if (resourceId != null) {
                imgPlay.setImageResource(resourceId)
            }
            isPlaying = false
            /*stopPlaying()
            seekposition = 0
            seekBar.progress = seekposition
            txtElaspsedTime.base = SystemClock.elapsedRealtime()*/
            txtElaspsedTime.stop()
            val handler = Handler()
            handler.postDelayed({
                txtElaspsedTime.setBase(SystemClock.elapsedRealtime())
                medicaPlayer?.seekTo(0) }, 100)
//            txtElaspsedTime.base = SystemClock.elapsedRealtime() - seekposition
//            txtElaspsedTime.base = seekposition.toLong()
        }
    }

    override fun onStop() {
        stopPlaying()
        super.onStop()
    }










    private fun setUpAcceptRejectSection(status: String, id: Int) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            if (service_type?.toLowerCase().equals("doctor")) {
                if (statusOfAppointmentForFlowChange != null && statusOfAppointmentForFlowChange?.equals("pending")!!) {
                    llBottomUploadPrescription.visibility = View.GONE
                    if (status.contains("accepted")) {
                        llBottomAcceptReject.visibility = View.GONE
                    }else if (status.contains("complete")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (status.contains("reject")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (status.contains("cancel")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (status.contains("book")) {
                        llBottomAcceptReject.visibility = View.VISIBLE
                        btnAccept.setOnClickListener {
                            acceptRejectAppointment(id, "Accept")
                        }
                        btnRejectt.setOnClickListener {
                            acceptRejectAppointment(id, "Reject")
                        }
                    }
                } else if (statusOfAppointmentForFlowChange != null && statusOfAppointmentForFlowChange?.equals("today")!!) {
                    llBottomUploadPrescription.visibility = View.VISIBLE
                    llBottomAcceptReject.visibility = View.GONE
                    if (status.contains("complete")) {
                        btnCompleted.visibility = View.GONE
                        btnUploadPrescription.visibility = View.VISIBLE
                        btnUploadPrescription.setOnClickListener {
                            openCamera()
                        }
                    } else {
                        btnCompleted.visibility = View.VISIBLE
                        btnUploadPrescription.visibility = View.GONE
                        btnCompleted.setOnClickListener {
                            apiHitForCompleted(id.toString())
                        }
                    }
                } else {
                    llBottomAcceptReject.visibility = View.GONE
                    llBottomUploadPrescription.visibility = View.GONE
                }
            }else if (service_type?.toLowerCase().equals("nurse")) {
                if (statusOfAppointmentForFlowChange != null && statusOfAppointmentForFlowChange?.equals("pending")!!) {
                    llBottomUploadPrescription.visibility = View.GONE
                    if (status.contains("complete")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (status.contains("reject")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (status.contains("cancel")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (status.contains("book")) {
                        llBottomAcceptReject.visibility = View.VISIBLE
                        btnAccept.setOnClickListener {
                            acceptRejectAppointment(id, "Accept")
                        }
                        btnRejectt.setOnClickListener {
                            acceptRejectAppointment(id, "Reject")
                        }
                    }
                }else {
                    llBottomAcceptReject.visibility = View.GONE
                    llBottomUploadPrescription.visibility = View.GONE
                }
            }
        }
    }


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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            imageSelectionModel = AddlabTestImageSelectionModel()
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
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
        if (fragmentDoctorAppointmentDetailsViewModel?.appSharedPref?.loginUserId != null && patientIdForPrescriptionUpload!=null) {
            baseActivity?.showLoading()
            baseActivity?.hideKeyboard()
            val doctor_id = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                fragmentDoctorAppointmentDetailsViewModel?.appSharedPref?.loginUserId!!
            )
            val patient_id = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                patientIdForPrescriptionUpload!!
            )
            val appointment_id = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                appointmentId!!
            )
            val prescription_number = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                imageSelectionModel.fileNameAsOriginal
            )
//            fileNameForTempUse = imageSelectionModel.fileNameAsOriginal

            val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageSelectionModel.file)
            val imageMultipartBody: MultipartBody.Part? =
                MultipartBody.Part.createFormData("e_prescription", imageSelectionModel.file?.name, image)
            fragmentDoctorAppointmentDetailsViewModel?.uploadPrescription(
                patient_id,
                doctor_id,
                appointment_id,
                prescription_number,
                imageMultipartBody
            )
        }
    }

    private fun apiHitForCompleted(id: String) {
        var request = CommonUserIdRequest()
        request.id = id
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentDoctorAppointmentDetailsViewModel!!.apiHitForMarkAsComplete(
                request
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun acceptRejectAppointment(id: Int, status: String) {
        CommonDialog.showDialog(activity!!, object :
            DialogClickCallback {
            override fun onDismiss() {

            }

            override fun onConfirm() {
                if (isNetworkConnected) {
                    baseActivity?.showLoading()
                    val updateAppointmentRequest = UpdateAppointmentRequest()
                    updateAppointmentRequest.id = id.toString()
                    updateAppointmentRequest.acceptanceStatus = status
                    fragmentDoctorAppointmentDetailsViewModel!!.apiupdatedoctorappointmentrequest(updateAppointmentRequest)
                } else {
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }
            }

        }, "Confirmation", "Are you sure to ${status.toLowerCase(Locale.ROOT)} this appointment?")
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?) {
        baseActivity?.hideLoading()
        apiHitFordetails()
    }

    override fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
    }

    override fun onSuccessMarkAsComplete(response: CommonResponse) {
        baseActivity?.hideLoading()
        apiHitFordetails()
    }

    override fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
    }

    override fun onSuccessUploadPrescription(response: CommonResponse) {
        baseActivity?.hideLoading()
        apiHitFordetails()
    }



}