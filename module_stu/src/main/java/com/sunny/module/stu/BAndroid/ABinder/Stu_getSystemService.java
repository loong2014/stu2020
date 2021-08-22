package com.sunny.module.stu.BAndroid.ABinder;

import android.content.Context;
import android.view.WindowManager;

import com.sunny.module.stu.base.StuImpl;


public class Stu_getSystemService extends StuImpl {

    @Override
    public void p_流程() {
        tryGetService(null);
    }

    private void tryGetService(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //1 ContextImpl#getSystemService("window")
        /*
        class ContextImpl extends Context {
            @Override
            public Object getSystemService(String name) {
                return SystemServiceRegistry.getSystemService(this, name);
            }
        }
         */

        //2 SystemServiceRegistry#getSystemService(ContextImpl.this,"window")
        /*
        final class SystemServiceRegistry {
            // 1
            private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS =
                    new ArrayMap<String, ServiceFetcher<?>>();

            // 2
            static {
                registerService(Context.WINDOW_SERVICE, WindowManager.class,
                        new CachedServiceFetcher<WindowManager>() {
                    @Override
                    public WindowManager createService(ContextImpl ctx) {
                        return new WindowManagerImpl(ctx);
                    }});
            }

            // 3
            public static Object getSystemService(ContextImpl ctx, String name) {
                ServiceFetcher<?> fetcher = SYSTEM_SERVICE_FETCHERS.get(name);
                return fetcher != null ? fetcher.getService(ctx) : null;
            }
        }
         */

        //3 new WindowManagerImpl(ctx)
    }
}
