package io.github.yusukeiwaki.better_always_drink.extension

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

fun GoogleMap.cameraPositionAsFlow(): Flow<CameraPosition> = channelFlow {
    channel.offer(cameraPosition)
    setOnCameraMoveListener {
        channel.offer(cameraPosition)
    }
    awaitClose {
        setOnCameraMoveListener(null)
    }
}
