package com.dzikirqu.android.base

import android.annotation.TargetApi
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.util.ViewUtils.getProgressDialog
import com.dzikirqu.android.util.main
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseFragment.Callback,
    BaseNavigator {

    lateinit var viewDataBinding: T
        private set

    abstract val bindingVariable: Int

    @get:LayoutRes
    abstract val layoutId: Int
    private var pDialog: Dialog? = null

    abstract val viewModel: V


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun onApplyWindowEvent(insets: Insets) {

    }

    override fun toast(message: String?) {
        main{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initShakeToDebugApiCall() {
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val shakeDetector = ShakeDetector {
//            startActivity(Chuck.getLaunchIntent(this))
//        }
//        shakeDetector.start(sensorManager)
    }

    override fun finish() {
        super.finish()
    }

    override fun onSettingsEvent() {

    }

    fun delay(delay: Long, runnable: () -> Unit) {
        Handler().postDelayed({
            runnable.invoke()
        }, delay)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pDialog = getProgressDialog("Please Wait")
        performDataBinding()
        initShakeToDebugApiCall()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hideLoading() {
        CoroutineScope(Main).launch {
            if (pDialog?.isShowing == true) pDialog?.dismiss()
        }
    }

    override fun showLoading() {
        CoroutineScope(Main).launch {
            if (pDialog?.isShowing == false) pDialog?.show()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun setStatusbarColor(color:Int){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
        setStatusbarColor()
    }

    fun setStatusbarColor(){
        when(Prefs.appTheme){
            AppCompatDelegate.MODE_NIGHT_NO->{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                } else {
                    window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    } else {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
            AppCompatDelegate.MODE_NIGHT_YES->{

            }
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM->{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                } else {
                    window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    } else {
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        }
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        viewDataBinding.apply {
            setVariable(bindingVariable, viewModel)
            executePendingBindings()
        }
    }
}

