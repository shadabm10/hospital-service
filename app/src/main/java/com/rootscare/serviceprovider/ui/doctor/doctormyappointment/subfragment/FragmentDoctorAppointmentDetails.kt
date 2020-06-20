package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.media.MediaPlayer
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.api.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.api.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorconsulting.FragmentDoctorConsulting
import com.rootscare.serviceprovider.ui.home.HomeActivity


class FragmentDoctorAppointmentDetails : BaseFragment<FragmentDoctorAppointmentDetailsBinding, FragmentDoctorAppointmentDetailsViewModel>(),
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
        fun newInstance(appointmentId: String, service_type: String): FragmentDoctorAppointmentDetails {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            val fragment = FragmentDoctorAppointmentDetails()
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

                    if (response.result.doctorName != null && !TextUtils.isEmpty(response.result.doctorName.trim())) {
                        tvDoctorName.text = response.result.doctorName
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

                    var tempBool = false
                    if (response.result.symptomRecording != null && !TextUtils.isEmpty(response.result.symptomRecording.trim())) {
                        llRecording.visibility = View.VISIBLE
                        tempBool = true
                        var audioUrl = getString(R.string.api_base) + "uploads/images/" + response.result.symptomRecording
                        setUpAudiolayout(audioUrl)
                    } else {
                        llRecording.visibility = View.GONE
                        tempBool = false
                    }
                    if (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim())) {
                        tvSymptoms.visibility = View.VISIBLE
                        tempBool = true
                        tvSymptoms.text = response.result.symptomText
                    } else {
                        tvSymptoms.visibility = View.GONE
                        tempBool = false
                    }
                    if (!tempBool) {
                        llAboutSymptoms.visibility = View.GONE
                    } else {
                        llAboutSymptoms.visibility = View.VISIBLE
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
                if (!isPlaying){
                    startPlaying()
                    val resourceId: Int? = activity?.resources?.getIdentifier(
                        "pause",
                        "drawable", activity?.packageName
                    )
                    if(resourceId!=null) {
                        imgPlay.setImageResource(resourceId)
                    }
                }else{
                    stopPlaying()
                    val resourceId: Int? = activity?.resources?.getIdentifier(
                        "play",
                        "drawable", activity?.packageName
                    )
                    if(resourceId!=null) {
                        imgPlay.setImageResource(resourceId)
                    }
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

        }
    }

    private fun setAudioFileToMediaPlayer(url: String) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            medicaPlayer = MediaPlayer()
            medicaPlayer?.setDataSource(url)
            medicaPlayer?.prepare()
            totalDuaration = medicaPlayer?.duration!!
        }
    }

    private fun startPlaying() {
        seekposition = medicaPlayer?.currentPosition!!
        medicaPlayer?.start()
    }

    private fun stopPlaying(){
        seekposition = medicaPlayer?.currentPosition!!
        medicaPlayer?.pause()
    }

    private fun seekToForMediaPlayer() {
        medicaPlayer?.seekTo(seekposition)
    }
}