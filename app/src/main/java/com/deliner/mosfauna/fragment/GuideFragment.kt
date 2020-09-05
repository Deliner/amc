package com.deliner.mosfauna.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.deliner.mosfauna.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.InputStream


class GuideFragment : Fragment() {

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_guide, container, false)
        mMapView = rootView.findViewById<View>(R.id.mapView) as MapView
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMapView!!.getMapAsync { mMap ->
            val user = LatLng(55.799334, 37.673134)
            val cameraPosition = CameraPosition.Builder().target(user).zoom(13f).build()



            val inputStream: InputStream = requireActivity().assets.open("gog.png")
            val bitmap = BitmapFactory.decodeStream(inputStream)

            googleMap = mMap
            googleMap!!.addMarker(MarkerOptions().position(LatLng(55.810348, 37.663642)).title("Обыкновенный гоголь").icon(bitmap.) )
            googleMap!!.addMarker(MarkerOptions().position(LatLng(55.799162, 37.655170)).title("Ястреб-перепелятник"))
            googleMap!!.addMarker(MarkerOptions().position(LatLng(55.807767, 37.684719)).title("Ястреб-тетеревятник"))
            googleMap!!.addMarker(MarkerOptions().position(LatLng(55.800969, 37.688343)).title("Серая неясыть"))
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

//    override fun getLayoutResource(): Int = R.layout.fragment_guide
}