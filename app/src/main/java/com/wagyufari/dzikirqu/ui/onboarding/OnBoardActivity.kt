package com.wagyufari.dzikirqu.ui.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.ActivityOnBoardBinding
import com.wagyufari.dzikirqu.ui.adapters.FragmentPagerAdapter
import com.wagyufari.dzikirqu.ui.onboarding.fragments.OnBoardLanguageFragment
import com.wagyufari.dzikirqu.ui.onboarding.fragments.OnBoardLocationFragment
import com.wagyufari.dzikirqu.ui.onboarding.fragments.OnBoardPraytimeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnBoardActivity : BaseActivity<ActivityOnBoardBinding, OnBoardViewModel>(), OnBoardNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_on_board
    override val viewModel: OnBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.navigator = this
        configureViews()
    }

    val adapter = FragmentPagerAdapter(this, arrayListOf(
        OnBoardLanguageFragment(),
        OnBoardLocationFragment(),
        OnBoardPraytimeFragment(),
    ))

    fun configureViews(){
        viewDataBinding.pager.isUserInputEnabled = false
        viewDataBinding.pager.adapter = adapter
        viewModel.page.observe(this){
            adapter.notifyItemChanged(0)
            adapter.notifyItemChanged(1)
            adapter.notifyItemChanged(2)
            adapter.notifyItemChanged(3)
            viewDataBinding.pager.setCurrentItem(it, true)
        }
    }

//    private val LOCATION_PERMISSION_CODE = 93

//    @SuppressLint("MissingPermission")
//    private fun buildLocationPermission() {
//        if (!Prefs.isBookLoaded && !Prefs.isPrayerLoaded && !Prefs.isQuranLoaded) {
//            viewModel.isBookLoaded.observe(this) { result ->
//                if (result != ResultEnum.Success) return@observe
//                progressPrayer.fadeShow()
//                progressPrayer.animToY(0f, duration = 1000)
//                Prefs.isBookLoaded = true
//            }
//            viewModel.isPrayerLoaded.observe(this) { result ->
//                if (result != ResultEnum.Success) return@observe
//                progressQuran.fadeShow()
//                progressQuran.animToY(0f, duration = 1000)
//                Prefs.isPrayerLoaded = true
//            }
//            viewModel.isQuranLoaded.observe(this) { result ->
//                if (result != ResultEnum.Success) return@observe
//                Prefs.isQuranLoaded = true
//                if (!isLocationPermissionGranted()) {
//                    requestLocationPermission()
//                } else{
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
//            }
//        } else {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    }
//
//    private fun requestLocationPermission() {
//        ActivityCompat.shouldShowRequestPermissionRationale(
//            this,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            LOCATION_PERMISSION_CODE
//        )
//    }
//
//    fun isLocationPermissionGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun showRequestPermissionsInfoAlertDialog(makeSystemRequest: Boolean) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Location Permission") // Your own title
//        builder.setMessage("This app require the device's location in order to get accurate prayer time") // Your own message
//        builder.setPositiveButton("OK") { dialog, _ ->
//            dialog.dismiss()
//            if (makeSystemRequest) {
//                requestLocationPermission()
//            }
//        }
//        builder.setCancelable(false)
//        builder.show()
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
//            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                showRequestPermissionsInfoAlertDialog(true)
//            } else {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }
//        }
//    }

}