package com.sunny.family.detail.view.common;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.family.R;
import com.sunny.family.detail.view.widget.BaseWidget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Created by ZhangBoshi
 * on 2019-12-12
 * 添加widget是通过{@link ViewGroup#addView(View)}添加到{@link T10000WidgetController#mRoot}实现的, 为了
 * 使widget无论添加到任何WidgetController实例都有相同的表现, 需要限制{@link T10000WidgetController#mRoot}的设置.
 * 因此不提供公有的WidgetController构造函数, 可以通过{@link T10000WidgetController#( ViewGroup )}或者
 * 获得{@link #()}WidgetController实例.
 */
public final class T10000WidgetController extends BasePosterController {
    private static final String TAG = "T10000WidgetController";
    private volatile boolean isAddWidgetAvailable = true;
    private static final int MAX_WIDTH_FOR_2_CORNER = 290;// 如果坑位的宽度小于该值，隐藏右角标
    private static final int MAX_WIDTH_R_B_CORNER = 160;// 右下角角标的最大宽度，此值为了限制左下标题的宽度
    private static final int WIDTH_ONE_TEXT = 30;// 右下角角标的最大宽度，此值为了限制左下标题的宽度
    @NonNull
    private final SparseArray<BaseWidget> mAddedWidgets = new SparseArray<>();

    @Nullable
    private ViewGroup mRoot;
    @Nullable
    private IWidgetFactory mWidgetFactory;
    @Nullable
    private WidgetPool mWidgetPool;
    private Poster mPoster;
    private boolean isHasAdCorner;

    private T10000WidgetController() {
    }

    @Override
    protected final void onBindView(@NonNull View v) {
        if (v instanceof ViewGroup) {
            mRoot = (ViewGroup) v;
        } else {
            throw new IllegalArgumentException("widget controller must bind view group");
        }
    }

    @Override
    protected final void onBindData(@Nullable Poster poster) {
        isAddWidgetAvailable = false;
        boolean hasFocus = getRoot().isFocused();
        for (int i = 0; i < mAddedWidgets.size(); i++) {
            int key = mAddedWidgets.keyAt(i);
            BaseWidget widget = mAddedWidgets.get(key);
            widget.bindData(poster);
            widget.onFocusChanged(hasFocus);
        }
    }

    @Override
    public void onFocusChanged(boolean focus, @NonNull View view) {
        int size = mAddedWidgets.size();
        for (int i = 0; i < size; i++) {
            int key = mAddedWidgets.keyAt(i);
            BaseWidget widget = mAddedWidgets.get(key);
            widget.onFocusChanged(focus);
        }

    }

    @Override
    public void onViewSelect(boolean isSelect) {
        super.onViewSelect(isSelect);
        int size = mAddedWidgets.size();
        for (int i = 0; i < size; i++) {
            int key = mAddedWidgets.keyAt(i);
            BaseWidget widget = mAddedWidgets.get(key);
            widget.onViewSelect(isSelect);
        }

    }

    public void setWidgetFactory(@Nullable IWidgetFactory widgetFactory) {
        mWidgetFactory = widgetFactory;
    }

    public void setWidgetPool(@Nullable WidgetPool widgetPool) {
        mWidgetPool = widgetPool;
    }

    public BaseWidget addWidget(int type) {
        checkStatus();
        checkAddAvailable();
        return addWidgetInner(type);
    }

    @Nullable
    public BaseWidget getWidget(int type) {
        return mAddedWidgets.get(type);
    }

    public void removeWidget(int type) {
        checkStatus();
        removeWidgetInner(type);
    }

    public void reset(int... types) {
        isAddWidgetAvailable = true;
        checkStatus();

        Set<Integer> rmvSet = new HashSet<>();
        int size = mAddedWidgets.size();
        for (int i = 0; i < size; i++) {
            rmvSet.add(mAddedWidgets.keyAt(i));
        }

        for (int type : types) {
            addWidgetInner(type);
            rmvSet.remove(type);
        }

        for (int rmvType : rmvSet) {
            removeWidgetInner(rmvType);
        }

    }

    public void clear() {
        checkStatus();
        int size = mAddedWidgets.size();
        for (int i = 0; i < size; i++) {
            removeWidgetInner(mAddedWidgets.keyAt(i));
        }

        isAddWidgetAvailable = true;
    }

    /**
     * 进入此方法前应先调用{@link T10000WidgetController#checkStatus()},
     * {@link T10000WidgetController#checkAddAvailable()}
     */
    @SuppressWarnings("all")
    private BaseWidget addWidgetInner(int type) {
        BaseWidget widget = mAddedWidgets.get(type);
        // 1. 是否已加载
        if (widget == null) {
            // 2. 从pool中取
            widget = mWidgetPool.get(type);
            if (widget == null) {
                // 3. 从factory创建
                widget = mWidgetFactory.createWidget(getRoot(), type);
                widget.setWidgetType(type);
            }
            widget.addToController(getRoot());
            mAddedWidgets.put(type, widget);
        }
        return widget;
    }

    /**
     * 进入此方法前应先调用{@link T10000WidgetController#checkStatus()}
     */
    @SuppressWarnings("all")
    private void removeWidgetInner(int type) {
        BaseWidget widget = mAddedWidgets.get(type);
        if (widget == null)
            return;
        widget.removeFromController(getRoot());
        mAddedWidgets.remove(type);
        mWidgetPool.put(widget);
    }

    @NonNull
    private ViewGroup getRoot() {
        if (mRoot == null)
            throw new IllegalStateException("widget controller must bind view first!");
        return mRoot;
    }

    private void checkStatus() {
        if (mWidgetPool == null)
            throw new IllegalStateException("pool is null!");
        if (mWidgetFactory == null)
            throw new IllegalStateException("factory is null!");
    }

    private void checkAddAvailable() {
        if (!isAddWidgetAvailable)
            throw new IllegalStateException("add is not available now!");
    }

    public interface IWidgetFactory {
        @NonNull
        BaseWidget createWidget(@NonNull ViewGroup parent, int widgetType);

        int getWidgetType(String tagType, String position);
    }

    public static T10000WidgetController create(@NonNull ViewGroup parent, int viewType) {
        View root;
        if (viewType == ControllerType.WIDGET_10001) {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.controller_t10001,
                    parent, false);
        } else {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.controller_t10000,
                    parent, false);
        }

        T10000WidgetController controller = new T10000WidgetController();
        controller.doBindView(root);
        return controller;
    }

    /**
     * 不允许外部重新绑定View, 为了保证widget加载在同样的父布局上
     */
    @Override
    protected boolean bindViewAvailable() {
        return false;
    }

    public static class WidgetPool {
        private final SparseArray<ArrayList<BaseWidget>> mScrap = new SparseArray<>();
        private final SparseIntArray mMaxScrap = new SparseIntArray();
        private static final int DEFAULT_MAX_SCRAP = 20;

        @Nullable
        public BaseWidget get(int type) {
            ArrayList<BaseWidget> scrapHeap = this.mScrap.get(type);
            if (scrapHeap != null && !scrapHeap.isEmpty()) {
                int index = scrapHeap.size() - 1;
                BaseWidget scrap = scrapHeap.get(index);
                scrapHeap.remove(index);
                return scrap;
            } else {
                return null;
            }
        }

        public void put(@NonNull BaseWidget widget) {
            int widgetType = widget.getWidgetType();
            ArrayList<BaseWidget> scrapHeap = this.getScrapHeapForType(widgetType);
            if (this.mMaxScrap.get(widgetType) > scrapHeap.size()) {
                scrapHeap.add(widget);
            }
        }

        public void clear() {
            this.mScrap.clear();
        }

        public void setMax(int type, int max) {
            this.mMaxScrap.put(type, max);
            ArrayList<BaseWidget> scrapHeap = this.mScrap.get(type);
            if (scrapHeap != null) {
                while (scrapHeap.size() > max) {
                    scrapHeap.remove(scrapHeap.size() - 1);
                }
            }
        }

        private ArrayList<BaseWidget> getScrapHeapForType(int type) {
            ArrayList<BaseWidget> scrap = this.mScrap.get(type);
            if (scrap == null) {
                scrap = new ArrayList<>();
                this.mScrap.put(type, scrap);
                if (this.mMaxScrap.indexOfKey(type) < 0) {
                    this.mMaxScrap.put(type, DEFAULT_MAX_SCRAP);
                }
            }
            return scrap;
        }
    }

    public Poster getPoster() {
        return mPoster;
    }
}