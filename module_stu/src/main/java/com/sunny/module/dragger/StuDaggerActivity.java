//package com.sunny.module.dragger;
//
//
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.sunny.lib.common.router.RouterConstant;
//import com.sunny.module.dragger.component.DaggerUserComponent;
//import com.sunny.module.dragger.module.LoginService;
//import com.sunny.module.dragger.module.UserModule;
//import com.sunny.module.stu.R;
//
//import javax.inject.Inject;
//
//@Route(path = RouterConstant.Stu.PageDagger)
//public class StuDaggerActivity extends AppCompatActivity {
//
//    @Inject
//    LoginService mLoginService;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_stu_dagger);
//
//        // 使用形式1
////        DaggerUserComponent.builder().userModule(new UserModule()).build().inject(this);
//        // 使用形式2
//        DaggerUserComponent.create().inject(this);
//
//        mLoginService.doLogin();
//    }
//}
