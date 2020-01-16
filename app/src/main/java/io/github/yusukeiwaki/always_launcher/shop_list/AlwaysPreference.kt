package io.github.yusukeiwaki.always_launcher.shop_list

import android.content.Context
import kotlin.reflect.KProperty

class AlwaysPreference(private val context: Context) {
    private val sharedPreferences get() = context.getSharedPreferences("always", Context.MODE_PRIVATE)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return sharedPreferences.getString("shop_uuid", null)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        sharedPreferences.edit()
            .putString("shop_uuid", value)
            .apply()
    }

    // Delegated property を使うまでもないときはこっちを使う
    fun putValue(value: String?) {
        if (value == null) {
            sharedPreferences.edit().remove("shop_uuid").apply()
        } else {
            sharedPreferences.edit().putString("shop_uuid", value).apply()
        }
    }
}
