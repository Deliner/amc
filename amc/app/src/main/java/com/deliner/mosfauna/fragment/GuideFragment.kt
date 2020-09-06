package com.deliner.mosfauna.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.deliner.mosfauna.R
import com.deliner.mosfauna.activity.BirdInfoActivity
import com.deliner.mosfauna.dialog.CommonDialogFragment
import com.deliner.mosfauna.dialog.DialogTags
import com.deliner.mosfauna.dialog.PlaceMarkerDialog
import com.deliner.mosfauna.system.CoreConst
import com.deliner.mosfauna.utils.Bird
import com.deliner.mosfauna.utils.MultiDrawable
import com.deliner.mosfauna.utils.StaticHandler
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashSet


class GuideFragment : CommonFragment(), OnMapReadyCallback,
    ClusterManager.OnClusterClickListener<Bird>,
    ClusterManager.OnClusterInfoWindowClickListener<Bird>,
    ClusterManager.OnClusterItemClickListener<Bird>,
    ClusterManager.OnClusterItemInfoWindowClickListener<Bird> {

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    private var mClusterManager: ClusterManager<Bird>? = null

    private var currentBirb: Bird? = null
    private var lastClickPos: LatLng? = null

    private val markerSet = HashSet<Marker>()

    private val timer = Timer()

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
        handler.setCallback(this)
        mMapView!!.onResume()
    }

    override fun onPause() {
        handler.dropCallback(this)
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
            if (currentBirb != null) {
                lastClickPos = it
                showDialogEx(DialogTags.PLACE_MARKER, bundleOf("KEY_NAME" to currentBirb!!.name))
            }
        }
        mClusterManager!!.setOnClusterClickListener(this)
        mClusterManager!!.setOnClusterInfoWindowClickListener(this)
        mClusterManager!!.setOnClusterItemClickListener(this)
        mClusterManager!!.setOnClusterItemInfoWindowClickListener(this)
        addItems()
        mClusterManager!!.cluster()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread {
                    markerSet.forEach { it.remove() }
                    markerSet.clear()
                    try {
                        runBlocking {
                            val result = HttpClient().get<String>(
                                host = "95.165.151.46",
                                port = 2378,
                                path = "/get_markers"
                            )
                            if (result.isNotEmpty()) {
                                markerSet.addAll(result.split("\n").map {
                                    val name = decodeBirdName(it.split("_")[0])
                                    val pos = it.split("_")[1]
                                    val lan = pos.split("x")[0].toDouble()
                                    val lgt = pos.split("x")[1].toDouble()
                                    googleMap!!.addMarker(
                                        MarkerOptions().position(LatLng(lan, lgt)).title(name)
                                    )
                                })
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }, 0, 5000)
    }

    override fun handleServiceMessage(msg: Message) {
        when (msg.what) {
            CoreConst.ON_SEND_MARKER -> sendMarker(currentBirb!!, lastClickPos!!)
            CoreConst.ON_CANCEL_MARKER -> {
                currentBirb = null
                lastClickPos = null
            }
            else -> super.handleServiceMessage(msg)
        }
    }

    private fun sendMarker(currentBird: Bird, clickPos: LatLng) {
        val marker = MarkerOptions().position(clickPos).title(currentBird.name)
        markerSet.add(googleMap!!.addMarker(marker))

        runBlocking {
            val response = HttpClient().get<HttpResponse>(
                host = "95.165.151.46",
                port = 2378,
                path = "/add_marker_${encodeBirdName(currentBird.name)}_${clickPos.latitude}x${clickPos.longitude}"
            )
            if (response.status.isSuccess()) {
                Toast.makeText(context, "Маркер отправлен", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Ошибка отправки маркера", Toast.LENGTH_SHORT).show()
            }
            currentBirb = null
            lastClickPos = null
        }
    }

    private fun encodeBirdName(name: String): String {
        return when (name) {
            "Обыкновенный гоголь" -> "1"
            "Ястреб-перепелятник" -> "2"
            "Ястреб-тетеревятник" -> "3"
            "Серая неясыть" -> "4"
            else -> "5"
        }
    }

    private fun decodeBirdName(name: String): String {
        return when (name) {
            "1" -> "Обыкновенный гоголь"
            "2" -> "Ястреб-перепелятник"
            "3" -> "Ястреб-тетеревятник"
            "4" -> "Серая неясыть"
            else -> "Ошибка"
        }
    }

    override fun onCreateDialogEx(tag: String, args: Bundle?): CommonDialogFragment {
        return when (tag) {
            DialogTags.PLACE_MARKER -> PlaceMarkerDialog.getInstance(args!!.getString("KEY_NAME")!!)
                .setDialogResult(handler)
            else -> super.onCreateDialogEx(tag, args)
        }
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
        currentBirb = item
        Toast.makeText(context, "Поставьте маркер, где вы видели животное", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onClusterItemInfoWindowClick(item: Bird) {
        val intent = Intent(activity, BirdInfoActivity::class.java)
        intent.putExtra("KEY_BIRD", item.name)
        intent.putExtra("KEY_PHOTO", item.photo)
        startActivity(intent)
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

    companion object {
        private val handler = StaticHandler()
    }
}