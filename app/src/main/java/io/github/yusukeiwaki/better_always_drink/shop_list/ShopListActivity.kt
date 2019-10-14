package io.github.yusukeiwaki.better_always_drink.shop_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.extension.cameraPositionAsFlow
import io.github.yusukeiwaki.better_always_drink.model.Shop
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class ShopListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private val viewModel: ShopListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        viewModel.centerLatLng.observe(this) { focusedShop ->
            focusedShop?.let {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            }
        }
        viewModel.zoomLevel.observe(this) {
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(it))
        }
        viewModel.shopList.observe(this) { shopList ->
            shopList.forEachIndexed { idx, shop ->
                val markerOptions = MarkerOptions()
                    .position(LatLng(shop.lat, shop.lng))
                    .title(shop.name)
                val marker = googleMap.addMarker(markerOptions)
                marker.tag = shop
            }
        }

        googleMap.setOnMarkerClickListener { marker ->
            (marker.tag as? Shop)?.let { shop ->
                viewModel.onFocusedShopChanged(shop)
            }
            false
        }
        viewModel.viewModelScope.launch {
            googleMap.cameraPositionAsFlow().debounce(300).collect { cameraPosition ->
                viewModel.onLatLngChanged(cameraPosition.target)
                viewModel.onZoomLevelChanged(cameraPosition.zoom)
            }
        }
    }
}
