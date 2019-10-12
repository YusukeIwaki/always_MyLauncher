package io.github.yusukeiwaki.better_always_drink.shop_list

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class ShopListViewModel : ViewModel() {

    private val shopList = MutableLiveData<List<LatLng>>(emptyList())

    private val focusedShop = MutableLiveData<LatLng?>()

    private val mapAvailable = MutableLiveData<Boolean>(false)

    init {
        // 暫定
        onShopListLoaded(arrayListOf(LatLng(35.0, 135.0), LatLng(35.1, 135.1), LatLng(34.9, 135.2)))
        onFocusedShopChanged(LatLng(35.0, 135.0))
    }

    val shopListWithAvailability = MediatorLiveData<List<LatLng>>().apply {
        addSource(shopList) { shopListValue ->
            if (mapAvailable.value!! && shopListValue.isNotEmpty()) { value = shopListValue }
            else { value = emptyList() }
        }
        addSource(mapAvailable) { mapAvailableValue ->
            if (mapAvailableValue!! && shopList.value!!.isNotEmpty()) { value = shopList.value }
            else { value = emptyList() }
        }
    }

    val focusedShopWithAvailability = MediatorLiveData<LatLng?>().apply {
        addSource(focusedShop) { focusedShopValue ->
            if (mapAvailable.value!!) { value = focusedShopValue }
            else { value = null }
        }
        addSource(mapAvailable) { mapAvailableValue ->
            if (mapAvailableValue!!) { value = focusedShop.value }
            else { value = null }
        }
    }

    fun onMapInitialize() {
        mapAvailable.value = false
    }

    // GoogleMapの準備ができたときに呼ばれる
    fun onMapReady() {
        mapAvailable.value = true
    }

    // ショップデータ一覧が取得できたときに呼ばれる
    fun onShopListLoaded(newShopList: List<LatLng>) {
        shopList.value = newShopList
    }

    fun onFocusedShopChanged(newShop: LatLng?) {
        focusedShop.value = newShop
    }
}
