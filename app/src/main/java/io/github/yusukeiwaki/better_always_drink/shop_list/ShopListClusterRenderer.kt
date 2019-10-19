package io.github.yusukeiwaki.better_always_drink.shop_list

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.model.ServiceArea
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopListClusterRenderer(
    private val context: Context,
    private val googleMap: GoogleMap,
    private val clusterManager: ClusterManager<Shop>,
    private val mapViewModel: MapFragmentViewModel
) : DefaultClusterRenderer<Shop>(context, googleMap, clusterManager), GoogleMap.OnCameraIdleListener {
    private var serviceAreaList: List<ServiceArea>? = null
    private var lastZoomLevel: Float? = null
    private var selectedShop: Shop? = null
    private val defaultMarkerBitmap = createBitmapDescriptorFromVector(R.drawable.ic_place_default_24dp, context.getColor(R.color.markerDefault))
    private val selectedMarkerBitmap = createBitmapDescriptorFromVector(R.drawable.ic_local_cafe_24dp, context.getColor(R.color.markerSelected))

    companion object {
        // クラスター表示とアイコン表示の境目となるズームレベル
        val ZOOM_THRESHOLD = 10.0f
    }

    override fun onCameraIdle() {
        val cameraPosition = googleMap.cameraPosition
        mapViewModel.onLatLngChanged(cameraPosition.target)
        mapViewModel.onZoomLevelChanged(cameraPosition.zoom)
    }

    fun updateServiceAreaList(newServiceAreaList: List<ServiceArea>) {
        serviceAreaList = newServiceAreaList
    }

    fun updateSelectedShop(shop: Shop?) {
        if (selectedShop != shop) {
            selectedShop?.let { oldSelectedShop ->
                getMarker(oldSelectedShop)?.apply {
                    setIcon(defaultMarkerBitmap)
                    hideInfoWindow()
                }
            }
            shop?.let { newSelectedShop ->
                getMarker(newSelectedShop)?.apply {
                    setIcon(selectedMarkerBitmap)
                    showInfoWindow()
                }
            }
            selectedShop = shop
        }
    }

    override fun shouldRenderAsCluster(cluster: Cluster<Shop>?): Boolean {
        return mapViewModel.lastZoomLevelValue?.let { it < ZOOM_THRESHOLD } ?: true
    }

    override fun onBeforeClusterRendered(cluster: Cluster<Shop>?, markerOptions: MarkerOptions?) {
        val nearestServiceArea = nearestServiceAreaFor(cluster)
        if (nearestServiceArea == null) {
            super.onBeforeClusterRendered(cluster, markerOptions)
            return
        }

        markerOptions?.apply {
            icon(BitmapDescriptorFactory.fromBitmap(IconGenerator(context).makeIcon(nearestServiceArea.name)))
        }
    }

    fun nearestServiceAreaFor(cluster: Cluster<Shop>?): ServiceArea? {
        val clusterPosition = cluster?.position
        val currentServiceAreaList = serviceAreaList
        if (clusterPosition == null || currentServiceAreaList == null) {
            return null
        }

        return currentServiceAreaList.minBy { area ->
            (area.lat - clusterPosition.latitude) * (area.lat - clusterPosition.latitude) + (area.lng - clusterPosition.longitude) * (area.lng - clusterPosition.longitude)
        }
    }

    override fun onBeforeClusterItemRendered(item: Shop?, markerOptions: MarkerOptions?) {
        item?.let { shop ->
            markerOptions?.apply {
                if (selectedShop == shop) {
                    icon(selectedMarkerBitmap)
                } else {
                    icon(defaultMarkerBitmap)
                }
                title(shop.name)
            }
        }
    }

    override fun onClusterItemRendered(clusterItem: Shop?, marker: Marker?) {
        clusterItem?.let { shop ->
            if (selectedShop == shop) {
                marker?.showInfoWindow()
            } else {
                marker?.hideInfoWindow()
            }
        }

    }

    private fun createBitmapDescriptorFromVector(@DrawableRes vectorResourceId: Int, @ColorInt tintColor: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(context.resources, vectorResourceId, null)
            ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        DrawableCompat.setTint(vectorDrawable, tintColor)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}
