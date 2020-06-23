package com.rootscare.dialog.timepicker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.DateTimePickerLayoutBinding
import com.rootscare.serviceprovider.ui.base.BaseDialog


class TimePickerDialog(internal var activity: Context, callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect) :
        BaseDialog(), TimeNavigator, View.OnClickListener {


    interface CallbackAfterDateTimeSelect {
        fun selectDateTime(dateTime: String)
    }


    companion object {
        private val TAG = TimePickerDialog::class.java.simpleName
    }

    private var selectedDate: String? = null
    private var callbackAfterDateTimeSelect: CallbackAfterDateTimeSelect? = callbackAfterDateTimeSelect
    private var dateAndTime = ""
    private lateinit var layoutBinding: DateTimePickerLayoutBinding
    private var forgotPasswordViewModel: TimePickerViewModel? = null


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.date_time_picker_layout, null, false)
        // setting viewmodel to layout bind
        forgotPasswordViewModel = ViewModelProviders.of(this).get(TimePickerViewModel::class.java)
        layoutBinding.setVariable(BR.viewModel, forgotPasswordViewModel)
        forgotPasswordViewModel?.navigator = this
        isCancelable = false

        with(layoutBinding) {
            tvTitle.text = activity.resources?.getString(R.string.select_time)
            datePicker.visibility = View.VISIBLE
            tvSubmit.setOnClickListener(this@TimePickerDialog)
            tvCancel.setOnClickListener(this@TimePickerDialog)

            datePicker.visibility = View.GONE
            timePicker.visibility = View.VISIBLE


        }

        return layoutBinding.root
    }


    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onClick(v: View?) {
        with(layoutBinding) {
            if (v!!.id == tvSubmit.id) {
                dateAndTime = getTimeFromTimePicker()
                callbackAfterDateTimeSelect?.selectDateTime(dateAndTime.trim())
                dismiss()

            }
            if (v.id == tvCancel.id) {
                callbackAfterDateTimeSelect?.selectDateTime("")
                dismiss()
            }
        }
    }


    private fun getTimeFromTimePicker(): String {
        var hour: Int = layoutBinding.timePicker.currentHour
        val min: Int = layoutBinding.timePicker.currentMinute
        val format: String
        when {
            hour == 0 -> {
                hour += 12
                format = "AM"
            }
            hour == 12 -> {
                format = "PM"
            }
            hour > 12 -> {
                hour -= 12
                format = "PM"
            }
            else -> {
                format = "AM"
            }
        }

        var finalValue = ""
        finalValue += if (hour in 0..9) {
            "0${hour}"
        } else {
            "$hour"
        }
        finalValue += if (min in 0..9) {
            ":0${min}"
        } else {
            ":${min}"
        }
        finalValue += " $format"
        return finalValue
    }
}