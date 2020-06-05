package com.rootscare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rootscare.serviceprovider.ui.login.subfragment.forgotpassword.FragmentForGotPassword
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree.FragmentRegistrationStepThree
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo.FragmentRegistrationStepTwo


class MyAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    val NUMBER_OF_PAGES = 5
    override fun getCount(): Int {
        return NUMBER_OF_PAGES
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentLogin.newInstance()
            1 ->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentRegistration.newInstance()
            2->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentRegistrationStepTwo.newInstance()

            3->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentRegistrationStepThree.newInstance()
            4->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentForGotPassword.newInstance()
            else -> FragmentLogin.newInstance()
        }
    }
}

