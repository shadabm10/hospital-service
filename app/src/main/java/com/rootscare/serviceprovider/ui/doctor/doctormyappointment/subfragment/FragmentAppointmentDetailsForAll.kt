package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorconsulting.FragmentDoctorConsulting
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.showimagelarger.TransaprentPopUpActivityForImageShow


class FragmentAppointmentDetailsForAll : BaseFragment<FragmentDoctorAppointmentDetailsBinding, FragmentDoctorAppointmentDetailsViewModel>(),
    FragmentDoctorAppointmentDetailsNavigator {

    private var appointmentId: String? = null
    private var service_type: String? = null

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
        fun newInstance(appointmentId: String, service_type: String): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
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
        var request = CommonUserIdRequest()
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
                    }else if (service_type.equals("nurse")){
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
                    }else if (service_type.equals("nurse")){
                        if (response.result.nurseName != null && !TextUtils.isEmpty(response.result.nurseName.trim())) {
                            tvDoctorName.text = response.result.nurseName
                        }
                    }

                    if (response.result.patientName != null && !TextUtils.isEmpty(response.result.patientName.trim())) {
                        txtPatientName.text = response.result.patientName
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
        handler?.postDelayed(runnable!!, 1000)
    }

    private fun stopPlaying() {
        seekposition = medicaPlayer?.currentPosition!!
        medicaPlayer?.pause()
        handler?.removeCallbacks(runnable!!)
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
            stopPlaying()
            seekposition = 0
            seekBar.progress = seekposition
        }
    }
}