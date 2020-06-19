package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.adapter

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.model.AddDoctorSlotTimeItmModel
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemAddScheduleRecyclerviewBinding
import com.sihatku.commondialog.timepicker.TimePickerDialog
import kotlinx.android.synthetic.main.item_add_schedule_recyclerview.view.*

class AdapterAddSlotPerDayRecyclerview ( internal var context: FragmentActivity) : RecyclerView.Adapter<AdapterAddSlotPerDayRecyclerview.ViewHolder>() {

    companion object {
        val TAG: String = AdapterAddSlotPerDayRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//

    constructor(context: FragmentActivity, productlistItem: ArrayList<AddDoctorSlotTimeItmModel?>?) : this(context) {
        this.productlistItem = productlistItem
    }

    var abc = -1
    var abcBools = false
    internal lateinit var onItemClikWithIdListener: OnItemClikWithIdListener
    var productlistItem: ArrayList<AddDoctorSlotTimeItmModel?>?  = ArrayList<AddDoctorSlotTimeItmModel?>()
    lateinit var singleItemDashboardListingBinding: ItemAddScheduleRecyclerviewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemAddScheduleRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_add_schedule_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return productlistItem!!.size
//        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemAddScheduleRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.img_remove?.setOnClickListener(View.OnClickListener {
                removeAt(local_position)
            })
        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(pos==0){
                itemView?.rootView?.img_remove?.visibility= View.GONE
            }else{
                itemView?.rootView?.img_remove?.visibility= View.VISIBLE
            }

            itemView?.rootView?.edt_add_schedule_slot?.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                    productlistItem?.get(adapterPosition)?.slot = charSequence.toString()
                }

                override fun afterTextChanged(editable: Editable) {}
            })

            itemView.rootView?.edt_add_schedule_starttime?.setOnClickListener {
                TimePickerDialog(context, object : TimePickerDialog.CallbackAfterDateTimeSelect {
                    override fun selectDateTime(dateTime: String) {
                        productlistItem!![local_position]?.stat_time = dateTime
                        notifyItemChanged(local_position)
                    }
                }).show(context.supportFragmentManager)
            }

            itemView.rootView?.edt_add_schedule_endtime?.setOnClickListener {
                TimePickerDialog(context, object : TimePickerDialog.CallbackAfterDateTimeSelect {
                    override fun selectDateTime(dateTime: String) {
                        productlistItem!![local_position]?.end_time = dateTime
                        notifyItemChanged(local_position)
                    }
                }).show(context.supportFragmentManager)
            }

            /*itemView?.rootView?.edt_add_schedule_starttime?.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        productlistItem?.get(adapterPosition)?.stat_time =
                            charSequence.toString()
                    }

                    override fun afterTextChanged(editable: Editable) {}
                })

            itemView?.rootView?.edt_add_schedule_endtime?.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        productlistItem?.get(adapterPosition)?.end_time =
                            charSequence.toString()
                    }

                    override fun afterTextChanged(editable: Editable) {}
                })*/

            if (productlistItem?.get(pos)?.slot != null) {
                itemView?.rootView?.edt_add_schedule_slot?.setText(
                    productlistItem?.get(
                        pos
                    )?.slot
                )
            }

            if (productlistItem?.get(pos)?.stat_time != null) {
                itemView?.rootView?.edt_add_schedule_starttime?.setText(
                    productlistItem?.get(pos)?.stat_time
                )
            }

            if (productlistItem?.get(pos)?.end_time != null) {
                itemView?.rootView?.edt_add_schedule_endtime?.setText(
                    productlistItem?.get(pos)?.end_time
                )
            }

//            if (abcBools){
//                if (TextUtils.isEmpty(productlistItem?.get(local_position)?.manager_name?.trim())) {
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerManagerName.requestFocus()
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerManagerName.setError("Please enter manager name")
//                }
//                if (TextUtils.isEmpty(productlistItem?.get(local_position)?.manager_contactnumber?.trim())) {
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerManagerContactnumber.setError("Contact number can not be blank")
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerManagerContactnumber.requestFocus()
//                }
//                abcBools = false
//            }

//            if (abc == local_position && abcBools) {
//                if (TextUtils.isEmpty(productlistItem?.get(abc)?.slot?.trim())) {
//                    singleItemDashboardListingBinding?.edt_add_schedule_slot.requestFocus()
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsName?.setError(null)
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsName.setError("Please enter eqipements name")
//
//                } else if (TextUtils.isEmpty(productlistItem?.get(abc)?.equpments_capacity?.trim())) {
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsCapacity.requestFocus()
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsCapacity?.setError(null)
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsCapacity.setError("Please enter eqipements cost")
//                    //
//                }else if (TextUtils.isEmpty(productlistItem?.get(abc)?.equpments_number?.trim())) {
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsNumberof.requestFocus()
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsNumberof?.setError(null)
//                    singleItemDashboardListingBinding?.edtLsfFarmdataFarmerEqupementsNumberof.setError("Please enter eqipements number")
//                    //
//                }
//            }

        }
    }


    fun addField(equipmentsItmModel: AddDoctorSlotTimeItmModel?) {
//        productlistItem?.add(productlistItem?.size!!, managerInfoItemModel)
//        notifyItemInserted(productlistItem!!.size - 1)

        productlistItem?.add(equipmentsItmModel)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        println("remove_pos: $position")
        productlistItem?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, productlistItem?.size!!)

    }

    fun geteEupmentsAdapterList(): ArrayList<AddDoctorSlotTimeItmModel?>? {
        val items: ArrayList<AddDoctorSlotTimeItmModel?> = ArrayList<AddDoctorSlotTimeItmModel?>()
        for (i in 0 until productlistItem?.size!!) {
//            if(TextUtils.isEmpty(productlistItem?.get(i)?.slot)){
//                Toast.makeText(context, "Slot can not be blank", Toast.LENGTH_SHORT).show()
//                break
//            }else if(TextUtils.isEmpty(productlistItem?.get(i)?.stat_time)){
//                Toast.makeText(context, "Start time can not be blank", Toast.LENGTH_SHORT).show()
//                break
//            }else if(TextUtils.isEmpty(productlistItem?.get(i)?.end_time)){
//                Toast.makeText(context, "End time can not be blank", Toast.LENGTH_SHORT).show()
//                break
//            }
//            else{
//                var equipmentsItmModel=AddDoctorSlotTimeItmModel(productlistItem?.get(i)?.slot!!,productlistItem?.get(i)?.stat_time!!,
//                    productlistItem?.get(i)?.end_time!!
//                )
//                items.add(equipmentsItmModel)
//            }
            var equipmentsItmModel=AddDoctorSlotTimeItmModel(productlistItem?.get(i)?.slot!!,productlistItem?.get(i)?.stat_time!!,
                productlistItem?.get(i)?.end_time!!
            )
            items.add(equipmentsItmModel)
        }
        return items
    }
}