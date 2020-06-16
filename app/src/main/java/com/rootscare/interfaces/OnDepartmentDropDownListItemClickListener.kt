package com.rootscare.interfaces

import com.rootscare.data.model.api.response.deaprtmentlist.ResultItem

interface OnDepartmentDropDownListItemClickListener {
    fun onConfirm(departmentList: ArrayList<ResultItem?>?)
}