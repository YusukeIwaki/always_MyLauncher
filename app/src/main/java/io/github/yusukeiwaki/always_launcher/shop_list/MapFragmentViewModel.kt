package io.github.yusukeiwaki.always_launcher.shop_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.google.android.gms.maps.model.LatLng

class MapFragmentViewModel: ViewModel() {
    private val _lastLatLng = MutableLiveData<LatLng>()
    private val _lastZoomLevel = MutableLiveData<Float>()

    val lastLatLng: LiveData<LatLng> get() = _lastLatLng.distinctUntilChanged()
    val lastLatLngValue: LatLng? get() = _lastLatLng.value
    val lastZoomLevel: LiveData<Float> get() = _lastZoomLevel.distinctUntilChanged()
    val lastZoomLevelValue: Float? get() = _lastZoomLevel.value

    fun onLatLngChanged(newLatLng: LatLng) {
        _lastLatLng.value = newLatLng
    }

    fun onZoomLevelChanged(newZoomLevel: Float) {
        _lastZoomLevel.value = newZoomLevel
    }
}
