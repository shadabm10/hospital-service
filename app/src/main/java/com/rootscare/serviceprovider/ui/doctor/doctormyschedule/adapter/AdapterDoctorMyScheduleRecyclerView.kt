package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.api.response.doctor.myschedule.hospitallist.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorMyScheduleRecyclerviewBinding
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.adapter.AddapterDoctorMyAppointmentListRecyclerview
import kotlinx.android.synthetic.main.item_doctor_my_schedule_recyclerview.view.*
import java.util.*

class AdapterDoctorMyScheduleRecyclerView(internal var context: Context) :
    RecyclerView.Adapter<AdapterDoctorMyScheduleRecyclerView.ViewHolder>() {

    companion object {
        val TAG: String = AdapterDoctorMyScheduleRecyclerView::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var result: LinkedList<ResultItem> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorMyScheduleRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_my_schedule_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorMyScheduleRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position: Int = 0

        init {
            itemView.root.txtViewSchedule?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView.onItemClick(local_position)
            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos


            with(itemView) {
                if (result[local_position].name != null) {
                    txtViewSchedule.text = result[local_position].name
                }
            }

        }
    }

    fun filterList(dataList: LinkedList<ResultItem>) {
        result = dataList
        notifyDataSetChanged()
    }
}