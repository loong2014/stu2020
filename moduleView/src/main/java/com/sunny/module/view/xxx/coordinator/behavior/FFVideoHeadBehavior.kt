package com.sunny.module.view.xxx.coordinator.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.SCROLL_AXIS_VERTICAL
import timber.log.Timber

/**
 * https://blog.csdn.net/victor_fang/article/details/84957070
 *
 *
 */
class FFVideoHeadBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {
    private var mOffsetTopAndBottom: Int = 0
    private var mLayoutTop: Int = 0

    fun showLog(log: String) {
        Timber.i("Behavior :$log")
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        parent.onLayoutChild(child, layoutDirection)
        // 获取到child初始的top值
        mLayoutTop = child.top
        return true
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 这里我们只关系垂直方向的滚动
        return (axes and SCROLL_AXIS_VERTICAL) !== 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (dy != 0) {
            // 如果本次滑动距离不为0，进行自己的滚动操作
            consumed[1] = scroll(child, dy);
        }
    }

    // 获取childView最大可滑动距离
    private fun getChildScrollRang(childView: View?): Int {
        return childView?.height ?: 0
    }

    // 滚动child
    private fun scroll(child: View, dy: Int): Int {
        var consumed = 0; // 记录我们消费的距离
        var offset = mOffsetTopAndBottom - dy; // 计算出本次需要滚动到的位置
        val minOffset = -getChildScrollRang(child);
        var maxOffset = 100;
        // 调整滚动距离，在0和最大可滑动距离的负数之间(因为是向上滑动，所以是负数哦)
        offset = when {
            offset < minOffset -> {
                minOffset
            }
            offset > maxOffset -> {
                maxOffset
            }
            else -> {
                offset
            }
        }
        // 通过offsetTopAndBottom()进行滚动
        ViewCompat.offsetTopAndBottom(child, offset - (child.top - mLayoutTop));
        // 计算消费的距离
        consumed = mOffsetTopAndBottom - offset;
        // 将本次滚动到的位置记录下来
        mOffsetTopAndBottom = offset;
        return consumed
    }
//
//    override fun layoutDependsOn(
//        parent: CoordinatorLayout,
//        child: TextView,
//        dependency: View
//    ): Boolean {
//        return dependency.id == R.id.video_recycler_view
//    }
//
//    private var deltaY: Float = 0F
//
//    override fun onDependentViewChanged(
//        parent: CoordinatorLayout,
//        child: TextView,
//        dependency: View
//    ): Boolean {
//        // 获取到RecyclerView的Y坐标
//        val dependencyY = dependency.y
//
//        if (deltaY == 0F) {
//            // 第一次先获取到初始状态下RecyclerView的Y坐标和绑定的View的高度差值作为后面计算的基值
//            deltaY = dependencyY - child.height
//        }
////        ​
//        // 根据RecyclerView移动，计算当前的差值
//        var dy = (dependencyY - child.height).takeIf { it > 0F } ?: 0F
//        // 求出当前需要移动的距离
//        val y = -(dy / deltaY) * child.height
//        val preTranslationY = child.translationY
//        if (y != preTranslationY) {
//            // 移动HelloWorld 并返回true
//            child.translationY = y
//            return true;
//        }
//        return false;
//    }
////
//    ————————————————
//    版权声明：本文为CSDN博主「VictorCatFish」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/victor_fang/article/details/84957070
//
//    /**
//     * 有嵌套滑动到来了，问下该Behavior是否接受嵌套滑动
//     *
//     * @param coordinatorLayout 当前的CoordinatorLayout
//     * @param child             该Behavior对应的View
//     * @param directTargetChild 我的理解是在CoordinateLayout下作为父View,而该View的子类是Target的那个View,
//     * 也就是Target的父View),因为我测试用ViewPager包裹了RecycleView后该参数返回Viewpager,
//     * 如果没有包裹参数返回的是RecycleView
//     * @param target            具体嵌套滑动的那个子类
//     * @param nestedScrollAxes  支持嵌套滚动轴。水平方向，垂直方向，或者不指定
//     * @param type              导致此滚动事件的输入类型
//     * @return 是否接受该嵌套滑动
//     */
//    override fun onStartNestedScroll(
//        coordinatorLayout: CoordinatorLayout,
//        child: View,
//        directTargetChild: View,
//        target: View,
//        nestedScrollAxes: Int,
//        @ViewCompat.NestedScrollType type: Int
//    ): Boolean {
//        i(TAG, "onStartNestedScroll===== :" + target.y)
//
//        // 处理垂直方向的滑动
//        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
//    }
//
//    override fun onStopNestedScroll(
//        coordinatorLayout: CoordinatorLayout,
//        child: View,
//        target: View,
//        type: Int
//    ) {
//        i(TAG, "onStopNestedScroll===== :" + target.y)
//    }
//
//    /**
//     * 在嵌套滑动的子View未滑动之前准备滑动的情况
//     *
//     * @param coordinatorLayout 此行为与关联的视图的父级CoordinatorLayout
//     * @param child             该Behavior对应的View
//     * @param target            具体嵌套滑动的那个子类
//     * @param dx                水平方向嵌套滑动的子View想要变化的距离
//     * @param dy                垂直方向嵌套滑动的子View想要变化的距离
//     * @param consumed          这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
//     * @param type              导致此滚动事件的输入类型
//     */
//    override fun onNestedPreScroll(
//        coordinatorLayout: CoordinatorLayout,
//        child: View,
//        target: View,
//        dx: Int,
//        dy: Int,
//        consumed: IntArray,
//        type: Int
//    ) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//        //dy大于0是向上滚动 小于0是向下滚动，判断的时候尽量不要判断是否大于等于或者小于等于0，否则可能会影响点击事件
//        //System.out.println(dy);
//        i(TAG, "onNestedPreScroll  dy :$dy")
//        if (dy > 20) {
//            tryShow(child)
//        } else if (dy < -20) {
//            tryHide(child)
//        }
//    }
//
//    //隐藏时的动画
//    private fun tryHide(view: View) {
//        if (isAnimate || view.visibility == View.INVISIBLE) {
//            return
//        }
//        val animator = view.animate().translationY(view.height.toFloat()).setInterpolator(
//            INTERPOLATOR
//        ).setDuration(500)
//        animator.setListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animator: Animator) {
//                isAnimate = true
//            }
//
//            override fun onAnimationEnd(animator: Animator) {
//                view.visibility = View.INVISIBLE
//                isAnimate = false
//            }
//
//            override fun onAnimationCancel(animator: Animator) {
//                tryShow(view)
//            }
//
//            override fun onAnimationRepeat(animator: Animator) {}
//        })
//        animator.start()
//    }
//
//    //显示时的动画
//    private fun tryShow(view: View) {
//        if (isAnimate || view.visibility == View.VISIBLE) {
//            return
//        }
//        val animator =
//            view.animate().translationY(0f).setInterpolator(INTERPOLATOR).setDuration(500)
//        animator.setListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animator: Animator) {
//                view.visibility = View.VISIBLE
//                isAnimate = true
//            }
//
//            override fun onAnimationEnd(animator: Animator) {
//                isAnimate = false
//            }
//
//            override fun onAnimationCancel(animator: Animator) {
//                tryHide(view)
//            }
//
//            override fun onAnimationRepeat(animator: Animator) {}
//        })
//        animator.start()
//    }
//
//    companion object {
//        private const val TAG = "BottomBarBehavior"
//        private val INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
//    }
}