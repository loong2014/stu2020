package com.sunny.module.view.xxx.coordinator.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.sunny.module.view.R

/**
 * https://blog.csdn.net/victor_fang/article/details/84957070
 *
 *
 */
class FFRecyclerViewBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<RecyclerView>(context, attrs) {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: RecyclerView,
        dependency: View
    ): Boolean {
        return dependency.id == R.id.video_layout_head
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: RecyclerView,
        dependency: View
    ): Boolean {
        // 如果我们所依赖的View有变化，也是通过offsetTopAndBottom移动我们的RecyclerView
        ViewCompat.offsetTopAndBottom(child, (dependency.bottom - child.top));
        return false
    }
}