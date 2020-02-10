package com.sunny.family.detail.view.common;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.R;
import com.sunny.family.detail.view.BlockRecycleViewScrollingListener;
import com.sunny.family.detail.view.FocusProcessLayout;
import com.sunny.family.detail.view.ScaledRecyclerView;
import com.sunny.lib.utils.AppConfigUtils;
import com.sunny.lib.utils.HandlerUtils;

/**
 * Created by ZhangBoshi
 * on 2019-12-9
 * 封装了Block框架的搜索焦点逻辑
 * 通过{@link BlockRecyclerView#focusSearchInternal(View, int)}搜索焦点.
 */
public class BlockRecyclerView extends ScaledRecyclerView {
    private static final int DEFAULT_FOCUS_SEARCH_SCROLL_DISTANCE = 50;
    private static final String SCROLL_FROM_RUNNABLE = "runnable";
    private static final String SCROLL_FROM_DISPATCH = "dispatchKeyEvent";
    private static final String TAG = "BlockRecyclerView";
    private static final int SCROLL_TIME = 100;
    private final int MIN_OPT_INTERVAL;
    private long lastOptTime = 0;
    private KeyEvent mPreKeyEvent;

    private int mCurKeyCode = -1;

    private boolean isFinishGetFocus;// 是否停止匀速下滑并获取到了坑位焦点
    private boolean isSmoothScrolling = false;
    @Nullable
    private IBlockFocusListener mBlockFocusListener;
    private boolean isAutoScroll;
    private final Runnable mAutoScrolling = new Runnable() {
        @Override
        public void run() {
            if (isAutoScroll) {
                startAutoScroll(mCurKeyCode, SCROLL_FROM_RUNNABLE);
            }
        }
    };
    /**
     * 当无法找到焦点时, 会尝试通过滚动再次搜索焦点
     */
    private int mFocusSearchScrollDistance = DEFAULT_FOCUS_SEARCH_SCROLL_DISTANCE;
    private final BlockRecycleViewScrollingListener mBlockRecycleViewScrollingListener = new BlockRecycleViewScrollingListener(
            new BlockRecycleViewScrollingListener.IBlockSmoothScrollingListener() {
                @Override
                public void postScrolling(int scrollState) {
                    if (scrollState == RecyclerView.SCROLL_STATE_IDLE && isAutoScroll
                            && mCurKeyCode != -1) {
                        HandlerUtils.INSTANCE.getUiHandler().postDelayed(mAutoScrolling, SCROLL_TIME);
                    }
                }
            });

    public BlockRecyclerView(Context context) {
        this(context, null);
    }

    public BlockRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (AppConfigUtils.INSTANCE.isLowCostDevice()) {
            MIN_OPT_INTERVAL = 200;
        } else {
            MIN_OPT_INTERVAL = 150;
        }
        setFocusable(false);
        setFocusableInTouchMode(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        super.setOnScrollListener(mBlockRecycleViewScrollingListener);

    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        mBlockRecycleViewScrollingListener.setCustomListener(listener);
    }

    public void setSmoothScrolling(boolean smoothScrolling) {
        Log.i(TAG, "setSmoothScrolling: " + smoothScrolling);
        isSmoothScrolling = smoothScrolling;
    }

    public void stopAutoScroll(String from) {
        Log.i(TAG, "stopAutoScroll:::" + from);
        if (isAutoScroll) {
            this.setFocusable(false);
            this.setFocusableInTouchMode(false);
            // CommonFrescoUtil.resume();
            setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
            HandlerUtils.INSTANCE.getUiHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findAvailableChildFocus(mCurKeyCode);
                }
            }, 200);
            // HandlerUtils.getUiThreadHandler().removeCallbacks(mAutoScrolling);
            stopScroll();
            mCurKeyCode = -1;
            isAutoScroll = false;
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) layout;
    }

    private FocusProcessLayout mFocusProcessLayout;

    /**
     * 滚动完毕需要在当前页面寻找可以获得焦点的poster
     */
    private void getCurrentChild(View viewChild) {

        if (mFocusProcessLayout != null) {
            return;
        }

        if (viewChild instanceof FocusProcessLayout && viewChild.getVisibility() == View.VISIBLE
                && isChildFullVisibleInParent(this, viewChild)) {
            mFocusProcessLayout = (FocusProcessLayout) viewChild;
            return;
        }

        if (viewChild instanceof ViewGroup) {
            ViewGroup viewParent = (ViewGroup) viewChild;
            int childCount = viewParent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View viewChild1 = viewParent.getChildAt(i);
                getCurrentChild(viewChild1);
            }
        }
    }

    /**
     * 滚动完毕需要在当前页面寻找可以获得焦点的poster
     */
    public void findAvailableChildFocus(int keyCode) {

        mFocusProcessLayout = null;
        getCurrentChild(this);

        if (mFocusProcessLayout != null) {
            Log.i(TAG, "GOT No CHILD request focus, but tempview: " + mFocusProcessLayout);
            mFocusProcessLayout.requestFocus();
        } else {
            Log.i(TAG, "GOT No CHILD request focus");
            this.requestFocus();
        }
        isFinishGetFocus = false;
    }

    public void startAutoScroll(int keyCode, String from) {
        if (!isAutoScroll) {
            isFinishGetFocus = true;
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
            this.requestFocus();
            longPressScroll(keyCode);
            isAutoScroll = true;
        } else if (from.equals(SCROLL_FROM_RUNNABLE)) {
            longPressScroll(keyCode);
        }
    }

    public final void setFocusSearchScrollDistance(int distance) {
        if (distance <= 0)
            throw new IllegalArgumentException("distance <= 0!");
        mFocusSearchScrollDistance = distance;
    }

    public void setBlockFocusListener(@Nullable IBlockFocusListener blockFocusListener) {
        mBlockFocusListener = blockFocusListener;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (mBlockFocusListener != null) {
            ViewHolder vh = getChildViewHolder(child);
            if (vh instanceof BlockHolder) {
                mBlockFocusListener.onBlockGotFocus(((BlockHolder) vh).getBlockInfo());
            }
        }
    }

    @Override
    public void onStartTemporaryDetach() {
        long start = System.currentTimeMillis();
        super.onStartTemporaryDetach();
        temporaryDetachFrescoView(this);
        long end = System.currentTimeMillis();
        Log.i(TAG, "fresco_debug onStartTemporaryDetach()-ms=" + (end - start));
    }

    @Override
    public void onFinishTemporaryDetach() {
        long start = System.currentTimeMillis();
        super.onFinishTemporaryDetach();
        finishTemporaryDetachFrescoView(this);
        long end = System.currentTimeMillis();
        Log.i(TAG, "fresco_debug onFinishTemporaryDetach()-ms=" + (end - start));
    }

    /**
     * 桌面匀速滑动需要在此处理。开启方法：
     * 1. 各个桌面的Fragment里所使用的RecycleView的LinearLayoutManager需要继承 {@link BlockLinearLayoutManager}
     * ，可以通过 {@link BlockLinearLayoutManager#setScrollingSpeed(float)} 方法来设置滚动速度。
     * 2. 各个桌面的RecycleView需要调用 {@link BlockRecyclerView#setSmoothScrolling(boolean)} 方法，
     * 传入True值来打开匀速滚动效果。
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        // 通过记录上一个按键事件是否与当前按键事件一致来判断是否是长按事件。情况右二：
        // 1. 焦点从Launcher的Tab栏长按"下"到桌面；
        // 2. 焦点在桌面内部长按事件。
        // 当按键抬起的时候，在 KeyEvent.ACTION_UP 发生的时候，将所有状态归位。
        if (isSmoothScrolling) {
            if (mPreKeyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    && mPreKeyEvent.getKeyCode() == keyEvent.getKeyCode()
                    && mPreKeyEvent.getAction() == keyEvent.getAction()
                    && (mPreKeyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN
                    || mPreKeyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP)) {
                startAutoScroll(keyEvent.getKeyCode(), SCROLL_FROM_DISPATCH);
                mPreKeyEvent = keyEvent;
                return true;
            } else if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                mPreKeyEvent = null;
                if (isAutoScroll) {
                    stopAutoScroll(SCROLL_FROM_DISPATCH);
                }
            } else {
                mPreKeyEvent = keyEvent;
            }
        }
        return handleFocus(keyEvent) || super.dispatchKeyEvent(keyEvent);

    }

    public void smoothScrollToPosition(int position, boolean isSlow) {
        if (getLayoutManager() == null) {
            Log.d(TAG,
                    "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else {
            if (getLayoutManager() instanceof BlockLinearLayoutManager) {
                BlockLinearLayoutManager blockLinearLayoutManager = (BlockLinearLayoutManager) getLayoutManager();
                blockLinearLayoutManager.smoothScrollToPosition(this, null, position, isSlow);
            }
        }
    }

    /**
     * 子类可以通过覆盖此方法拦截焦点搜索结果
     */
    protected boolean onFocusHandled(@Nullable Integer direction, boolean handled) {
        return handled;
    }

    /**
     * @param view 尝试获取焦点
     */
    protected final boolean requestFocusInternal(@Nullable View view) {
        return isDescendant(this, view) && view.requestFocus();
    }

    protected final boolean isDescendant(@Nullable View parent, @Nullable View child) {
        if ((child == null) || !(parent instanceof ViewParent)) {
            return false;
        }
        ViewParent childParent = child.getParent();
        View viewParent = (childParent instanceof View) ? (View) childParent : null;
        return childParent == parent || isDescendant(parent, viewParent);
    }

    void temporaryDetachFrescoView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof FocusProcessLayout) {
                child.onStartTemporaryDetach();
            } else if (child instanceof ViewGroup) {
                temporaryDetachFrescoView((ViewGroup) child);
            }
        }
    }

    void finishTemporaryDetachFrescoView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof FocusProcessLayout) {
                child.onFinishTemporaryDetach();
            } else if (child instanceof ViewGroup) {
                finishTemporaryDetachFrescoView((ViewGroup) child);
            }
        }
    }

    private static boolean isChildFullVisibleInParent(@NonNull ViewGroup parent, View child) {
        boolean visible = false;
        if (child != null) {
            Rect pBounds = new Rect();
            Rect cBounds = new Rect();
            parent.getDrawingRect(pBounds);
            child.getDrawingRect(cBounds);
            parent.offsetDescendantRectToMyCoords(child, cBounds);
            if (pBounds.contains(cBounds)) {
                visible = true;
            }
        }
        return visible;
    }

    /**
     * 通过{@link BlockRecyclerView#focusSearchInternal(View, int)}搜索焦点.
     * 如果未找到焦点, 但当前recycler view在滚动, 会将此次事件分发拦截.
     */
    private boolean handleFocus(KeyEvent event) {
        boolean hasNextFocus = false;
        View currentFocused = findFocus();
        if ((currentFocused == this) || !isDescendant(this, currentFocused)) {
            currentFocused = null;
        }
        View nextFocus = null;
        Integer direction = null;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    direction = FOCUS_UP;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    direction = FOCUS_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    direction = FOCUS_RIGHT;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    direction = FOCUS_LEFT;
                    break;
                default:
                    break;
            }
            if (direction != null) {
                long curTime = System.currentTimeMillis();
                if (curTime - lastOptTime < MIN_OPT_INTERVAL) {
                    return true;
                }
                lastOptTime = curTime;
                nextFocus = focusSearchInternal(currentFocused, direction);
                if (nextFocus == null && (direction == FOCUS_LEFT || direction == FOCUS_RIGHT)) {
                    return true;
                }
            }

        }

        if (isDescendant(this, nextFocus)) {
            hasNextFocus = requestFocusInternal(nextFocus);
        }

        // 特殊判断PlayerRootOrderView
        if (nextFocus != null) {
            Object obj = nextFocus.getTag(R.id.player_root_view);
            if (obj instanceof Boolean) {
                hasNextFocus = (Boolean) obj;
                nextFocus.requestFocus();
            }
        }

        if (hasNextFocus) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return onFocusHandled(direction, hasNextFocus || (getScrollState() != SCROLL_STATE_IDLE));
    }

    /**
     * 1. 若当前获取焦点的view属于blockHolder,
     * 通过{@link BlockHolder#handleFocus(RecyclerView, View, int)}搜索焦点, 若找到, 则直接返回结果.
     * <br/>
     * 2. 通过{@link BlockRecyclerView#searchFocusByFocusFinder(View, int)}搜索焦点, 若未找到, 则查找失败.
     * <br/>
     * 3. 第二步找到的焦点进行判断:
     * --3.1 若找到的焦点在与当前焦点同一个viewHolder中, 则返回结果.
     * --3.2 若非同一个viewHolder, 且
     *
     * @param direction 为{@link View#FOCUS_LEFT}或{@link View#FOCUS_RIGHT},
     *                  则结果无效, 查找失败.
     *                  --3.3 若非同一个viewHolder, 且为上下事件, 则优先返回{@link BlockHolder#requestFocusFromTop()} 或
     *                  {@link BlockHolder#requestFocusFromBottom()}寻找到的焦点.
     */
    @Nullable
    private View focusSearchInternal(View focused, int direction) {
        View nextFocus;
        View currFocusHolderItemView = getViewHolderItemView(focused);
        if (currFocusHolderItemView == null) {
            Log.e(TAG, "cannot get currFocusHolderItemView!");
            return null;
        }

        ViewHolder currFocusHolder = getChildViewHolder(currFocusHolderItemView);

        // 1. 若当前获取焦点的view属于blockHolder, 通过handleFocus搜索焦点
        if (currFocusHolder instanceof BlockHolder) {
            nextFocus = ((BlockHolder) currFocusHolder).handleFocus(this, focused, direction);
            if (nextFocus != null) {
                Log.d(TAG, "focus searched by BlockHolder#handleFocus()");
                return nextFocus;
            }
        }

        // 2. 通过FocusFinder搜索焦点
        nextFocus = searchFocusByFocusFinder(focused, direction);

        // 3. 找到的焦点进行判断
        if (nextFocus != null) {
            View nextFocusHolderItemView = getViewHolderItemView(nextFocus);
            if (focused.getParent() instanceof RecyclerView
                    && (direction == FOCUS_LEFT || direction == FOCUS_RIGHT)) {
                if (nextFocus.getParent() instanceof RecyclerView
                        && nextFocus.getParent() == focused.getParent()) {
                    Log.e(TAG, "当前focusView的父类是RecyclerView,且是左右滑动，nextFocus在同一RecyclerView");
                    return nextFocus;
                }
                Log.e(TAG, "当前focusView的父类是RecyclerView,且是左右滑动，nextFocus不在同一RecyclerView");
                return null;
            }
            // 3.1 若找到的焦点在同一个viewHolder中, 则返回结果
            if (nextFocusHolderItemView == currFocusHolderItemView) {
                Log.e(TAG, "focus searched by FocusFinder, in the same block");
                return nextFocus;
            }
            // 3.2 若不在同一个viewHolder中, 且为左右事件, 则查找失败
            if (direction == FOCUS_LEFT || direction == FOCUS_RIGHT) {
                Log.e(TAG, "horizontal event , focus searched in different block, search fail!");
                return null;
            }
            // 3.3 若不在同一个viewHolder中, 为上下事件
            int currFocusPosition = getChildByAdapterPosition(currFocusHolderItemView);
            if (currFocusPosition < 0) {
                Log.e(TAG, "currFocusHolderItemView is not in RecyclerView!");
                return null;
            }
            if (direction == FOCUS_UP) {
                ViewHolder nextViewHolder = getViewHolderByAdapterPosition(currFocusPosition - 1);
                if (nextViewHolder instanceof BlockHolder) {
                    View nextFocusTmp = ((BlockHolder) nextViewHolder).requestFocusFromBottom();
                    if (nextFocusTmp != null) {
                        Log.e(TAG, "focus searched by BlockHolder#requestFocusFromBottom()");
                        nextFocus = nextFocusTmp;
                    } else {
                        Log.d(TAG, "focus searched by FocusFinder in different block, up event");
                    }
                }
                return nextFocus;
            }
            if (direction == FOCUS_DOWN) {
                ViewHolder nextViewHolder = getViewHolderByAdapterPosition(currFocusPosition + 1);
                if (nextViewHolder instanceof BlockHolder) {
                    View nextFocusTmp = ((BlockHolder) nextViewHolder).requestFocusFromTop();
                    if (nextFocusTmp != null) {
                        Log.d(TAG, "focus searched by BlockHolder#requestFocusFromTop()");
                        nextFocus = nextFocusTmp;
                    } else {
                        Log.d(TAG, "focus searched by FocusFinder in different block, down event");
                    }
                }
                return nextFocus;
            }
            return null;
        }
        Log.d(TAG, "cannot get focus!");
        return null;
    }

    /**
     * 通过{@link FocusFinder#findNextFocus(ViewGroup, View, int)}搜索焦点.
     * 首次未找到, 会触发滚动, 并重新搜索焦点.
     */
    @Nullable
    private View searchFocusByFocusFinder(View focused, int direction) {
        if (!isDescendant(this, focused)) {
            Log.e(TAG,
                    "searchFocusByFocusFinder() - focused view is not descendant of recycler view!");
            return null;
        }
        View nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
        if (focused.getParent() instanceof RecyclerView && (direction == FOCUS_LEFT || direction == FOCUS_RIGHT)) {
            if (nextFocus != null && nextFocus.getParent() != focused.getParent()) {
                nextFocus = null;
            }
        }
        if (nextFocus == null && focused.getParent() instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) focused.getParent();
            if (recyclerView.getScrollState() == SCROLL_STATE_IDLE) {
                boolean retry = false;
                if (direction == FOCUS_LEFT) {
                    recyclerView.scrollBy(-mFocusSearchScrollDistance, 0);
                    retry = true;
                } else if (direction == FOCUS_RIGHT) {
                    recyclerView.scrollBy(mFocusSearchScrollDistance, 0);
                    retry = true;
                }
                if (retry) {
                    if (!isDescendant(this, focused)) {
                        Log.e(TAG,
                                "searchFocusByFocusFinder() - after horizontal retry, focused view is not descendant of recycler view!");
                        return null;
                    }
                    nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
                    Log.d(TAG, "retry focus search after horizontal scrolling." + " found=" + (nextFocus != null));
                }
            }
        }

        if (nextFocus == null && getScrollState() == SCROLL_STATE_IDLE) {
            boolean retry = false;
            switch (direction) {
                case FOCUS_UP:
                    scrollBy(0, -mFocusSearchScrollDistance);
                    retry = true;
                    break;
                case FOCUS_DOWN:
                    scrollBy(0, mFocusSearchScrollDistance);
                    retry = true;
                    break;
                default:
                    break;
            }
            if (retry) {
                if (!isDescendant(this, focused)) {
                    Log.e(TAG,
                            "searchFocusByFocusFinder() - after retry, focused view is not descendant of recycler view!");
                    return null;
                }
                nextFocus = FocusFinder.getInstance().findNextFocus(this, focused, direction);
                Log.d(TAG, "retry focus search after scrolling." + " found=" + (nextFocus != null));
            }
        }
        return nextFocus;
    }

    public boolean isFinishGetFocus() {
        return isFinishGetFocus;
    }

    // 递归寻找recycler view子view
    @Nullable
    private View getViewHolderItemView(@Nullable View view) {
        if (view == null || !(view.getParent() instanceof ViewGroup))
            return null;
        ViewGroup parent = (ViewGroup) view.getParent();
        return parent == this ? view : getViewHolderItemView(parent);
    }

    @Nullable
    private View getChildByAdapterPosition(int adapterPosition) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (BlockUtils.getAdapterPosition(child) == adapterPosition)
                return child;
        }
        return null;
    }

    @Nullable
    private ViewHolder getViewHolderByAdapterPosition(int adapterPosition) {
        View child = getChildByAdapterPosition(adapterPosition);
        return child == null ? null : getChildViewHolder(child);
    }

    private int getChildByAdapterPosition(@Nullable View child) {
        return BlockUtils.getAdapterPosition(child);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        ViewHolder firstVh = getViewHolderByAdapterPosition(0);
        View focus = null;
        if (firstVh instanceof BlockHolder) {
            focus = ((BlockHolder) firstVh).requestFocusFromTop();
        }

        if (focus != null && focus.requestFocus()) {
            return true;
        }

        return super.requestFocus(direction, previouslyFocusedRect);
    }

    private void longPressScroll(int keyCode) {
        mCurKeyCode = keyCode;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && getAdapter() != null) {
            this.smoothScrollToPosition(getAdapter().getItemCount());
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            this.smoothScrollToPosition(0, true);
        }
    }

    public interface IBlockFocusListener {
        /**
         * block获得焦点时回调
         * 移动焦点时可能被多次回调, 监听注意过滤
         */
        void onBlockGotFocus(@NonNull BlockInfo blockInfo);
    }

    public void destroy() {
        setBlockFocusListener(null);
    }

}