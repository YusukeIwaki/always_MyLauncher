package io.github.yusukeiwaki.always_launcher.shop_list

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import io.github.yusukeiwaki.always_launcher.R
import io.github.yusukeiwaki.always_launcher.model.Shop


class ShopListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var viewPager: ViewPager2
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomSheet: BottomSheetBehavior<View>
    private val viewModel: ShopListViewModel by viewModels()
    private val mapViewModel: MapFragmentViewModel by viewModels()
    private lateinit var viewPagerAdapter: ShopListAdapter

    private val alwaysShopUuid: String? by AlwaysPreference(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE.or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewPager = findViewById(R.id.view_pager)
        bottomSheet = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_container))
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        viewPagerAdapter = ShopListAdapter()

        // 左右のカードを少しだけ見えるようにする
        viewPager.offscreenPageLimit = 2
        val shopCardMargin = viewPager.resources.getDimensionPixelSize(R.dimen.shop_card_margin)
        viewPager.setPageTransformer { page, position ->
            val offset = position * shopCardMargin * 2
            page.translationX = -offset
        }

        viewModel.selectedServiceArea.observe(this) { updateShopListOfViewPager() }
        viewModel.shopList.observe(this) { updateShopListOfViewPager() }
        viewModel.focusedShop.observe(this) { focusedShop ->
            focusedShop?.let {
                val position = viewPagerAdapter.currentList.indexOfFirst { shop -> shop.uuid == focusedShop.uuid }
                if (position >= 0) viewPager.currentItem = position
                onShopFocused()
            } ?: run {
                onShopUnfocused()
            }
        }
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                viewModel.onFocusedShopChanged(viewPagerAdapter.currentList[position])
            }
        })
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(view: View, slideOffset: Float) { }

            override fun onStateChanged(view: View, newState: Int) {
                updateBottomSheetState(newState)
            }
        })
        updateBottomSheetState(bottomSheet.state)
        viewPager.adapter = viewPagerAdapter
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val shopListClusterManager = ClusterManager<Shop>(this, googleMap)
        val shopListClusterRenderer = ShopListClusterRenderer(this, googleMap, shopListClusterManager, mapViewModel)
        shopListClusterManager.renderer = shopListClusterRenderer
        googleMap.setOnCameraIdleListener(shopListClusterManager)
        googleMap.setOnMarkerClickListener(shopListClusterManager)
        googleMap.setOnInfoWindowClickListener(shopListClusterManager)

        // 初期位置を決めないと、アフリカの海が表示されるので、適当に初期位置を設定しておく。
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
            mapViewModel.lastLatLngValue ?: LatLng(34.6870728, 135.0490244),
            mapViewModel.lastZoomLevelValue ?: 5.0f))

        viewModel.focusedShop.observe(this) { focusedShop ->
            shopListClusterRenderer.updateSelectedShop(focusedShop)

            focusedShop?.let { shop ->
                if (googleMap.cameraPosition.zoom < ShopListClusterRenderer.ZOOM_THRESHOLD) {
                    val area = shop.nearestServiceAreaIn(viewModel.serviceAreaList.value!!)
                    if (area != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(shop.lat, shop.lng), area.zoom))
                    } else {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(shop.lat, shop.lng)))
                    }
                } else {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(shop.lat, shop.lng)))
                }
            }
        }
        viewModel.serviceAreaList.observe(this) { serviceAreaList ->
            shopListClusterRenderer.updateServiceAreaList(serviceAreaList)
        }
        viewModel.shopList.observe(this) { shopList ->
            shopListClusterManager.apply {
                clearItems()
                addItems(shopList)
                cluster()
            }
        }

        shopListClusterManager.setOnClusterClickListener { cluster ->
            cluster?.let{ shopListClusterRenderer.nearestServiceAreaFor(it) }?.let { serviceArea ->
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(serviceArea.lat, serviceArea.lng), serviceArea.zoom))
                viewModel.onServiceAreaSelected(serviceArea)
                true
            } ?: false
        }
        shopListClusterManager.setOnClusterItemClickListener { shop ->
            viewModel.onFocusedShopChanged(shop)
            false
        }
        googleMap.setOnMapClickListener {
            viewModel.onFocusedShopChanged(null)
        }
    }

    private fun updateShopListOfViewPager() {
        val selectedServiceArea = viewModel.selectedServiceAreaValue
        val newList = viewModel.shopList.value!!
        val serviceAreas = viewModel.serviceAreaList.value!!

        val newListFiltered = selectedServiceArea?.let {
            newList.filter { shop -> shop.nearestServiceAreaIn(serviceAreas) == it }
        } ?: newList

        alwaysShopUuid?.let { uuid -> // お気に入りのお店があれば
            // 表示しようとしているリストの中にそれがある場合には
            newListFiltered.firstOrNull { it.uuid == uuid }?.let { alwaysShop ->
                // 現在のエリアとお気に入りエリアが異なる場合には、お気に入りエリアをを強制的に設定し、
                // あらためてupdateShopListOfViewPagerを呼び出してもらう
                alwaysShop.nearestServiceAreaIn(serviceAreas)?.let { newServiceArea ->
                    if (selectedServiceArea != newServiceArea) {
                        viewModel.onServiceAreaSelected(newServiceArea)
                        return
                    }
                }
                // submit後にそのページにフォーカスを合わせる
                viewPagerAdapter.submitList(newListFiltered) {
                    viewModel.onFocusedShopChanged(alwaysShop)
                }
                return
            }
        }

        viewPagerAdapter.submitList(newListFiltered)
    }

    private fun updateBottomSheetState(newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            viewModel.onFocusedShopChanged(null) // ボトムシートを手で引っ込めた場合には、キャンセル扱いにする
        }
    }

    private fun onShopFocused() {
        if (bottomSheet.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun onShopUnfocused() {
        if (bottomSheet.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onBackPressed() {
        if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        } else if (viewModel.hasFocusedShop) {
            viewModel.onFocusedShopChanged(null)
        } else {
            super.onBackPressed()
        }
    }
}
