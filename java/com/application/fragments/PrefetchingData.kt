package com.application.fragments

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.application.R
import com.application.databinding.PrefectingDataBinding
import java.io.InputStream

class PrefetchingData: Fragment(R.layout.prefecting_data) {
    lateinit var  binding : PrefectingDataBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PrefectingDataBinding.bind(view)
        val nightModeFlags = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES

        val imageStream: InputStream = if(!isNightMode){
            this.resources.openRawResource(R.raw.sell_zone)
        }else{
            this.resources.openRawResource(R.raw.sell_zone_night)
        }
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.image.setImageBitmap(bitmap)
    }

}