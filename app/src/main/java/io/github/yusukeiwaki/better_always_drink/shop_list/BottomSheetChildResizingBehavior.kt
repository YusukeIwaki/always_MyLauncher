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
    private var resizingChildRef: WeakReference<View> = WeakReference<View>(null)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return super.layoutDependsOn(parent, child, dependency).also {
            (child as? ViewGroup)?.let { viewGroup ->
                if (viewGroup.childCount > 0) {
                    parentRef = WeakReference(parent)
                    resizingChildRef = WeakReference(viewGroup.getChildAt(0))
                }
            }
        }
    }

    init {
        // dispatchOnSlideをオーバーライドはできないので、BottomSheetCallbackを仕掛けて、そこでやる。
        addBottomSheetCallback(object: BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                parentRef.get()?.let { parent ->
                    resizingChildRef.get()?.let { child ->
                        val newHeight = parent.bottom - bottomSheet.top
                        if (child.layoutParams.height != newHeight) {
                            child.layoutParams = child.layoutParams.apply { height = newHeight }
                        }
                    }
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) { }
        })
    }
}
