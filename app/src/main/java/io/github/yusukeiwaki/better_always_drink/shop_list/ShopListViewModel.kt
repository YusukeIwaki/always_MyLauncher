package io.github.yusukeiwaki.better_always_drink.shop_list

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import io.github.yusukeiwaki.better_always_drink.api.AlwaysApiClient
import io.github.yusukeiwaki.better_always_drink.model.ServiceArea
import io.github.yusukeiwaki.better_always_drink.model.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopListViewModel : ViewModel() {

    private val _serviceAreaList = MutableLiveData<List<ServiceArea>>(emptyList())
    private val _shopList = MutableLiveData<List<Shop>>(emptyList())

    private val _selectedServiceArea = MutableLiveData<ServiceArea?>()
    private val _focusedShop = MutableLiveData<Shop?>()

    private val _lastLatLng = MutableLiveData<LatLng>()
    private val _lastZoomLevel = MutableLiveData<Float>()

    private val _defaultCameraUpdate = MutableLiveData<CameraUpdate>()

    val serviceAreaList: LiveData<List<ServiceArea>> get() = _serviceAreaList
    val shopList: LiveData<List<Shop>> get() = _shopList

    val selectedServiceArea: LiveData<ServiceArea?> get() = _selectedServiceArea.distinctUntilChanged()
    val selectedServiceAreaValue get() = _selectedServiceArea.value
    val focusedShop: LiveData<Shop?> get() = _focusedShop.distinctUntilChanged()
    val hasFocusedShop get() = _focusedShop.value != null

    val lastLatLng: LiveData<LatLng> get() = _lastLatLng.distinctUntilChanged()
    val lastZoomLevel: LiveData<Float> get() = _lastZoomLevel.distinctUntilChanged()
    val lastZoomLevelValue: Float? get() = _lastZoomLevel.value

    val defaultCameraUpdate: LiveData<CameraUpdate> get() = _defaultCameraUpdate.distinctUntilChanged()

    init {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { AlwaysApiClient.listDrinkProviders() }
            }.onSuccess { response ->
                val serviceAreaList = response.places.map { place ->
                    ServiceArea(
                        uuid = place.uuid,
                        name = place.name,
                        lat = place.lat,
                        lng = place.lng,
                        zoom = place.zoom.toFloat()
                    )
                }.toMutableList()
                serviceAreaList.firstOrNull { area -> area.zoom < ShopListClusterRenderer.ZOOM_THRESHOLD }?.let { wideArea ->
                    onWideServiceAreaLoaded(wideArea)
                    serviceAreaList.remove(wideArea)
                }
                onServiceAreaListLoaded(serviceAreaList)

                val shopList = response.menus.map { menu ->
                    Shop(
                        uuid = menu.pbProvider.uuid,
                        name = menu.pbProvider.name,
                        description = menu.pbProvider.description,
                        roughLocationDescription = menu.pbProvider.area,
                        businessHoursDescription = menu.pbProvider.businessHours,
                        lat = menu.pbProvider.location.lat,
                        lng = menu.pbProvider.location.lon,
                        thumbnailUrl = menu.pictures.firstOrNull()?.pictureUrl?.largeUrl,
                        pictureUrls = menu.pbProvider.pictures.map { picture -> picture.pictureUrl.largeUrl })
                }
                onShopListLoaded(shopList)
            }.onFailure { throwable ->
                Log.e("ShopListViewModel", "error", throwable)
            }
        }
    }

    // 「全国」のエリア情報が取得できたときに呼ばれる。
    private fun onWideServiceAreaLoaded(wideServiceArea: ServiceArea) {
        _defaultCameraUpdate.value = CameraUpdateFactory.newLatLngZoom(LatLng(wideServiceArea.lat, wideServiceArea.lng), wideServiceArea.zoom)
    }

    // エリア情報一覧が取得できたときに呼ばれる。ただし「全国」は含めていない。
    private fun onServiceAreaListLoaded(serviceAreaList: List<ServiceArea>) {
        _serviceAreaList.value = serviceAreaList
    }

    // ショップデータ一覧が取得できたときに呼ばれる
    private fun onShopListLoaded(newShopList: List<Shop>) {
        _shopList.value = newShopList
    }

    fun onServiceAreaSelected(newServiceArea: ServiceArea) {
        _selectedServiceArea.value = newServiceArea
    }

    fun onFocusedShopChanged(newShop: Shop?) {
        // ・京都を選択した状態（=ViewPagerには京都のお店がズラッと並んでいる状態）で、
        // 　福岡を拡大して福岡のお店を選択したときには、ViewPagerのリストは福岡になってくれないと困る。
        // ・地域を選択していない状態では、
        // 　お店を選択した時点では地域を勝手に選択されて（=ViewPagerで表示する地域が絞られて）ほしくない
        // という要件を満たしたい。なので
        _selectedServiceArea.value?.let { area -> // 地域が選択済みの場合に限り
            if (newShop != null) {
                // 選択したお店のエリアが
                val newArea = newShop.nearestServiceAreaIn(_serviceAreaList.value!!)
                if (area != newArea) { // 選択中のものと違っていたら、エリアを変更する。
                    _selectedServiceArea.value = area
                }
            }
        }
        _focusedShop.value = newShop
    }

    fun onLatLngChanged(newLatLng: LatLng) {
        _lastLatLng.value = newLatLng
    }

    fun onZoomLevelChanged(newZoomLevel: Float) {
        _lastZoomLevel.value = newZoomLevel
    }
}
