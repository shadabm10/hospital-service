package com.rootscare.serviceprovider.ui.doctor.profile.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.api.response.doctor.profileresponse.QualificationDataItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorImportantDocumentRecyclerviewBinding
import com.rootscare.serviceprovider.databinding.ItemNursesImportantDocumentListRecyclerviewBinding
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter.AdapterNursesUploadDocument
import kotlinx.android.synthetic.main.item_doctor_important_document_recyclerview.view.*

class AdapterDoctorImportantDocumentrecyclerview   (val qualificationDataList: ArrayList<QualificationDataItem>?,internal var context: Context) : RecyclerView.Adapter<AdapterDoctorImportantDocumentrecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorImportantDocumentrecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorImportantDocumentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_important_document_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return qualificationDataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorImportantDocumentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.parentLayout?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(local_position)
            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            itemView?.txt_description?.setText(qualificationDataList?.get(pos)?.qualification)

        }
    }

}