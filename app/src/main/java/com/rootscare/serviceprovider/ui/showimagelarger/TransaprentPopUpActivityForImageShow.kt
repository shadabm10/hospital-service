package com.rootscare.serviceprovider.ui.showimagelarger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityTransparentForShowBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity


class TransaprentPopUpActivityForImageShow : BaseActivity<ActivityTransparentForShowBinding, TransparentActivityForShowViewModel>(),
    TransparentActivityForShowNavigator {

    companion object {
        private val TAG = TransaprentPopUpActivityForImageShow::class.java.simpleName
        fun newIntent(activity: Activity, PassData: String): Intent {
            return Intent(activity, TransaprentPopUpActivityForImageShow::class.java)
                .putExtra("PassData", PassData)
        }

    }

    private var fileUrl: String? = null
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    private var activityDoctorListingBinding: ActivityTransparentForShowBinding? = null
    private var doctorListingViewModel: TransparentActivityForShowViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_transparent_for_show
    override val viewModel: TransparentActivityForShowViewModel
        get() {
            doctorListingViewModel = ViewModelProviders.of(this).get(TransparentActivityForShowViewModel::class.java)
            return doctorListingViewModel as TransparentActivityForShowViewModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doctorListingViewModel?.navigator = this
        activityDoctorListingBinding = viewDataBinding

        with(activityDoctorListingBinding!!) {
            imageViewCross.setOnClickListener {
                onBackPressed()
            }

            fileUrl = intent?.extras?.getString("PassData")
            if (fileUrl != null) {
                val circularProgressDrawable = CircularProgressDrawable(this@TransaprentPopUpActivityForImageShow)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.setColorSchemeColors(*intArrayOf(R.color.green, R.color.green_light_1))
                circularProgressDrawable.start()
                val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(this@TransaprentPopUpActivityForImageShow).load(fileUrl).
                apply(RequestOptions().placeholder(circularProgressDrawable)).timeout(1000*60).apply(requestOptions).into(imageviewShow)
                imageviewShow.visibility = View.VISIBLE
            }

            mScaleGestureDetector = ScaleGestureDetector(this@TransaprentPopUpActivityForImageShow, ScaleListener())
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
        mScaleGestureDetector?.onTouchEvent(event)
        return true
    }


    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
//            mScaleFactor *= scaleGestureDetector.scaleFactor
//            mScaleFactor = Math.max(0.1f,
//                    Math.min(mScaleFactor, 10.0f))
//            activityDoctorListingBinding?.imageviewShow?.scaleX = mScaleFactor
//            activityDoctorListingBinding?.imageviewShow?.scaleY = mScaleFactor
            return true
        }
    }

}
