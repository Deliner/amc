package com.deliner.mosfauna.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.deliner.mosfauna.R
import com.deliner.mosfauna.utils.Bird
import com.deliner.mosfauna.utils.MultiDrawable
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import java.util.*


class GuideFragment : CommonFragment(), OnMapReadyCallback, ClusterManager.OnClusterClickListener<Bird>,
    ClusterManager.OnClusterInfoWindowClickListener<Bird>,
    ClusterManager.OnClusterItemClickListener<Bird>,
    ClusterManager.OnClusterItemInfoWindowClickListener<Bird> {

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    private var mClusterManager: ClusterManager<Bird>? = null

    private var currentBirb: Bird? = null

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
        mMapView!!.getMapAsync(this)
        return rootView
    }

    override fun getLayoutResource(): Int { //IGNORE THIS
        TODO("Not yet implemented")
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

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        val user = LatLng(55.799334, 37.673134)
        val cameraPosition = CameraPosition.Builder().target(user).zoom(13f).build()
        googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        mClusterManager = ClusterManager<Bird>(context, googleMap)
        mClusterManager!!.renderer = BirdRenderer()
        googleMap!!.setOnCameraIdleListener(mClusterManager)
        googleMap!!.setOnMarkerClickListener(mClusterManager)
        googleMap!!.setOnInfoWindowClickListener(mClusterManager)
        googleMap!!.setOnMapClickListener {
            if (currentBirb != null){

//                show

                Toast.makeText(context, "ground", Toast.LENGTH_SHORT).show()
            }
        }
        mClusterManager!!.setOnClusterClickListener(this)
        mClusterManager!!.setOnClusterInfoWindowClickListener(this)
        mClusterManager!!.setOnClusterItemClickListener(this)
        mClusterManager!!.setOnClusterItemInfoWindowClickListener(this)
        addItems()
        mClusterManager!!.cluster()
    }

    override fun onClusterClick(cluster: Cluster<Bird>): Boolean {
        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }

        val bounds = builder.build()
        try {
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<Bird>) {
    }

    override fun onClusterItemClick(item: Bird): Boolean {
        return false
    }

    override fun onClusterItemInfoWindowClick(item: Bird) {
        Toast.makeText(context, "info", Toast.LENGTH_SHORT).show()

    }

    private fun addItems() {
        mClusterManager!!.addItem(
            Bird(
                LatLng(55.810348, 37.663642),
                "Обыкновенный гоголь",
                R.drawable.gog
            )
        )
        mClusterManager!!.addItem(
            Bird(
                LatLng(55.799162, 37.655170),
                "Ястреб-перепелятник",
                R.drawable.perepel
            )
        )
        mClusterManager!!.addItem(
            Bird(
                LatLng(55.807767, 37.684719),
                "Ястреб-тетеревятник",
                R.drawable.teterev
            )
        )
        mClusterManager!!.addItem(
            Bird(
                LatLng(55.800969, 37.688343),
                "Серая неясыть",
                R.drawable.neyas
            )
        )
    }

    private inner class BirdRenderer :
        DefaultClusterRenderer<Bird>(context, googleMap, mClusterManager) {
        private val mIconGenerator = IconGenerator(context)
        private val mClusterIconGenerator = IconGenerator(context)
        private val mImageView: ImageView
        private val mClusterImageView: ImageView
        private var mDimension = 0
        override fun onBeforeClusterItemRendered(person: Bird, markerOptions: MarkerOptions) {
            markerOptions
                .icon(getItemIcon(person))
                .title(person.name)
        }

        override fun onClusterItemUpdated(person: Bird, marker: Marker) {
            marker.setIcon(getItemIcon(person))
            marker.title = person.name
        }

        private fun getItemIcon(person: Bird): BitmapDescriptor {
            mImageView.setImageResource(person.photo)
            val icon = mIconGenerator.makeIcon()
            return BitmapDescriptorFactory.fromBitmap(icon)
        }

        override fun onBeforeClusterRendered(
            cluster: Cluster<Bird>,
            markerOptions: MarkerOptions
        ) {
            markerOptions.icon(getClusterIcon(cluster))
        }

        override fun onClusterUpdated(cluster: Cluster<Bird>, marker: Marker) {
            marker.setIcon(getClusterIcon(cluster))
        }

        private fun getClusterIcon(cluster: Cluster<Bird>): BitmapDescriptor {
            val profilePhotos: MutableList<Drawable> = ArrayList(4.coerceAtMost(cluster.size))
            val width = mDimension
            val height = mDimension
            for (p in cluster.items) {
                if (profilePhotos.size == 4) break
                val drawable: Drawable = ContextCompat.getDrawable(context!!, p.photo)!!
                drawable.setBounds(0, 0, width, height)
                profilePhotos.add(drawable)
            }
            val multiDrawable = MultiDrawable(profilePhotos)
            multiDrawable.setBounds(0, 0, width, height)
            mClusterImageView.setImageDrawable(multiDrawable)
            val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
            return BitmapDescriptorFactory.fromBitmap(icon)
        }

        override fun shouldRenderAsCluster(cluster: Cluster<Bird>): Boolean {
            return cluster.size > 1
        }

        init {
            val multiProfile: View = layoutInflater.inflate(R.layout.multi_profile, null)
            mClusterIconGenerator.setContentView(multiProfile)
            mClusterImageView = multiProfile.findViewById(R.id.image)
            mImageView = ImageView(context)
            mDimension = resources.getDimension(R.dimen.custom_profile_image).toInt()
            mImageView.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
            val padding = resources.getDimension(R.dimen.custom_profile_padding).toInt()
            mImageView.setPadding(padding, padding, padding, padding)
            mIconGenerator.setContentView(mImageView)
        }
    }
}