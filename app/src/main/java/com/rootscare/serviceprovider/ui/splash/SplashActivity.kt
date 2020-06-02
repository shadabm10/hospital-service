package com.rootscare.serviceprovider.ui.splash

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.rootscare.data.model.api.response.loginresponse.LoginResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivitySplashBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.LoginActivity

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityViewModel>(), SplashActivityNavigator {
    private var activitySplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashActivityViewModel? = null
    private val SPLASH_DISPLAY_LENGTH = 1000
    var loginresponse: LoginResponse? =null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_splash

    override val viewModel: SplashActivityViewModel
        get() {
            splashViewModel = ViewModelProviders.of(this).get(SplashActivityViewModel::class.java!!)
            return splashViewModel as SplashActivityViewModel
        }
    companion object {
        val TAG = SplashActivity::class.java.simpleName

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, SplashActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel!!.navigator = this
        activitySplashBinding = viewDataBinding

//


        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            runOnUiThread {
                //     startActivity(IntroActivity.newIntent(this@SplashActivity))
//                if (splashViewModel?.appSharedPref?.isIntoPageOn!!) {
//                    startActivity(IntroActivity.newIntent(this@SplashActivity))
//                } else{
//
//                    //startActivity(LoginActivity.newIntent(this@SplashActivity))
//                    if (!splashViewModel?.appSharedPref?.userId.equals("") && splashViewModel?.appSharedPref?.userId!=null){
//                        if(splashViewModel?.appSharedPref?.userType!=null){
//                            if(splashViewModel?.appSharedPref?.userType.equals("subscriber")){
//                                startActivity(HomeActivity.newIntent(this@SplashActivity))
//                            }else if(splashViewModel?.appSharedPref?.userType.equals("Trainer")){
//                                startActivity(TrainerHomeActivity.newIntent(this@SplashActivity))
//                            }
//                        }else{
//                            startActivity(ActivityLogInOption.newIntent(this@SplashActivity))
//                        }
//
//
//                    } else{
//                        startActivity(ActivityLogInOption.newIntent(this@SplashActivity))
//                    }
//
//                }
                if (splashViewModel?.appSharedPref?.loginUserType!=null && !splashViewModel?.appSharedPref?.loginUserType.equals("")){
                    if(!splashViewModel?.appSharedPref?.isloginremember.equals("") && splashViewModel?.appSharedPref?.isloginremember!=null){
                        if(splashViewModel?.appSharedPref?.isloginremember.equals("true")){
                            if(!splashViewModel?.appSharedPref?.loginUserType.equals("") && splashViewModel?.appSharedPref?.loginUserType.equals("doctor")){
                                startActivity(HomeActivity.newIntent(this@SplashActivity))
                                finish()
                            }

                        }else{
                            startActivity(LoginActivity.newIntent(this@SplashActivity))
                            finish()
                        }
                    }
                    else{
                        startActivity(LoginActivity.newIntent(this@SplashActivity))
                        finish()
                    }
                }else{
                    startActivity(LoginActivity.newIntent(this@SplashActivity))
                    finish()
                }


            }
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}

