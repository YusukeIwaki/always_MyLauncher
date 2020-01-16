package io.github.yusukeiwaki.always_launcher.shop_list

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
        val bottomSheet = BottomSheetBehavior.from(dependency)

        val bottomSheetOffset = parent.bottom - dependency.top
        if (bottomSheetOffset <= bottomSheet.peekHeight) {
            child.translationY = -bottomSheetOffset.toFloat() / 2
        }

        if (bottomSheet.peekHeight > 0 && bottomSheet.peekHeight < parent.bottom) {
            val offset = 1.0f * dependency.top / (parent.bottom - bottomSheet.peekHeight)

            child.alpha =
                if (offset < 0) 0.0f
                else if (offset <= 1) offset
                else 1.0f
        }
        return true
    }
}
