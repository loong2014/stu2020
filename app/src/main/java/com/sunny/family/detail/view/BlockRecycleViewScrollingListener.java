package com.sunny.family.detail.view;


import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.detail.view.common.BlockRecyclerView;
import com.sunny.lib.image.FrescoUtils;
import com.sunny.lib.report.ICtrTrigger;
import com.sunny.lib.utils.SunLog;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ZhangBoshi
 */

public class BlockRecycleViewScrollingListener extends RecyclerView.OnScrollListener {
    private static final String TAG = BlockRecycleViewScrollingListener.class.getSimpleName();
    private RecyclerView.OnScrollListener mCustomListener = null;
    private IBlockSmoothScrollingListener mIBlockSmoothScrollingListener = null;

    public BlockRecycleViewScrollingListener(IBlockSmoothScrollingListener listener) {
        mIBlockSmoothScrollingListener = listener;
    }

    public void setCustomListener(RecyclerView.OnScrollListener customListener) {
        mCustomListener = customListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mCustomListener != null) {
            mCustomListener.onScrollStateChanged(recyclerView, newState);
        } else {
//            Logger.e(TAG, "mCustomListener is NULL");
        }
        if (mIBlockSmoothScrollingListener != null) {
            mIBlockSmoothScrollingListener.postScrolling(newState);
        } else {
            SunLog.INSTANCE.e(TAG, "IBlockSmoothScrollingListener is NULL");
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mCustomListener != null) {
            mCustomListener.onScrolled(recyclerView, dx, dy);
        } else {
            SunLog.INSTANCE.e(TAG, "mCustomListener is NULL");
        }
    }

    public interface IBlockSmoothScrollingListener {
        void postScrolling(int scrollState);
    }

    /**
     * Created by ZhangBoshi
     * on 2019-12-9
     */
    public static class HomeRecyclerView extends BlockRecyclerView {

        /**
         * This is public so that the CREATOR can be access on cold launch.
         */
        public static class SavedState extends View.BaseSavedState {

            Parcelable mRect;

            /**
             * called by CREATOR
             */
            SavedState(Parcel in) {
                super(in);
                mRect = in.readParcelable(RecyclerView.LayoutManager.class.getClassLoader());
            }

            /**
             * Called by onSaveInstanceState
             */
            SavedState(Parcelable superState) {
                super(superState);
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeParcelable(mRect, 0);
            }

            private void copyFrom(SavedState other) {
                mRect = other.mRect;
            }

            public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
                @Override
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                @Override
                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
        }

        private static final String TAG = "HomeRecyclerView";
        private static final long POST_PENDING_TIME = 300;

        private boolean hasShownBackTip;

        private View mPreFocusedView; // previous focused view.
        private Rect mPreFocusedRect; // Rect of previous focused view.
        private Rect mRectToFindFocus; // Rect for search in direction of View.DOWN
        private ICtrTrigger mCtrTrigger;
        private KeyListener keyListener;

        public HomeRecyclerView(Context context) {
            this(context, null);
        }

        public HomeRecyclerView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public HomeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            setFocusable(false);
            setFocusableInTouchMode(false);
            setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                setChildrenDrawingOrderEnabled(true);

            setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                    if (dy < 0) {
                        showBackTip();
                    }
                }

                /**
                 * 滑动停止，进行数据上报，否则暂停上报
                 */
                @Override
                public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        FrescoUtils.resume();
                        if (mCtrTrigger != null)
                            mCtrTrigger.triggerReport();
                    } else {
                        FrescoUtils.pause();
                        if (mCtrTrigger != null)
                            mCtrTrigger.cancelReport();
                    }
                }
            });
            hasShownBackTip = false;
        }

        @Override
        public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
            // When ViewPager selects a page, it also requests focus for this page,
            // but the request would works on the first focusable view in the page.
            // so, if there was a focused view, do request focus on this view.
            // here, mPreFocusedView is the previous focused view.

            // boolean found = requestFocusInternal(mPreFocusedView);
            // if (!found && mRectToFindFocus != null) {
            // View view = FocusFinder.getInstance().findNextFocusFromRect(this, mRectToFindFocus,
            // View.FOCUS_DOWN);
            // found = requestFocusInternal(view);
            // mRectToFindFocus = null;
            // }

            // calls super if previous focus is not found.
            return super.requestFocus(direction, previouslyFocusedRect);
        }

        @Override
        public void onChildDetachedFromWindow(@NotNull View child) {
            super.onChildDetachedFromWindow(child);
            // if mPreFocusedView is a descendant of the detached view,
            // then MUST set it to null, or leak occurs, focus goes wrong.
            if (isDescendant(child, mPreFocusedView)) {
                mPreFocusedView = null;
                // view may detach from window when notified data changed.
                // when mPreFocusedView is detached, set mRectToFindFocus
                // for default focus request by the ViewGroup, RecyclerView.
                mRectToFindFocus = mPreFocusedRect;
            }
        }

        @Override
        public void requestChildFocus(View child, View focused) {
            // mPreFocusedView = focused;
            // if (mPreFocusedRect == null) {
            // mPreFocusedRect = new Rect();
            // }
            // mPreFocusedView.getFocusedRect(mPreFocusedRect);
            // offsetDescendantRectToMyCoords(mPreFocusedView, mPreFocusedRect);
            // // move Rect to (left, top - 1, right, bottom - 1),
            // // then FocusFinder searches previous focused view in the direction
            // // of View.DOWN.
            // mPreFocusedRect.top -= 1;
            // mPreFocusedRect.bottom -= 1;
            super.requestChildFocus(child, focused);
        }

        @Override
        protected Parcelable onSaveInstanceState() {
            SavedState state = new SavedState(super.onSaveInstanceState());
            state.mRect = mPreFocusedRect;
            return state;
        }

        @Override
        protected void onRestoreInstanceState(Parcelable state) {
            try {
                SavedState restoredState = (SavedState) state;
                super.onRestoreInstanceState(restoredState.getSuperState());
                if (restoredState.mRect != null) {
                    mRectToFindFocus = (Rect) restoredState.mRect;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
            try {
                super.dispatchRestoreInstanceState(container);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();

            boolean consumed = false;

            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                consumed = restoreToDefault();
            }
            return consumed || super.dispatchKeyEvent(keyEvent);
        }

        /**
         * 点击返回键，返回到顶部
         *
         * @return 是否消费返回键事件
         */
        public boolean restoreToDefault() {

            if (getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
                int itemPostion = layoutManager.findFirstVisibleItemPosition();
                SunLog.INSTANCE.e(TAG, "findFirstVisibleItemPosition=" + itemPostion);
                if (itemPostion < 1)
                    return false;
            }

            mPreFocusedView = null;
            mRectToFindFocus = null;
            scrollToPosition(0);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollToPosition(0);
                }
            }, POST_PENDING_TIME);

            if (keyListener != null) {
                clearFocus();
                keyListener.backPress();
            }
            return true;
        }

        /**
         * 第一次滑动到底部提示信息
         */
        private void showBackTip() {
            // if (!hasShownBackTip) {
            // Toast.makeText(ContextProvider.getApplicationContext(), R.string.detail_back_tip,
            // Toast.LENGTH_SHORT).show();
            // hasShownBackTip = true;
            // }
        }

        public void setCtrTrigger(@Nullable ICtrTrigger ctrTrigger) {
            mCtrTrigger = ctrTrigger;
        }

        public void setKeyListener(KeyListener keyListener) {
            this.keyListener = keyListener;
        }

        @Override
        public void clearFocus() {
            super.clearFocus();
            mPreFocusedView = null;
        }

        @Override
        public void destroy() {
            super.destroy();
        }

        public interface KeyListener {
            void backPress();
        }
    }
}
