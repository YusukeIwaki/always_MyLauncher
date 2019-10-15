package io.github.yusukeiwaki.better_always_drink.shop_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.extension.cameraPositionAsFlow
import io.github.yusukeiwaki.better_always_drink.extension.observeOnce
import io.github.yusukeiwaki.better_always_drink.model.Shop
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class ShopListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapFragmentAsView: View
    private lateinit var viewPager: ViewPager2
    private lateinit var googleMap: GoogleMap
    private val viewModel: ShopListViewModel by viewModels()

    private val markers: ArrayList<Marker> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragmentAsView = findViewById(R.id.map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewPager = findViewById(R.id.view_pager)
        val viewPagerAdapter = ShopListAdapter()

        // 左右のカードを少しだけ見えるようにする
        viewPager.offscreenPageLimit = 2
        val shopCardMargin = viewPager.resources.getDimensionPixelSize(R.dimen.shop_card_margin)
        viewPager.setPageTransformer { page, position ->
            val offset = position * shopCardMargin * 2
            page.translationX = -offset
        }

        viewModel.shopList.observe(this) {
            viewPagerAdapter.submitList(it)
        }
        viewModel.focusedShop.observe(this) { focusedShop ->
            focusedShop?.let {
                viewPager.currentItem = viewModel.shopList.value!!.indexOfFirst { shop -> shop.uuid == focusedShop.uuid }
                markers.forEach { marker ->
                    (marker.tag as? Shop)?.let { shop ->
                        if (shop.uuid == focusedShop.uuid) {
                            marker.showInfoWindow()
                        } else {
                            if (marker.isInfoWindowShown) {
                                marker.hideInfoWindow()
                            }
                        }
                    }
                }
            }
        }
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                viewModel.onFocusedShopChanged(viewModel.shopList.value!![position])
            }
        })
        viewPager.adapter = viewPagerAdapter
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        viewModel.focusedShop.observe(this) { focusedShop ->
            focusedShop?.let {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lng)))
                onShopFocused()
            } ?: run {
                onShopUnfocused()
            }
        }
        viewModel.shopList.observe(this) { shopList ->
            markers.clear()
            shopList.forEach { shop ->
                val markerOptions = MarkerOptions()
                    .position(LatLng(shop.lat, shop.lng))
                    .title(shop.name)
                val marker = googleMap.addMarker(markerOptions)
                marker.tag = shop
                markers.add(marker)
            }
        }
        viewModel.defaultCameraUpdate.observeOnce(this) { defaultCameraUpdate ->
            if (viewModel.focusedShop.value == null) {
                googleMap.moveCamera(defaultCameraUpdate)
            }
        }

        googleMap.setOnMarkerClickListener { marker ->
            (marker.tag as? Shop)?.let { shop ->
                viewModel.onFocusedShopChanged(shop)
            }
            false
        }
        googleMap.setOnMapClickListener {
            if (!markers.any { it.isInfoWindowShown }) {
                viewModel.onFocusedShopChanged(null)
            }
        }
        viewModel.viewModelScope.launch {
            googleMap.cameraPositionAsFlow().debounce(300).collect { cameraPosition ->
                viewModel.onLatLngChanged(cameraPosition.target)
                viewModel.onZoomLevelChanged(cameraPosition.zoom)
            }
        }
    }

    private fun onShopFocused() {
        val height = viewPager.height
        mapFragmentAsView.animate()
            .translationY(-height / 2.0f)
            .start()
        viewPager.animate()
            .translationY(0.0f)
            .start()
    }

    private fun onShopUnfocused() {
        val height = viewPager.height
        mapFragmentAsView.animate()
            .translationY(0.0f)
            .start()
        viewPager.animate()
            .translationY(height * 0.9f)
            .start()
    }
}
