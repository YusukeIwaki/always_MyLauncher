package io.github.yusukeiwaki.better_always_drink.shop_list

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
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.extension.observeOnce
import io.github.yusukeiwaki.better_always_drink.model.Shop


class ShopListActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var viewPager: ViewPager2
    private lateinit var googleMap: GoogleMap
    private lateinit var shopListClusterRenderer: ShopListClusterRenderer
    private lateinit var bottomSheet: BottomSheetBehavior<View>
    private val viewModel: ShopListViewModel by viewModels()

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
                onShopFocused()
            } ?: run {
                onShopUnfocused()
            }
        }
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                viewModel.onFocusedShopChanged(viewModel.shopList.value!![position])
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
        shopListClusterRenderer = ShopListClusterRenderer(this, googleMap, shopListClusterManager, viewModel)
        shopListClusterManager.renderer = shopListClusterRenderer
        googleMap.setOnCameraIdleListener(shopListClusterManager)
        googleMap.setOnMarkerClickListener(shopListClusterManager)
        googleMap.setOnInfoWindowClickListener(shopListClusterManager)

        // 初期位置を決めないと、アフリカの海が表示されるので、適当に初期位置を設定しておく。
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(33.2343214,131.6082574),15.0f))


        viewModel.focusedShop.observe(this) { focusedShop ->
            shopListClusterRenderer.updateSelectedShop(focusedShop)

            focusedShop?.let {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lng)))
            }
        }
        viewModel.shopList.observe(this) { shopList ->
            shopListClusterManager.apply {
                clearItems()
                addItems(shopList)
                cluster()
            }
        }
        viewModel.defaultCameraUpdate.observeOnce(this) { defaultCameraUpdate ->
            if (!viewModel.hasFocusedShop) {
                googleMap.moveCamera(defaultCameraUpdate)
            }
        }


        shopListClusterManager.setOnClusterItemClickListener { shop ->
            viewModel.onFocusedShopChanged(shop)
            false
        }
        googleMap.setOnMapClickListener {
            viewModel.onFocusedShopChanged(null)
        }
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
