package io.github.yusukeiwaki.better_always_drink.shop_list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.ref.WeakReference

class BottomSheetChildResizingBehavior<V : View>(context: Context, attrs: AttributeSet) : BottomSheetBehavior<V>(context, attrs) {

    private var parentRef: WeakReference<View> = WeakReference<View>(null)
    private var resizingTargetRef: WeakReference<View> = WeakReference<View>(null)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        (child as? ViewGroup)?.let { viewGroup ->
            if (viewGroup.childCount > 0) {
                parentRef = WeakReference(parent)
                resizingTargetRef = WeakReference(viewGroup.getChildAt(0))
            }
        }

        return super.layoutDependsOn(parent, child, dependency)
    }

    init {
        // dispatchOnSlideをオーバーライドはできないので、BottomSheetCallbackを仕掛けて、そこでやる。
        addBottomSheetCallback(object: BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                onChildUpdated(bottomSheet)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) { }
        })
    }

    // 画面回転したときには onSlideはコールバックされないので、ここで救う。
    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection).also {
            onChildUpdated(child)
        }
    }

    private fun onChildUpdated(bottomSheet: View) {
        parentRef.get()?.let { parent ->
            resizingTargetRef.get()?.let { resizingTarget ->
                val newHeight = parent.bottom - bottomSheet.top
                if (resizingTarget.layoutParams.height != newHeight) {
                    resizingTarget.layoutParams = resizingTarget.layoutParams.apply { height = newHeight }
                }
            }
        }
    }
}
