package io.github.yusukeiwaki.better_always_drink.shop_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import io.github.yusukeiwaki.better_always_drink.R

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
        viewModel.focusedShop.observe(this) { focusedShop ->
            focusedShop?.let {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0f))
            }
        }
        viewModel.shopList.observe(this) { shopList ->
            shopList.forEachIndexed { idx, shop ->
                googleMap.addMarker(MarkerOptions().position(shop).title("Marker ${idx}"))
            }
        }
    }
}
