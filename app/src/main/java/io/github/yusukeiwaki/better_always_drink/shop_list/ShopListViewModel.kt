package io.github.yusukeiwaki.better_always_drink.shop_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class ShopListViewModel : ViewModel() {

    private val _shopList = MutableLiveData<List<LatLng>>(emptyList())

    private val _focusedShop = MutableLiveData<LatLng?>()

    val shopList: LiveData<List<LatLng>> get() = _shopList

    val focusedShop: LiveData<LatLng?> get() = _focusedShop

    init {
        // 暫定
        onShopListLoaded(arrayListOf(LatLng(35.0, 135.0), LatLng(35.1, 135.1), LatLng(34.9, 135.2)))
        onFocusedShopChanged(LatLng(35.0, 135.0))
    }

    // ショップデータ一覧が取得できたときに呼ばれる
    private fun onShopListLoaded(newShopList: List<LatLng>) {
        _shopList.value = newShopList
    }

    private fun onFocusedShopChanged(newShop: LatLng?) {
        _focusedShop.value = newShop
    }
}
