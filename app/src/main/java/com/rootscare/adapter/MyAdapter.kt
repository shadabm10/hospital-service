package com.rootscare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration


class MyAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    val NUMBER_OF_PAGES = 2
    override fun getCount(): Int {
        return NUMBER_OF_PAGES
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentLogin.newInstance()
            1 ->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentRegistration.newInstance()
            else -> FragmentLogin.newInstance()
        }
    }
}

