package io.github.yusukeiwaki.better_always_drink.shop_list

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.github.yusukeiwaki.better_always_drink.extension.cameraPositionAsFlow
import io.github.yusukeiwaki.better_always_drink.extension.observeOnce
import io.github.yusukeiwaki.better_always_drink.model.Shop
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import androidx.core.graphics.drawable.DrawableCompat
import android.graphics.Bitmap
import androidx.core.content.res.ResourcesCompat
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor
import android.graphics.Canvas
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import io.github.yusukeiwaki.better_always_drink.R


class ShopListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapFragmentAsView: View
    private lateinit var viewPager: ViewPager2
    private lateinit var googleMap: GoogleMap
    private val viewModel: ShopListViewModel by viewModels()

    private val markers: ArrayList<Marker> = arrayListOf()
    private val alwaysShopUuid: String? by AlwaysPreference(this)

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

        viewModel.shopList.observe(this) { newList ->
            var handled = false
            if (viewPagerAdapter.itemCount == 0) { // 初回に限り
                alwaysShopUuid?.let { uuid -> // お気に入りのお店があれば
                    val newPage = newList.indexOfFirst { shop -> shop.uuid == uuid }
                    if (newPage >= 0) {
                        // submit後にそのページにフォーカスを合わせる
                        viewPagerAdapter.submitList(newList) {
                            viewPager.setCurrentItem(newPage, false)
                        }
                        handled = true
                    }
                }
            }

            if (!handled) viewPagerAdapter.submitList(newList)
        }
        viewModel.focusedShop.observe(this) { focusedShop ->
            focusedShop?.let {
                viewPager.currentItem = viewModel.shopList.value!!.indexOfFirst { shop -> shop.uuid == focusedShop.uuid }
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

        // 初期位置を決めないと、アフリカの海が表示されるので、適当に初期位置を設定しておく。
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(33.2343214,131.6082574),15.0f))

        viewModel.focusedShop.observe(this) { focusedShop ->
            val defaultMarker = createBitmapDescriptorFromVector(R.drawable.ic_place_default_24dp, getColor(R.color.markerDefault))
            markers.forEach { marker ->
                (marker.tag as? Shop)?.let { shop ->
                    if (shop.uuid == focusedShop?.uuid) {
                        marker.setIcon(createBitmapDescriptorFromVector(R.drawable.ic_local_cafe_24dp, getColor(R.color.markerSelected)))
                        marker.showInfoWindow()
                    } else {
                        marker.setIcon(defaultMarker)
                        if (marker.isInfoWindowShown) {
                            marker.hideInfoWindow()
                        }
                    }
                }
            }

            focusedShop?.let {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lng)))
                onShopFocused()
            } ?: run {
                onShopUnfocused()
            }
        }
        viewModel.shopList.observe(this) { shopList ->
            markers.clear()
            val defaultMarker = createBitmapDescriptorFromVector(R.drawable.ic_place_default_24dp, getColor(R.color.markerDefault))
            shopList.forEach { shop ->
                val title =
                    if (shop.roughLocationDescription.isNullOrBlank())
                        shop.name
                    else
                        "${shop.roughLocationDescription} - ${shop.name}"

                val markerOptions = MarkerOptions()
                    .icon(defaultMarker)
                    .position(LatLng(shop.lat, shop.lng))
                    .title(title)
                val marker = googleMap.addMarker(markerOptions)
                marker.tag = shop
                markers.add(marker)
            }
        }
        viewModel.defaultCameraUpdate.observeOnce(this) { defaultCameraUpdate ->
            if (!viewModel.hasFocusedShop) {
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
            .withEndAction {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
            .start()
        viewPager.animate()
            .translationY(0.0f)
            .start()
    }

    private fun onShopUnfocused() {
        val height = viewPager.height
        mapFragmentAsView.animate()
            .translationY(0.0f)
            .withStartAction {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }
            .start()
        viewPager.animate()
            .translationY(height.toFloat())
            .start()
    }

    fun createBitmapDescriptorFromVector(@DrawableRes vectorResourceId: Int, @ColorInt tintColor: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, vectorResourceId, null)
            ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        DrawableCompat.setTint(vectorDrawable, tintColor)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onBackPressed() {
        if (viewModel.hasFocusedShop) {
            viewModel.onFocusedShopChanged(null)
        } else {
            super.onBackPressed()
        }
    }
}
