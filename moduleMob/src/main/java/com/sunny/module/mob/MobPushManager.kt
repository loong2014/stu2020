package com.sunny.module.mob

import android.content.Context
import com.mob.pushsdk.MobPush
import com.mob.pushsdk.MobPushCustomMessage
import com.mob.pushsdk.MobPushNotifyMessage
import com.mob.pushsdk.MobPushReceiver
import timber.log.Timber

/**
 * https://www.mob.com/wiki/detailed?wiki=513&id=136
 *
 * https://new.dashboard.mob.com/#/mobPush/manage
 */
object MobPushManager {

    var hasInit = false
    fun doInit() {
        Timber.i("doInit hasInit = $hasInit")
        if (hasInit) return
        hasInit = true

        Timber.i("addPushReceiver")
        MobPush.addPushReceiver(mobPushReceiver)
        MobPush.getRegistrationId {
            Timber.i("RegistrationId: $it")
        }

        // 推送服务是否开启
        Timber.i("isPushStopped = ${MobPush.isPushStopped()}")

        // 设置点击通知是否启动主页
        MobPush.setClickNotificationToLaunchMainActivity(true)

        // 设置通知图标
        MobPush.setNotifyIcon(R.mipmap.ic_launcher)

        // 设置是否显示角标
        MobPush.setShowBadge(true)
        // 设置显示的角标数，需要用户根据自己的逻辑设置。
        MobPush.setBadgeCounts(1)

        // 设置通知栏显示的最大通知条数， 需大于0。
        MobPush.setNotificationMaxCount(5)
    }

    fun doRelease() {
        Timber.i("doRelease")
        MobPush.removePushReceiver(mobPushReceiver)
        MobPush.stopPush()
    }

    private val mobPushReceiver: MobPushReceiver = object : MobPushReceiver {
        override fun onCustomMessageReceive(context: Context, message: MobPushCustomMessage) {
            Timber.i("onCustomMessageReceive : $message")
            //接收到自定义消息（透传消息）
            message.messageId //获取任务ID
            message.content //获取推送内容
        }

        /*
2022-07-28 09:48:56.282 20539-20539/com.sunny.app I/SunnyMob-MobPushManager$mobPushReceiver: onNotifyMessageReceive : MobPushNotifyMessage{style = 0, title = 'SunnyTime', content = 'Hello Sunny, Do You Miss Me?', styleContent = 'null', inboxStyleContent = null, extrasMap = {mobpush_link_k=mlink://com.sunny.mob.link, pushData={"mobpush_link_k":"mlink://com.sunny.mob.link","mobpush_link_v":"link=LinkAct&value=SUNNY"}, channel=mobpush, mobpush_link_v=link=LinkAct&value=SUNNY, id=4bp4tw9u32lxsmzwu8, schemeData={"link":"LinkAct","value":"SUNNY"}}, messageId = '4bp4tw9u32lxsmzwu8', timestamp = 1658972936280, voice = true, shake = true, light = false, channel = 0, notifySound = 'null', dropType = 0, dropId = 'null', mobNotifyId = '132765042', offlineFlag = 0, isGuardMsg = false, icon = 'null', image = 'null', androidBadgeType = 0, androidBadge = 0, androidChannelId = 'MobPush', importance = '3', notificationChannelName = '消息通知', isLockscreenVisible = 'false', allowBubbles = 'false'}
         */
        override fun onNotifyMessageReceive(context: Context, message: MobPushNotifyMessage) {
            Timber.i("onNotifyMessageReceive : $message")
            //接收到通知消息
            message.mobNotifyId //获取消息ID
            message.messageId //获取任务ID
            message.title //获取推送标题
            message.content //获取推送内容
            val schemeData = message.extrasMap["schemeData"]
            Timber.i("onNotifyMessageReceive schemeData :$schemeData")
        }

        override fun onNotifyMessageOpenedReceive(context: Context, message: MobPushNotifyMessage) {
            Timber.i("onNotifyMessageOpenedReceive : $message")
            //通知被点击事件
            message.mobNotifyId //获取消息ID
            message.messageId //获取任务ID
            message.title //获取推送标题
            message.content //获取推送内容
        }

        override fun onTagsCallback(
            context: Context,
            tags: Array<String>,
            operation: Int,
            errorCode: Int
        ) {
            Timber.i("onTagsCallback")
            //标签操作回调
            //tags: RegistrationId已添加的标签
            //operation: 0获取标签 1设置标签 2删除标签
            //errorCode: 0操作成功 非0操作失败
        }

        override fun onAliasCallback(
            context: Context,
            alias: String,
            operation: Int,
            errorCode: Int
        ) {
            Timber.i("onAliasCallback")
            //别名操作回调
            //alias: RegistrationId对应的别名
            //operation: 0获取别名 1设置别名 2删除别名
            //errorCode: 0操作成功 非0操作失败
        }
    }

}