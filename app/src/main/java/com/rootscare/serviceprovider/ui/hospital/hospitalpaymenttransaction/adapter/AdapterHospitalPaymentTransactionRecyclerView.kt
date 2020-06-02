package com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalPaymentHistoryRecyclerviewBinding
import com.rootscare.serviceprovider.databinding.ItemHospitalSampleCollectionRecyclerviewBinding
import com.rootscare.serviceprovider.databinding.ItemNursesPaymentHistoryBinding
import com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory.adapter.AdapterDoctorPaymentHistoryRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.adapter.AdapterHospitalSampleCollectionRecyclerview
import com.rootscare.serviceprovider.ui.nurses.nursespaymenthistory.adapter.AdapterNursesPaymentHistory
import kotlinx.android.synthetic.main.item_doctor_paymenthistory_recyclerview.view.*
import kotlinx.android.synthetic.main.item_hospital_sample_collection_recyclerview.view.*

class AdapterHospitalPaymentTransactionRecyclerView( internal var context: Context) : RecyclerView.Adapter<AdapterHospitalPaymentTransactionRecyclerView.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalPaymentTransactionRecyclerView::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalPaymentHistoryRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_payment_history_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return 9
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalPaymentHistoryRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
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
            if(local_position==0){
                itemView?.rootView?.ll_header?.visibility= View.VISIBLE
                itemView?.rootView?.view_header?.visibility= View.VISIBLE
            }else{
                itemView?.rootView?.ll_header?.visibility= View.GONE
                itemView?.rootView?.view_header?.visibility= View.GONE
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
    }

}