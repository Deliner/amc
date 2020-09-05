package com.deliner.mosfauna.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Toast
//import com.deliner.apps.fauna.ClickableImageView
//import com.deliner.apps.fauna.R
//import com.deliner.apps.fauna.system.CoreConst
//import com.deliner.common.utils.StaticHandler
import java.io.InputStream


//class MapFragment : FaunaCommonFragment() {
//
//    private lateinit var mapImage1: ClickableImageView
//    private lateinit var mapImage2: ClickableImageView
//    private lateinit var mapImage3: ClickableImageView
//    private lateinit var mapImage4: ClickableImageView
//    private lateinit var mapImage5: ClickableImageView
//    private lateinit var mapImage6: ClickableImageView
//    private lateinit var mapImage7: ClickableImageView
//    private lateinit var mapImage8: ClickableImageView
//    private lateinit var mapImage9: ClickableImageView
//
//    override fun handleServiceMessage(msg: Message) {
//        when (msg.what) {
//            CoreConst.ON_IMAGE_CLICKED -> onImageClicked(msg.obj as Int)
//            else -> super.handleServiceMessage(msg)
//        }
//    }
//
//    private fun onImageClicked(index: Int) {
//        Toast.makeText(context, index.toString(), Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        mapImage1 = view.findViewById(R.id.fragment_map_image_first)
//        mapImage2 = view.findViewById(R.id.fragment_map_image_second)
//        mapImage3 = view.findViewById(R.id.fragment_map_image_third)
//        mapImage4 = view.findViewById(R.id.fragment_map_image_forth)
//        mapImage5 = view.findViewById(R.id.fragment_map_image_fifth)
//        mapImage6 = view.findViewById(R.id.fragment_map_image_six)
//        mapImage7 = view.findViewById(R.id.fragment_map_image_seventh)
//        mapImage8 = view.findViewById(R.id.fragment_map_image_eights)
//        mapImage9 = view.findViewById(R.id.fragment_map_image_ninth)
//
//        arrayOf(
//            mapImage1,
//            mapImage2,
//            mapImage3,
//            mapImage4,
//            mapImage5,
//            mapImage6,
//            mapImage7,
//            mapImage8,
//            mapImage9,
//        ).forEachIndexed { index, imageView ->
//            imageView.setHandler(handler)
//            imageView.setImageId(index)
//        }
//
//        mapOf(
//            mapImage1 to "map1.png",
//            mapImage2 to "map2.png",
//            mapImage3 to "map3.png",
//            mapImage4 to "map4.png",
//            mapImage5 to "map5.png",
//            mapImage6 to "map6.png",
//            mapImage7 to "map7.png",
//            mapImage8 to "map8.png",
//            mapImage9 to "map9.png",
//        ).forEach {
//            try {
//                val inputStream: InputStream = requireActivity().assets.open(it.value)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                it.key.setImageBitmap(bitmap)
//            } catch (e: Exception) {
//            }
//        }
//    }
//
//    override fun getLayoutResource(): Int = R.layout.fragment_map
//
//    override fun onPause() {
//        handler.dropCallback(this)
//        super.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        handler.setCallback(this)
//    }
//
//    companion object {
//        val handler = StaticHandler()
//    }
//}