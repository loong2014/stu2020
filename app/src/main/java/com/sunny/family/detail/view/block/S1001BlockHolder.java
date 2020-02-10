package com.sunny.family.detail.view.block;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.R;
import com.sunny.family.detail.view.common.BasePosterController;
import com.sunny.family.detail.view.common.BlockInfo;
import com.sunny.family.detail.view.common.ControllerType;
import com.sunny.family.detail.view.common.HorizontalAdapter;
import com.sunny.family.detail.view.common.IControllerFactory;
import com.sunny.family.detail.view.common.Poster;
import com.sunny.family.detail.view.common.RebuildDataModel;
import com.sunny.family.detail.view.common.T10000WidgetController;
import com.sunny.family.detail.view.widget.WidgetType;
import com.sunny.lib.utils.ResUtils;

import java.util.List;

/**
 * Created by ZhangBoshi
 * on 2019-12-12
 * 一行六竖图
 */

public class S1001BlockHolder extends DetailBlockHolder {

    private final TextView mTitle;
    @NonNull
    private final RecyclerView mRecyclerView;
    @NonNull
    private final HorizontalAdapter mAdapter;
    private View currentFocusView;
    private String TAG = "S1001BlockHolder----";


    private S1001BlockHolder(View root, IControllerFactory factory,
                             RecyclerView.RecycledViewPool pool) {
        super(root);

        mTitle = (TextView) root.findViewById(R.id.title);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        HorizontalAdapter.AdapterCallback callback = new HorizontalAdapter.AdapterCallback() {
            @Override
            public void onBindController(@NonNull BasePosterController controller,
                                         @Nullable Poster poster, int position) {
                if (controller instanceof T10000WidgetController) {

                    T10000WidgetController controller1 = (T10000WidgetController) controller;
                    controller1.reset(WidgetType.WIDGET_101, WidgetType.WIDGET_102);

                    bindControllerData(controller, poster, position);
                }
                if (position == 0) {
                    currentFocusView = controller.getView();

                }

            }

            @Override
            public int getControllerType(int position) {
                return ControllerType.WIDGET_10000;
            }
        };

        mAdapter = HorizontalAdapter.Builder.create().setCallback(callback)
                .setControllerFactory(factory).setRecyclerView(mRecyclerView)
                .setRecyclerViewPool(pool)
//                .setSize(6).setSpanCount(6).build();
                .setWidth(ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_s1001_width)).build();
    }

    @Override
    protected void onFocusChangePoster(View v, boolean hasFocus, BasePosterController poster) {
        super.onFocusChangePoster(v, hasFocus, poster);
        if (hasFocus)
            currentFocusView = v;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
    }

    @Override
    protected void onRecycled() {
        super.onRecycled();
    }

    @Nullable
    @Override
    public View requestFocusFromTop() {
        if (currentFocusView != null) {
            return currentFocusView;
        }
        return super.requestFocusFromTop();
    }

    @Nullable
    @Override
    public View requestFocusFromBottom() {
        if (currentFocusView != null) {
            return currentFocusView;
        }
        return super.requestFocusFromBottom();
    }

    @Override
    protected void onBindBlockData(@NonNull BlockInfo info) {
        RebuildDataModel model = info.getBlockModel();
        if (model == null)
            return;
        if (!TextUtils.isEmpty(model.getName())) {
            TAG = TAG + model.getName();
            mTitle.setText(model.getName());
        }
        if (model.getData() != null) {
            List<Poster> posters = (List<Poster>) model.getData();
            mAdapter.bindData(posters);

        }

    }

    public static S1001BlockHolder create(@NonNull ViewGroup parent,
                                          @NonNull IControllerFactory factory, RecyclerView.RecycledViewPool sControllerPool) {
        Context context = parent.getContext();
        View root = LayoutInflater.from(context).inflate(R.layout.block_s1001, parent, false);

        View recyclerView = root.findViewById(R.id.recycler_view);
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = ResUtils.INSTANCE.getDimensionPixelSize(R.dimen.block_s1001_height);
        recyclerView.setLayoutParams(params);

        return new S1001BlockHolder(root, factory, sControllerPool);
    }

}