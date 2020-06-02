package com.rootscare.serviceprovider.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.rootscare.adapter.MyAdapter
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityLoginBinding
import com.rootscare.serviceprovider.databinding.ActivitySplashBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.splash.SplashActivityNavigator
import com.rootscare.serviceprovider.ui.splash.SplashActivityViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>(),
    LoginActivityNavigator {
    private var ativityLoginBinding: ActivityLoginBinding? = null
    private var loginActivityViewModel: LoginActivityViewModel? = null
    var mAdapter: MyAdapter? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginActivityViewModel
        get() {
            loginActivityViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java!!)
            return loginActivityViewModel as LoginActivityViewModel
        }

    companion object {
        val TAG = LoginActivity::class.java.simpleName

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, LoginActivity::class.java)
        }
        private var fragment_open_container: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityViewModel!!.navigator = this
        ativityLoginBinding = viewDataBinding
        mAdapter = MyAdapter(getSupportFragmentManager());
//        mPager = findViewById(R.id.viewpager);
        ativityLoginBinding?.viewPager?.setAdapter(mAdapter)
    }



    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        ativityLoginBinding?.viewPager?.setCurrentItem(item, smoothScroll)
    }

    override fun onBackPressed() {

        if (ativityLoginBinding?.viewPager?.getCurrentItem() == 0) {
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//            Handler().postDelayed({
//                 }, 1000)

            super.onBackPressed()

        }else{
            this!!.setCurrentItem(0, true)
        }
    }
}