package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.api.response.doctor.myschedule.timeslotlist.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorMyScheduleRecyclerviewBinding
import com.rootscare.serviceprovider.databinding.ItemDoctorViewScheduleListBinding
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.adapter.AddapterDoctorMyAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.adapter.AdapterDoctorMyScheduleRecyclerView
import kotlinx.android.synthetic.main.item_doctor_view_schedule_list.view.*
import java.util.*

class AdapterDoctoeViewScheduleRecyclerview(internal var context: Context) :
    RecyclerView.Adapter<AdapterDoctoeViewScheduleRecyclerview.ViewHolder>() {
    companion object {
        val TAG: String = AdapterDoctoeViewScheduleRecyclerview::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var result: LinkedList<ResultItem> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorViewScheduleListBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_view_schedule_list, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorViewScheduleListBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position: Int = 0

        init {
            itemView.root.btn_remove?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView.onItemClick(local_position)
            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            with(itemView) {
                if (result[local_position].day != null) {
                    tvSlotName.setText(result[local_position].day)
                }
                if (result[local_position].timeFrom != null) {
                    tvStartTime.setText(result[local_position].timeFrom)
                }
                if (result[local_position].timeTo != null) {
                    tvEndTime.setText(result[local_position].timeTo)
                }
            }
        }
    }

}