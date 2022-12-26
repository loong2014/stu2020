package com.sunny.lib.common.helper;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaxMotionHelper {
    /**
     * 模拟用户点击
     *
     * @param view 要触发操作的view
     * @param x    相对于要操作view的左上角x轴偏移量
     * @param y    相对于要操作view的左上角y轴偏移量
     */
    public static void analogUserClick(View view, float x, float y) {
        if (view == null) {
            return;
        }
//        LogUtil.e(TAG_POINT, "正在模拟点击操作：p->" + x + "," + y);
        long downTime = SystemClock.uptimeMillis();//模拟按下去的时间

        long eventTime = downTime;//事件发生时间

        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        view.onTouchEvent(downEvent);

        eventTime = eventTime + 90;//离开屏幕时间

        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(upEvent);

        //回收事件
        downEvent.recycle();
        upEvent.recycle();
    }

    /**
     * 模拟用户滑动操作
     *
     * @param view 要触发操作的view
     * @param type 模拟操作类型：均匀滑动、快速滑动
     * @param p1x  滑动的起始点x坐标
     * @param p1y  滑动的起始点y坐标
     * @param p2x  滑动的终点x坐标
     * @param p2y  滑动的终点y坐标
     */
    public static void analogUserScroll(View view, final int type, final float p1x, final float p1y, final float p2x, final float p2y) {
//        LogUtil.e(TAG_POINT, "正在模拟滑屏操作：p1->" + p1x + "," + p1y + ";p2->" + p2x + "," + p2y);
        if (view == null) {
            return;
        }
        long downTime = SystemClock.uptimeMillis();//模拟按下去的时间

        long eventTime = downTime;

        float pX = p1x;
        float pY = p1y;
        int speed = 0;//快速滑动
        float touchTime = 116;//模拟滑动时发生的触摸事件次数

        //平均每次事件要移动的距离
        float perX = (p2x - p1x) / touchTime;
        float perY = (p2y - p1y) / touchTime;

        boolean isReversal = perX < 0 || perY < 0;//判断是否反向：手指从下往上滑动，或者手指从右往左滑动
        boolean isHandY = Math.abs(perY) > Math.abs(perX);//判断是左右滑动还是上下滑动

//        if (type == USER_TOUCH_TYPE_1) {//加速滑动
        if (type == 1) {//加速滑动
            touchTime = 10;//如果是快速滑动，则发生的触摸事件比均匀滑动更少
            speed = isReversal ? -20 : 20;//反向移动则坐标每次递减
        }

        //模拟用户按下
        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, pX, pY, 0);
        view.onTouchEvent(downEvent);

        //模拟移动过程中的事件
        List<MotionEvent> moveEvents = new ArrayList<>();
        boolean isSkip = false;
        for (int i = 0; i < touchTime; i++) {

            pX += (perX + speed);
            pY += (perY + speed);
            if ((isReversal && pX < p2x) || (!isReversal && pX > p2x)) {
                pX = p2x;
                isSkip = !isHandY;
            }

            if ((isReversal && pY < p2y) || (!isReversal && pY > p2y)) {
                pY = p2y;
                isSkip = isHandY;
            }
            eventTime += 20.0f;//事件发生的时间要不断递增
            MotionEvent moveEvent = getMoveEvent(downTime, eventTime, pX, pY);
            moveEvents.add(moveEvent);
            view.onTouchEvent(moveEvent);
//            if (type == USER_TOUCH_TYPE_1) {//加速滑动
            if (type == 1) {//加速滑动
                speed += (isReversal ? -70 : 70);
            }
            if (isSkip) {
                break;
            }
        }

        //模拟手指离开屏幕
        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, pX, pY, 0);
        view.onTouchEvent(upEvent);

        //回收触摸事件
        downEvent.recycle();
        for (int i = 0; i < moveEvents.size(); i++) {
            moveEvents.get(i).recycle();
        }
        upEvent.recycle();
    }

    private static MotionEvent getMoveEvent(long downTime, long evntTime, float x, float y) {
        return MotionEvent.obtain(downTime, evntTime, MotionEvent.ACTION_MOVE, x, y, 0);
    }
}
