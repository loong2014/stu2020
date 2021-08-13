package com.sunny.module.stu.C性能优化.A启动优化;

//应用启动流程

import com.sunny.module.stu.base.StuImpl;

class d统计启动时间 extends StuImpl {

    @Override
    public void c_功能() {
        // 统计应用启动时长，用于对比优化效果

        am_start命令();

        /*
        在应用的 application 的 attachBaseContext 中添加 startTime
        在第一个 activity 的 onWindowFocusChanged 中添加 finishTime
        计算应用启动时间
         */

    }

    void am_start命令() {

    /*
    adb shell am start -S -W [包名]/[启动类的全限定名]
        -S 表示重启当前应用
        -W 等待应用启动

// 命令
adb shell am start -S -W com.letv.tv/com.letv.tv.welcome.SplashActivity
adb shell am start -S -W com.letv.tv/com.letv.tv.home.DebugActivity

// 输出
zhangxin:~ qazwsx$ adb shell am start -S -W com.letv.tv/com.letv.tv.welcome.SplashActivity
Stopping: com.letv.tv
Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.letv.tv/.welcome.SplashActivity }
Status: ok
Activity: com.letv.tv/.home.HomeActivity
ThisTime: 763
TotalTime: 2291
WaitTime: 2326
Complete

// 解释
Activity: com.letv.tv/.home.HomeActivity // 当前可见 Activity
ThisTime: 763 // 当前 Activity 的启动耗时，这里指 HomeActivity
TotalTime: 2291 // 从app启动到当前可见 Activity 的总耗时
WaitTime: 2326 // ActivityManagerService启动App的Activity时的总时间（包括当前Activity的onPause()和自己Activity的启动）
     */

        // 过滤`displayed`输出的启动日志
    /*

// 输出
08-13 11:48:24.880 2310-2422/system_process I/ActivityManager: Displayed com.letv.tv/.home.HomeActivity: +763ms (total +2s291ms)

// 解释
+763ms (total +2s291ms)
+763ms: // HomeActivity 的启动耗时，这里指 HomeActivity
(total +2s291ms): // 从app启动到当前可见 Activity 的总耗时
     */
    }

    void 常用系统页面启动() {

        // 1. adb 打开设置：
        /*
// 主页面
adb shell am start com.android.settings/com.android.settings.Settings

com.android.settings.SecuritySettings // 安全
com.android.settings.RadioInfo // 手机无线信息
com.android.settings.AccessibilitySettings //辅助功能设置
com.android.settings.ActivityPicker //选择活动
com.android.settings.ApnSettings //APN设置
com.android.settings.ApplicationSettings //应用程序设置
com.android.settings.BandMode //设置GSM/UMTS波段
com.android.settings.BatteryInfo //电池信息
com.android.settings.DateTimeSettings //日期和坝上旅游网时间设置
com.android.settings.DateTimeSettingsSetupWizard //日期和时间设置
com.android.settings.DevelopmentSettings //开发者设置
com.android.settings.DeviceAdminSettings //设备管理器
com.android.settings.DeviceInfoSettings //关于手机
com.android.settings.Display //显示——设置显示字体大小及预览
com.android.settings.DisplaySettings //显示设置
com.android.settings.DockSettings //底座设置
com.android.settings.IccLockSettings //SIM卡锁定设置
com.android.settings.InstalledAppDetails //语言和键盘设置
com.android.settings.LanguageSettings //语言和键盘设置
com.android.settings.LocalePicker //选择手机语言
com.android.settings.LocalePickerInSetupWizard //选择手机语言
com.android.settings.ManageApplications //已下载（安装）软件列表
com.android.settings.MasterClear //恢复出厂设置
com.android.settings.MediaFormat //格式化手机闪存
com.android.settings.PhysicalKeyboardSettings //设置键盘
com.android.settings.PrivacySettings //隐私设置
com.android.settings.ProxySelector //代理设置
com.android.settings.RadioInfo //手机信息
com.android.settings.RunningServices //正在运行的程序（服务）
com.android.settings.SecuritySettings //位置和安全设置
com.android.settings.Settings //系统设置
com.android.settings.SettingsSafetyLegalActivity //安全信息
com.android.settings.SoundSettings //声音设置
com.android.settings.TestingSettings //测试——显示手机信息、电池信息、使用情况统计、Wifi information、服务信息
com.android.settings.TetherSettings //绑定与便携式热点
com.android.settings.TextToSpeechSettings //文字转语音设置
com.android.settings.UsageStats //使用情况统计
com.android.settings.UserDictionarySettings //用户词典
com.android.settings.VoiceInputOutputSettings //语音输入与输出设置
com.android.settings.WirelessSettings //无线和网络设置
         */

        // 2. adb 打开摄像头：
        /*
        adb shell am start -n com.android.camera/com.android.camera.Camera
         */

        // 3. Browser（浏览器）的启动方法为：
        /*
        adb shell  am start -n com.android.browser/com.android.browser.BrowserActivity
         */

        // 4. 启动多媒体
        /*
adb shell am start -n com.android.music/com.android.music.MusicBrowserActivity
adb shell am start -n com.android.music/com.android.music.VideoBrowserActivity
adb shell am start -n com.android.music/com.android.music.MediaPlaybackActivity
         */
    }

    void stu_am_start() {
/*
am start: start an Activity.  Options are:
    -D: enable debugging

    -W: wait for launch to complete

    --start-profiler <FILE>: start profiler and send results to <FILE>

    --sampling INTERVAL: use sample profiling with INTERVAL microseconds
        between samples (use with --start-profiler)

    -P <FILE>: like above, but profiling stops when app goes idle

    -R: repeat the activity launch <COUNT> times.  Prior to each repeat,
        the top activity will be finished.

    -S: force stop the target app before starting the activity

    --opengl-trace: enable tracing of OpenGL functions

    --user <USER_ID> | current: Specify which user to run as; if not
        specified then run as the current user.
 */
    }

}

