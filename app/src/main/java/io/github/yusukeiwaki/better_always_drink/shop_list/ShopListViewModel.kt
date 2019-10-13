package io.github.yusukeiwaki.better_always_drink.shop_list

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import io.github.yusukeiwaki.better_always_drink.api.AlwaysApiClient
import io.github.yusukeiwaki.better_always_drink.api.ListDrinkProvidersResponse
import io.github.yusukeiwaki.better_always_drink.api.MonoV2ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ShopListViewModel : ViewModel() {

    private val _shopList = MutableLiveData<List<LatLng>>(emptyList())

    private val _focusedShop = MutableLiveData<LatLng?>()

    private val _zoomLevel = MutableLiveData<Double>()

    val shopList: LiveData<List<LatLng>> get() = _shopList

    val focusedShop: LiveData<LatLng?> get() = _focusedShop.distinctUntilChanged()

    val zoomLevel: LiveData<Double> get() = _zoomLevel.distinctUntilChanged()

    init {
        val area = "3a2eefa2" //福岡
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { AlwaysApiClient.listDrinkProviders(area) }
            }.onSuccess { response ->
                onShopListLoaded(response.menus.map { menu -> LatLng(menu.pbProvider.location.lat, menu.pbProvider.location.lon) })
                response.places.find { it.uuid == area }?.let { fukuoka ->
                    onDefaultPositionLoaded(LatLng(fukuoka.lat, fukuoka.lng), fukuoka.zoom.toDouble())
                }
            }
        }
    }

    // ショップデータ一覧が取得できたときに呼ばれる
    private fun onShopListLoaded(newShopList: List<LatLng>) {
        _shopList.value = newShopList
    }

    private fun onDefaultPositionLoaded(defaultPosition: LatLng, defaultZoomLevel: Double) {
        _focusedShop.value ?: run {
            _focusedShop.value = defaultPosition
            _zoomLevel.value = defaultZoomLevel
        }
    }

    private fun onFocusedShopChanged(newShop: LatLng?) {
        _focusedShop.value = newShop
    }

    private fun onZoomLevelChanged(newZoomLevel: Double) {
        _zoomLevel.value = newZoomLevel
    }
}
