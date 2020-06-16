package com.rootscare.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.api.response.deaprtmentlist.ResultItem
import com.rootscare.interfaces.OnDepartmentDropDownListItemClickListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDepartmentDropdownBinding
import kotlinx.android.synthetic.main.item_department_dropdown.view.*


class AdapterDropdownDepartment (var departmentList: ArrayList<ResultItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterDropdownDepartment.ViewHolder>() {
    //
    companion object {
        val TAG: String = AdapterCommonDropdown::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClick: OnDepartmentDropDownListItemClickListener
//
//        internal lateinit var recyclerViewItemClickWithView: OnClickWithTwoButton

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDepartmentDropdownBinding>(
            LayoutInflater.from(context),
            R.layout.item_department_dropdown, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return dropdownList!!.size
            return departmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDepartmentDropdownBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.checkbox_department?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
               override fun onCheckedChanged(
                    buttonView: CompoundButton?,
                    isChecked: Boolean
                ) { //set your object's last status
                 //  notifyDataSetChanged()
                   if(isChecked){

                       departmentList?.get(local_position)?.isChecked="true"
                     //  notifyDataSetChanged()
//                       recyclerViewItemClick.onConfirm(departmentList)
                   }else{

                       departmentList?.get(local_position)?.isChecked="false"
                  //     notifyDataSetChanged()

//                       recyclerViewItemClick.onConfirm(departmentList)
                   }

                   recyclerViewItemClick.onConfirm(departmentList)

                }
            })


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            itemView?.rootView?.txtDropDownDepartment?.setText(departmentList?.get(local_position)?.title)
            if(departmentList?.get(local_position)?.isChecked.equals("false")){
                itemView?.rootView?.checkbox_department?.isChecked=false

            }else if(departmentList?.get(local_position)?.isChecked.equals("true")){
                itemView?.rootView?.checkbox_department?.isChecked=true
            }


        }
    }

}