package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.data.model.api.response.loginresponse.Result
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesManageRateBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.subfragment.FragmentNurseHome
import okhttp3.RequestBody

class FragmentNursesManageRate : BaseFragment<FragmentNursesManageRateBinding, FragmentNursesManageRateViewModel>(),
    FragmentNursesManageRateNavigator {
    private var fragmentNursesManageRateBinding: FragmentNursesManageRateBinding? = null
    private var fragmentNursesManageRateViewModel: FragmentNursesManageRateViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_manage_rate
    override val viewModel: FragmentNursesManageRateViewModel
        get() {
            fragmentNursesManageRateViewModel = ViewModelProviders.of(this).get(
                FragmentNursesManageRateViewModel::class.java
            )
            return fragmentNursesManageRateViewModel as FragmentNursesManageRateViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesManageRate {
            val args = Bundle()
            val fragment = FragmentNursesManageRate()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesManageRateViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesManageRateBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentEditDoctorProfile.newInstance())
//        })

        with(fragmentNursesManageRateBinding!!) {
            btnSubmit1.setOnClickListener {
                if (checkValidation()) {
                    apiHitToSaveSlotPrice()
                }
            }
        }

        with(fragmentNursesManageRateBinding!!) {
            btnSubmit2.setOnClickListener {
                if (checkValidation2()) {
                    apiHitToSaveHourlyPrice()
                }
            }
        }
    }


    private fun apiHitToSaveSlotPrice() {
        with(fragmentNursesManageRateBinding!!) {
            baseActivity?.showLoading()
            var jsonObject = JsonObject()
            jsonObject.addProperty("user_id", fragmentNursesManageRateViewModel?.appSharedPref?.loginUserId!!)
            jsonObject.addProperty("slot_1", tvTitleForPrice1.text.toString().trim())
            jsonObject.addProperty("amount_1", etForPrice1.text.toString().trim())
            jsonObject.addProperty("slot_2", tvTitleForPrice2.text.toString().trim())
            jsonObject.addProperty("amount_2", etForPrice2.text.toString().trim())
            jsonObject.addProperty("slot_3", tvTitleForPrice3.text.toString().trim())
            jsonObject.addProperty("amount_3", etForPrice3.text.toString().trim())
            jsonObject.addProperty("slot_4", tvTitleForPrice4.text.toString().trim())
            jsonObject.addProperty("amount_4", etForPrice4.text.toString().trim())

            val body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonObject.toString())
            fragmentNursesManageRateViewModel?.savePrice(body)
        }
    }

    private fun checkValidation(): Boolean {
        with(fragmentNursesManageRateBinding!!) {
            if (!TextUtils.isEmpty(etForPrice1.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice2.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice3.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice4.text.toString().trim())
            ) {
               return true
            }else{
                Toast.makeText(activity!!, "Please enter price for at those slot above", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    override fun onSuccessAfterSavePrice(loginResponse: LoginResponse) {
        baseActivity?.hideLoading()
        if (loginResponse.code.equals("200")) {
            if (loginResponse.result?.hourlyRates != null && loginResponse.result.hourlyRates.size > 0) {
                fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin = Gson().toJson(loginResponse.result)
                activity?.startActivity(activity?.let { NursrsHomeActivity.newIntent(it) })
            }
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }


    private fun apiHitToSaveHourlyPrice() {
        with(fragmentNursesManageRateBinding!!) {
            baseActivity?.showLoading()
            var jsonObject = JsonObject()
            jsonObject.addProperty("user_id", fragmentNursesManageRateViewModel?.appSharedPref?.loginUserId!!)
            jsonObject.addProperty("daily_rate", etForPrice5.text.toString().trim())

            val body = RequestBody.create(okhttp3.MediaType.parse("application/json"), jsonObject.toString())
            fragmentNursesManageRateViewModel?.saveHourlyPrice(body)
        }
    }

    private fun checkValidation2(): Boolean {
        with(fragmentNursesManageRateBinding!!) {
            if (TextUtils.isEmpty(etForPrice5.text.toString().trim())) {
                Toast.makeText(activity!!, "Please enter price for hourly rate below", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }


    private fun showDataoField(){
        with(fragmentNursesManageRateBinding!!){
            if (fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin!=null){
                val nurseDataAfterLogIn = Gson().fromJson(fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin, Result::class.java)
                if (nurseDataAfterLogIn?.hourlyRates!=null && nurseDataAfterLogIn.hourlyRates.size>0){
                    for (i in 0 until nurseDataAfterLogIn.hourlyRates.size){
                        when (i){
                            0 -> etForPrice1.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            1 -> etForPrice2.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            2 -> etForPrice3.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            3 -> etForPrice4.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                        }
                    }
                }
                if (nurseDataAfterLogIn?.dailyRate!=null && !TextUtils.isEmpty(nurseDataAfterLogIn.dailyRate.trim())){
                    etForPrice5.setText(nurseDataAfterLogIn?.dailyRate)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showDataoField()
    }
}