package io.github.yusukeiwaki.better_always_drink.shop_list

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import io.github.yusukeiwaki.better_always_drink.api.AlwaysApiClient
import io.github.yusukeiwaki.better_always_drink.model.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopListViewModel : ViewModel() {

    private val _shopList = MutableLiveData<List<Shop>>(emptyList())

    private val _focusedShop = MutableLiveData<Shop?>()

    private val _centerLatLng = MutableLiveData<LatLng>()
    private val _zoomLevel = MutableLiveData<Float>()

    val shopList: LiveData<List<Shop>> get() = _shopList

    val focusedShop: LiveData<Shop?> get() = _focusedShop.distinctUntilChanged()

    val centerLatLng: LiveData<LatLng> get() = _centerLatLng.distinctUntilChanged()
    val zoomLevel: LiveData<Float> get() = _zoomLevel.distinctUntilChanged()

    init {
        val area = "3a2eefa2" //福岡
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { AlwaysApiClient.listDrinkProviders(area) }
            }.onSuccess { response ->
                val shopList = response.menus.map { menu ->
                    Shop(
                        uuid = menu.pbProvider.uuid,
                        name = menu.pbProvider.name,
                        description = menu.pbProvider.description,
                        roughLocationDescription = menu.pbProvider.area,
                        businessHoursDescription = menu.pbProvider.businessHours,
                        lat = menu.pbProvider.location.lat,
                        lng = menu.pbProvider.location.lon,
                        pictureUrls = menu.pictures.map { picture -> picture.pictureUrl.largeUrl })
                }
                onShopListLoaded(shopList)

                response.places.find { it.uuid == area }?.let { fukuoka ->
                    onDefaultPositionLoaded(LatLng(fukuoka.lat, fukuoka.lng), fukuoka.zoom.toFloat())
                }
            }.onFailure { throwable ->
                Log.e("ShopListViewModel", "error", throwable)
            }
        }
    }

    // ショップデータ一覧が取得できたときに呼ばれる
    private fun onShopListLoaded(newShopList: List<Shop>) {
        _shopList.value = newShopList
    }

    private fun onDefaultPositionLoaded(defaultPosition: LatLng, defaultZoomLevel: Float) {
        _focusedShop.value ?: run {
            _centerLatLng.value = defaultPosition
            _zoomLevel.value = defaultZoomLevel
        }
    }

    fun onFocusedShopChanged(newShop: Shop?) {
        _focusedShop.value = newShop
    }

    fun onLatLngChanged(newLatLng: LatLng) {
        _centerLatLng.value = newLatLng
    }

    fun onZoomLevelChanged(newZoomLevel: Float) {
        _zoomLevel.value = newZoomLevel
    }
}
