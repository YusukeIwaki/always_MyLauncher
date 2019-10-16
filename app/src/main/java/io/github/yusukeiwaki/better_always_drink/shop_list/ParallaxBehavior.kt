package io.github.yusukeiwaki.better_always_drink.shop_list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

import com.google.android.material.bottomsheet.BottomSheetBehavior

class ParallaxBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        try {
            BottomSheetBehavior.from(dependency)
        } catch (exception: IllegalArgumentException) {
            return false
        }

        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        val bottomSheetOffset = parent.bottom - dependency.top
        child.translationY = - bottomSheetOffset.toFloat() / 2
        return true
    }
}
