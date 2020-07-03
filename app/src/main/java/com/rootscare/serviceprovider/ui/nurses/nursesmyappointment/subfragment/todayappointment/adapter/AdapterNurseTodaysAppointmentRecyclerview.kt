package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.api.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
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

class AdapterNurseTodaysAppointmentRecyclerview (internal var todaysAppointList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterNurseTodaysAppointmentRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterNurseTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//

    internal lateinit var recyclerViewItemClickWithView2: OnClickOfDoctorAppointment2
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


    inner class ViewHolder(internal var itemVie: ItemDoctorTodaysAppointmentrecyclerviewBinding) :
        RecyclerView.ViewHolder(itemVie.root) {

        private var local_position: Int = 0

        init {
            with(itemVie) {
                btnViewDetails.setOnClickListener(View.OnClickListener {
                    recyclerViewItemClickWithView2.onItemClick(local_position)
                })
                btnCompleted.setOnClickListener {
                    recyclerViewItemClickWithView2.onAcceptBtnClick(local_position.toString(), "")
                }
                btnReject.setOnClickListener(View.OnClickListener {
                    recyclerViewItemClickWithView2.onRejectBtnBtnClick(local_position.toString(), "Reject")
                })
            }
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

            with(itemVie) {
                if (todaysAppointList?.get(pos)?.orderId != null && !todaysAppointList?.get(pos)?.orderId.equals("")) {
                    txtTodaysAppointment?.text = todaysAppointList?.get(pos)?.orderId
                } else {
                    txtTodaysAppointment?.text = ""
                }

                if (todaysAppointList?.get(pos)?.patientName != null && !todaysAppointList?.get(pos)?.patientName.equals("")) {
                    txtTodaysPatientName?.text = todaysAppointList?.get(pos)?.patientName
                } else {
                    txtTodaysPatientName?.text = ""
                }
                if (todaysAppointList?.get(pos)?.bookingDate != null && !todaysAppointList?.get(pos)?.bookingDate.equals("")) {
                    txtTodaysBookingDate?.text =
                        formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", todaysAppointList?.get(pos)?.bookingDate)
                } else {
                    txtTodaysBookingDate?.text = ""
                }
                if (todaysAppointList?.get(pos)?.fromTime != null && !todaysAppointList?.get(pos)?.fromTime.equals("")) {
                    startTime = todaysAppointList?.get(pos)?.fromTime!!
                } else {
                    startTime = ""
                }

                if (todaysAppointList?.get(pos)?.toTime != null && !todaysAppointList?.get(pos)?.toTime.equals("")) {
                    endTime = todaysAppointList?.get(pos)?.toTime!!
                } else {
                    endTime = ""
                }

                txtTodaysTime?.text = startTime + "-" + endTime
                if (todaysAppointList?.get(pos)?.appointmentDate != null && !todaysAppointList?.get(pos)?.appointmentDate.equals("")) {
                    txtTodaysAppointmentDate?.text =
                        formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", todaysAppointList?.get(pos)?.appointmentDate)
                } else {
                    txtTodaysAppointmentDate?.text = ""
                }

                if (todaysAppointList?.get(pos)?.uploadPrescription != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.uploadPrescription?.trim())) {
//                itemView.btnCompleted.visibility = View.GONE
                } else {
                    if (todaysAppointList?.get(pos)?.appointmentStatus != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.appointmentStatus?.trim()) &&
                        todaysAppointList?.get(pos)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("completed")!!
                    ) {
                        btnCompleted.visibility = View.GONE
                        btnReject.visibility = View.GONE
                    } else {
                        btnCompleted.visibility = View.VISIBLE
                        btnCompleted.setText("Complete")
                        btnCompleted.setOnClickListener {
                            recyclerViewItemClickWithView2.onAcceptBtnClick(local_position.toString(), "")
                        }
                        if (todaysAppointList?.get(pos)?.acceptanceStatus != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.acceptanceStatus?.trim()) &&
                            todaysAppointList?.get(pos)?.acceptanceStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!!
                        ) {
                            btnReject.visibility = View.GONE
                        } else {
                            btnReject.visibility = View.VISIBLE
                        }
                    }
                }

            }
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
