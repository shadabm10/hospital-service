package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.interfaces.OnClickWithTwoButton
import com.rootscare.data.model.api.response.doctor.appointment.requestappointment.getrequestappointment.ResultItem
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorRequestedAppointmentlistBinding
import com.rootscare.serviceprovider.databinding.ItemDoctorTodaysAppointmentrecyclerviewBinding
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter.AdapterDoctorTodaysAppointmentRecyclerview
import kotlinx.android.synthetic.main.item_doctor_requested_appointmentlist.view.*
import kotlinx.android.synthetic.main.item_doctor_todays_appointmentrecyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterRequestedAppointmentListRecyclerview (val requestedappointmentList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterRequestedAppointmentListRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickOfDoctorAppointment
    var startTime=""
    var endTime=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemDoctorRequestedAppointmentlistBinding>(
                LayoutInflater.from(context),
                R.layout.item_doctor_requested_appointmentlist, parent, false
            )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return requestedappointmentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorRequestedAppointmentlistBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private var local_position: Int = 0

        init {
            itemView?.root?.crdview_requested_appointment_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(1)
            })
            itemView?.root?.btn_accept?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onAcceptBtnClick(requestedappointmentList?.get(local_position)?.id!!,"Accept")
            })

            itemView?.root?.btn_reject?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onRejectBtnBtnClick(requestedappointmentList?.get(local_position)?.id!!,"Reject")
            })
//            itemView?.root?.btn_view_trainner_profile?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(trainerList?.get(local_position)?.id?.toInt()!!)
//            })

//            itemView.root?.img_bid?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_bid,local_position)
//                    //serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_bid,it1) }
//                }
//            }
//
//            itemView.root?.img_details?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_details,local_position)
//                    // serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_details,it1) }
//                }
//            }


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(requestedappointmentList?.get(pos)?.orderId!=null && !requestedappointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_requested_appointment?.setText(requestedappointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_requested_appointment?.setText("")
            }

            if(requestedappointmentList?.get(pos)?.patientName!=null && !requestedappointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_requested_patient_name?.setText(requestedappointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_requested_patient_name?.setText("")
            }
            if(requestedappointmentList?.get(pos)?.bookingDate!=null && !requestedappointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_requested_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",requestedappointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_requested_booking_date?.setText("")
            }
            if(requestedappointmentList?.get(pos)?.fromTime!=null && !requestedappointmentList?.get(pos)?.fromTime.equals("")){
                startTime= requestedappointmentList?.get(pos)?.fromTime!!
            }else{
                startTime=""
            }

            if(requestedappointmentList?.get(pos)?.toTime!=null && !requestedappointmentList?.get(pos)?.toTime.equals("")){
                endTime= requestedappointmentList?.get(pos)?.toTime!!
            }else{
                endTime=""
            }

            itemView?.rootView?.txt_requested_time?.setText(startTime+"-"+endTime)
            if(requestedappointmentList?.get(pos)?.appointmentDate!=null && !requestedappointmentList?.get(pos)?.appointmentDate.equals("")){
                itemView?.rootView?.txt_requested_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",requestedappointmentList?.get(pos)?.appointmentDate))
            }else{
                itemView?.rootView?.txt_requested_appointment_date?.setText("")
            }




//            itemView?.rootView?.txt_teacher_name?.text= trainerList?.get(pos)?.name
//            itemView?.rootView?.txt_teacher_qualification?.text= "Qualification : "+" "+trainerList?.get(pos)?.qualification
//            if(trainerList?.get(pos)?.avgRating!=null && !trainerList?.get(pos)?.avgRating.equals("")){
//                itemView?.rootView?.ratingBarteacher?.rating= trainerList?.get(pos)?.avgRating?.toFloat()!!
//            }


//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


        }

        fun formateDateFromstring(inputFormat: String?, outputFormat: String?, inputDate: String?): String? {
            var parsed: Date? = null
            var outputDate = ""
            val df_input =
                SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output =
                SimpleDateFormat(outputFormat, Locale.getDefault())
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: ParseException) {
            }
            return outputDate
        }
    }
}
