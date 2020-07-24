package com.sunny.family.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sunny.family.R;
import com.sunny.lib.utils.SunToast;

/**
 * Created by zhangxin17 on 2020/7/7
 */
public class MyAlertDialogFragment extends DialogFragment {

    public static MyAlertDialogFragment buildDialog(String title) {
        MyAlertDialogFragment dialogFragment = new MyAlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);

        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        builder.setMessage("删除歌曲?");
        builder.setIcon(R.drawable.jz_volume_icon);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SunToast.show("确定");
                DialogClickListener listener = (DialogClickListener) getActivity();
                listener.onConfirmClick();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SunToast.show("取消");
            }
        });

        // 设置dialog位置
        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.white);
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width=WindowManager.LayoutParams.MATCH_PARENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        return dialog;
    }
}
