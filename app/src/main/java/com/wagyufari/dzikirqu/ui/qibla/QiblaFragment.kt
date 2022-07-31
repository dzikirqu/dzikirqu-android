package com.wagyufari.dzikirqu.ui.qibla

import android.content.Intent
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentQiblaBinding
import com.wagyufari.dzikirqu.util.qibla.Compass
import com.wagyufari.dzikirqu.util.qibla.SOTWFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@AndroidEntryPoint
class QiblaFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentQiblaBinding

    var currentAzimuth = 0f
    lateinit var compass:Compass
    lateinit var formatter:SOTWFormatter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentQiblaBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCompass()
        configureViews()
        formatter = SOTWFormatter(requireActivity())
    }

    fun configureViews(){
        binding.qiblafinder.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://qiblafinder.withgoogle.com/"))
            startActivity(browserIntent)
        }
    }

    private fun adjustArrow(azimuth: Float) {
        val kaabaLng =
            39.826206 // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
        val kaabaLat =
            Math.toRadians(21.422487) // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
        val myLatRad = Math.toRadians(Prefs.userCoordinates.latitude)
        val longDiff = Math.toRadians(kaabaLng - 107.446274)
        val y = sin(longDiff) * cos(kaabaLat)
        val x =
            cos(myLatRad) * sin(kaabaLat) - sin(myLatRad) * cos(kaabaLat) * cos(
                longDiff
            )
        val result = (Math.toDegrees(atan2(y, x)) + 360) % 360
        binding.arrow.rotation = result.toFloat()
        binding.sotwText.text = formatter.format(azimuth)
        binding.qiblaDirection.text = "${Prefs.userCity}: ${result.toInt()}°"

        val an: Animation = RotateAnimation(
            -currentAzimuth, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 10
        an.repeatCount = 0
        an.fillAfter = true
        binding.compassView.startAnimation(an)
    }

    private fun setupCompass() {
        compass = Compass(requireActivity())
        val cl: Compass.CompassListener = getCompassListener()
        compass.setListener(cl)
    }

    private fun getCompassListener(): Compass.CompassListener {
        return object:Compass.CompassListener{
            override fun onNewAzimuth(azimuth: Float) {
                CoroutineScope(Main).launch {
                    adjustArrow(azimuth)
                }
            }
            override fun onAccuracyChanged(accuracy: Int) {
                when(accuracy){
                    SensorManager.SENSOR_STATUS_ACCURACY_LOW->{
                        binding.compassView.visibility = View.INVISIBLE
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM->{
                        binding.compassView.visibility = View.INVISIBLE
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH->{
                        binding.compassView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        compass.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        compass.stop()
    }

    override fun onPause() {
        super.onPause()
        compass.stop()
    }

    override fun onResume() {
        super.onResume()
        compass.start()
    }

    override fun onStop() {
        super.onStop()
        compass.stop()
    }

}