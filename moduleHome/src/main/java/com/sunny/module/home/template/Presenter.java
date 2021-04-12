package com.sunny.module.home.template;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class Presenter {

    public static class ViewHolder {
        public final View view;
        private Context context;

        public ViewHolder(View view) {
            this.view = view;
        }

        public Context getContext() {
            if (context == null) {
                context = view.getContext();
            }
            return context;
        }
    }

    public abstract ViewHolder onCreateViewHolder(ViewGroup parent);

    public abstract void onBindViewHolder(ViewHolder viewHolder, Object item, int position);


    public void onViewAttachedToWindow(ViewHolder holder) {
    }
    public void onViewDetachedFromWindow(ViewHolder holder) {
    }

}
