package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorTodaysAppointmentrecyclerviewBinding
import com.rootscare.serviceprovider.databinding.ItemDoctorUpcomingAppointmentRecyclerviewBinding
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.adapter.AdapterDoctorUpcommingAppointment
import kotlinx.android.synthetic.main.item_doctor_myappointment_recyclerview.view.*
import kotlinx.android.synthetic.main.item_doctor_todays_appointmentrecyclerview.view.*
import kotlinx.android.synthetic.main.item_doctor_upcoming_appointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterDoctorTodaysAppointmentRecyclerview (val todaysAppointList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterDoctorTodaysAppointmentRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var startTime=""
    var endTime=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemDoctorTodaysAppointmentrecyclerviewBinding>(
                LayoutInflater.from(context),
                R.layout.item_doctor_todays_appointmentrecyclerview, parent, false
            )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return todaysAppointList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorTodaysAppointmentrecyclerviewBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private var local_position: Int = 0

        init {
            itemView?.root?.crdview_todays_appointment_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(1)
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

            if(todaysAppointList?.get(pos)?.orderId!=null && !todaysAppointList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_todays_appointment?.setText(todaysAppointList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_todays_appointment?.setText("")
            }

            if(todaysAppointList?.get(pos)?.patientName!=null && !todaysAppointList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_todays_patient_name?.setText(todaysAppointList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_todays_patient_name?.setText("")
            }
            if(todaysAppointList?.get(pos)?.bookingDate!=null && !todaysAppointList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_todays_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",todaysAppointList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_todays_booking_date?.setText("")
            }
            if(todaysAppointList?.get(pos)?.fromTime!=null && !todaysAppointList?.get(pos)?.fromTime.equals("")){
                startTime= todaysAppointList?.get(pos)?.fromTime!!
            }else{
                startTime=""
            }

            if(todaysAppointList?.get(pos)?.toTime!=null && !todaysAppointList?.get(pos)?.toTime.equals("")){
                endTime= todaysAppointList?.get(pos)?.toTime!!
            }else{
                endTime=""
            }

            itemView?.rootView?.txt_todays_time?.setText(startTime+"-"+endTime)
            if(todaysAppointList?.get(pos)?.appointmentDate!=null && !todaysAppointList?.get(pos)?.appointmentDate.equals("")){
                itemView?.rootView?.txt_todays_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",todaysAppointList?.get(pos)?.appointmentDate))
            }else{
                itemView?.rootView?.txt_todays_appointment_date?.setText("")
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