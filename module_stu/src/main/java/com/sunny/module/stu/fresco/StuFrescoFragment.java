package com.sunny.module.stu.fresco;

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.generic.RoundingParams;
import com.sunny.lib.common.utils.ResUtils;
import com.sunny.module.stu.R;
import com.sunny.module.stu.base.StuBaseFragment;

public class StuFrescoFragment extends StuBaseFragment {

    @Override
    protected int buildLayoutResID() {
        return R.layout.fragment_stu_fresco;
    }


    public void stuAutoRotateDrawable() {

        //自动旋转图片
        AutoRotateDrawable drawable =
                new AutoRotateDrawable(
                        ResUtils.getDrawable(R.drawable.brvah_sample_footer_loading),
                        200);

        //圆角参数
        RoundingParams roundingParams = new RoundingParams();

        //
        RoundingParams.RoundingMethod roundingMethod;

        //默认BITMAP_ONLY，使用BitmapShader进行圆角绘制，只支持：CENTER_CROP，FOCUS_CROP，FIT_XY
        roundingMethod = RoundingParams.RoundingMethod.BITMAP_ONLY;

        // 当设置roundWithOverlayColor叠加颜色时，更改圆角绘制方法为OVERLAY_COLOR
        //通过在底层图层上，通过指定颜色画圆角，也就是将圆角颜色设置为跟底图颜色一致，来达到圆角效果
        //只有当底层图层颜色时静态，且纯色时有效
        roundingMethod = RoundingParams.RoundingMethod.OVERLAY_COLOR;

        roundingParams.setRoundingMethod(roundingMethod);

    }

}
