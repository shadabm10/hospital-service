package com.rootscare.serviceprovider.ui.login.subfragment.registration

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLoginBinding
import com.rootscare.serviceprovider.databinding.FragmentRegistrationBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentDoctorAppointmentDetails
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLoginNavigator
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLoginViewModel
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo.FragmentRegistrationStepTwo

class FragmentRegistration : BaseFragment<FragmentRegistrationBinding, FragmentRegistrationviewModel>(),
    FragmentRegistrationNavigator {
    private var fragmentRegistrationBinding: FragmentRegistrationBinding? = null
    private var fragmentRegistrationviewModel: FragmentRegistrationviewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration
    override val viewModel: FragmentRegistrationviewModel
        get() {
            fragmentRegistrationviewModel = ViewModelProviders.of(this).get(FragmentRegistrationviewModel::class.java!!)
            return fragmentRegistrationviewModel as FragmentRegistrationviewModel
        }
    companion object {
        val TAG = FragmentRegistration::class.java.simpleName
        fun newInstance(): FragmentRegistration {
            val args = Bundle()
            val fragment = FragmentRegistration()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRegistrationviewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRegistrationBinding = viewDataBinding
        var dropdownlist= ArrayList<String?>()
        dropdownlist.add("Doctor")
        dropdownlist.add("Nurses")
        dropdownlist.add("Caregiver")
        dropdownlist.add("Hospital")
        dropdownlist.add("Babysitter")
        dropdownlist.add("Physiotherapy")
        dropdownlist.add("Lab Technician")
        fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Select User Type",dropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentRegistrationBinding?.txtRootscareRegistrationUserType?.setText(text)
                }

            })
        })

        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSteponeContinue?.setOnClickListener(
            View.OnClickListener {

                fragmentRegistrationBinding?.llRegistrationSetpOne?.visibility=View.GONE
                fragmentRegistrationBinding?.llRegistrationSetpTwo?.visibility=View.VISIBLE
                fragmentRegistrationBinding?.llRegistrationSetpThree?.visibility=View.GONE
            })

        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSteptwoContinue?.setOnClickListener(
            View.OnClickListener {
                fragmentRegistrationBinding?.llRegistrationSetpOne?.visibility=View.GONE
                fragmentRegistrationBinding?.llRegistrationSetpTwo?.visibility=View.GONE
                fragmentRegistrationBinding?.llRegistrationSetpThree?.visibility=View.VISIBLE
            })

        fragmentRegistrationBinding?.btnRooscareServiceproviderRegistrationSubmit?.setOnClickListener(
            View.OnClickListener {
//                fragmentRegistrationBinding?.llRegistrationSetpOne?.visibility=View.GONE
//                fragmentRegistrationBinding?.llRegistrationSetpTwo?.visibility=View.GONE
//                fragmentRegistrationBinding?.llRegistrationSetpThree?.visibility=View.VISIBLE

            })
    }

}