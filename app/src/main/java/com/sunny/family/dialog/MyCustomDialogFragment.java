package com.sunny.family.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sunny.family.R;
import com.sunny.lib.utils.SunToast;

/**
 * Created by zhangxin17 on 2020/7/7
 */
public class MyCustomDialogFragment extends DialogFragment {

    public static MyCustomDialogFragment buildDialog(String title) {
        MyCustomDialogFragment dialogFragment = new MyCustomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialogFragment.setArguments(bundle);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);
        return dialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_custom, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        TextView tipView = view.findViewById(R.id.tv_tip);
        tipView.setText("请输入用户 " + title + " 对应的密码:");

        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSubmit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void doSubmit() {
        EditText pwdView = getView().findViewById(R.id.et_pwd);

        String pwd = pwdView.getText().toString();

        dismiss();

        SunToast.show("pwd :" + pwd);
        DialogClickListener listener = (DialogClickListener) getActivity();
        listener.onSubmitClick(pwd);
    }
}
