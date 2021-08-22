package com.sunny.module.stu.BAndroid.ABinder;

import android.content.Context;
import android.content.Intent;

import com.sunny.module.stu.BAndroid.ABinder.service.SunnyService;
import com.sunny.module.stu.base.StuImpl;


public class Stu_startService extends StuImpl {

    @Override
    public void p_流程() {
        super.p_流程();


    }

    private void tryStartService(Context context) {
        Intent intent = new Intent(context, SunnyService.class);
        context.startService(intent);
        //1 ContextImpl#startService(service)
        /*
        class ContextImpl extends Context {

            @Override
            public ComponentName startService(Intent service) {
                warnIfCallingFromSystemProcess();
                return startServiceCommon(service, false, mUser);
            }

            private ComponentName startServiceCommon(Intent service, boolean requireForeground, UserHandle user) {
                try {
                    validateServiceIntent(service);
                    service.prepareToLeaveProcess(this);
                    ComponentName cn = ActivityManager.getService().startService(
                                    mMainThread.getApplicationThread(),
                                    service,
                                    service.resolveTypeIfNeeded(getContentResolver()),
                                    requireForeground,  // false
                                    getOpPackageName(),
                                    user.getIdentifier()
                                    );
                    if (cn != null) {
                        ...
                    }
                    return cn;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
         */
        //2 ActivityManager#getService()
        // 最终返回 ActivityManagerService
        /*
        public class ActivityManager {

            // 1
            private static final Singleton<IActivityManager> IActivityManagerSingleton =
                new Singleton<IActivityManager>() {
                    @Override
                    protected IActivityManager create() {
                        final IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                        final IActivityManager am = IActivityManager.Stub.asInterface(b);
                        return am;
                    }
                };

            // 2
            public static IActivityManager getService() {
                return IActivityManagerSingleton.get();
            }
        }
         */

        //3 ActivityManagerService#getService("activity")
        /*
        public class ActivityManagerService {

            // 1
            final ActiveServices mServices;

            // 2
            ActivityManagerService(){
                ...
                mServices = new ActiveServices(this);
                ...
            }

            @Override
            public ComponentName startService(
                        IApplicationThread caller,
                        Intent service,
                        String resolvedType,
                        boolean requireForeground,
                        String callingPackage,
                        int userId)
                    throws TransactionTooLargeException {
                ...
                synchronized(this) {
                    final int callingPid = Binder.getCallingPid();
                    final int callingUid = Binder.getCallingUid();
                    final long origId = Binder.clearCallingIdentity();
                    ComponentName res;
                    try {
                        res = mServices.startServiceLocked(
                                        caller,
                                        service,
                                        resolvedType,
                                        callingPid,
                                        callingUid,
                                        requireForeground,
                                        callingPackage,
                                        userId);
                    } finally {
                        ...
                    }
                    return res;
                }
            }
        }
         */

        // ActiveServices#startServiceLocked

        /*
        public final class ActiveServices {

            ComponentName startServiceLocked(
                            IApplicationThread caller,
                            Intent service,
                            String resolvedType,
                            int callingPid,
                            int callingUid,
                            boolean fgRequired,
                            String callingPackage,
                            final int userId,
                            boolean allowBackgroundActivityStarts)
                throws TransactionTooLargeException {
            ...

        final boolean callerFg;
        if (caller != null) {
            final ProcessRecord callerApp = mAm.getRecordForAppLocked(caller);
            callerFg = callerApp.setSchedGroup != ProcessList.SCHED_GROUP_BACKGROUND;
        } else {
            callerFg = true;
        }

        ServiceLookupResult res =
            retrieveServiceLocked(service, null, resolvedType, callingPackage,
                    callingPid, callingUid, userId, true, callerFg, false, false);
        if (res == null) {
            return null;
        }
        if (res.record == null) {
            return new ComponentName("!", res.permission != null
                    ? res.permission : "private to package");
        }

        ServiceRecord r = res.record;

        if (!mAm.mUserController.exists(r.userId)) {
            Slog.w(TAG, "Trying to start service with non-existent user! " + r.userId);
            return null;
        }

        final boolean bgLaunch = !mAm.isUidActiveLocked(r.appInfo.uid);

        if (bgLaunch && appRestrictedAnyInBackground(r.appInfo.uid, r.packageName)) {
            forcedStandby = true;
        }

        // If this is a direct-to-foreground start, make sure it is allowed as per the app op.
        boolean forceSilentAbort = false;
        if (fgRequired) {
            final int mode = mAm.mAppOpsService.checkOperation(
                    AppOpsManager.OP_START_FOREGROUND, r.appInfo.uid, r.packageName);
            switch (mode) {
                case AppOpsManager.MODE_ALLOWED:
                case AppOpsManager.MODE_DEFAULT:
                    // All okay.
                    break;
                case AppOpsManager.MODE_IGNORED:
                    // Not allowed, fall back to normal start service, failing siliently
                    // if background check restricts that.
                    Slog.w(TAG, "startForegroundService not allowed due to app op: service "
                            + service + " to " + r.shortInstanceName
                            + " from pid=" + callingPid + " uid=" + callingUid
                            + " pkg=" + callingPackage);
                    fgRequired = false;
                    forceSilentAbort = true;
                    break;
                default:
                    return new ComponentName("!!", "foreground not allowed as per app op");
            }
        }

        // If this isn't a direct-to-foreground start, check our ability to kick off an
        // arbitrary service
        if (forcedStandby || (!r.startRequested && !fgRequired)) {
            // Before going further -- if this app is not allowed to start services in the
            // background, then at this point we aren't going to let it period.
            final int allowed = mAm.getAppStartModeLocked(r.appInfo.uid, r.packageName,
                    r.appInfo.targetSdkVersion, callingPid, false, false, forcedStandby);
            if (allowed != ActivityManager.APP_START_MODE_NORMAL) {
                Slog.w(TAG, "Background start not allowed: service "
                        + service + " to " + r.shortInstanceName
                        + " from pid=" + callingPid + " uid=" + callingUid
                        + " pkg=" + callingPackage + " startFg?=" + fgRequired);
                if (allowed == ActivityManager.APP_START_MODE_DELAYED || forceSilentAbort) {
                    // In this case we are silently disabling the app, to disrupt as
                    // little as possible existing apps.
                    return null;
                }
                if (forcedStandby) {
                    // This is an O+ app, but we might be here because the user has placed
                    // it under strict background restrictions.  Don't punish the app if it's
                    // trying to do the right thing but we're denying it for that reason.
                    if (fgRequired) {
                        if (DEBUG_BACKGROUND_CHECK) {
                            Slog.v(TAG, "Silently dropping foreground service launch due to FAS");
                        }
                        return null;
                    }
                }
                // This app knows it is in the new model where this operation is not
                // allowed, so tell it what has happened.
                UidRecord uidRec = mAm.mProcessList.getUidRecordLocked(r.appInfo.uid);
                return new ComponentName("?", "app is in background uid " + uidRec);
            }
        }

        // At this point we've applied allowed-to-start policy based on whether this was
        // an ordinary startService() or a startForegroundService().  Now, only require that
        // the app follow through on the startForegroundService() -> startForeground()
        // contract if it actually targets O+.
        if (r.appInfo.targetSdkVersion < Build.VERSION_CODES.O && fgRequired) {
            if (DEBUG_BACKGROUND_CHECK || DEBUG_FOREGROUND_SERVICE) {
                Slog.i(TAG, "startForegroundService() but host targets "
                        + r.appInfo.targetSdkVersion + " - not requiring startForeground()");
            }
            fgRequired = false;
        }

        NeededUriGrants neededGrants = mAm.mUgmInternal.checkGrantUriPermissionFromIntent(
                callingUid, r.packageName, service, service.getFlags(), null, r.userId);

        // If permissions need a review before any of the app components can run,
        // we do not start the service and launch a review activity if the calling app
        // is in the foreground passing it a pending intent to start the service when
        // review is completed.

        // XXX This is not dealing with fgRequired!
        if (!requestStartTargetPermissionsReviewIfNeededLocked(r, callingPackage,
                callingUid, service, callerFg, userId)) {
            return null;
        }

        if (unscheduleServiceRestartLocked(r, callingUid, false)) {
            if (DEBUG_SERVICE) Slog.v(TAG_SERVICE, "START SERVICE WHILE RESTART PENDING: " + r);
        }
        r.lastActivity = SystemClock.uptimeMillis();
        r.startRequested = true;
        r.delayedStop = false;
        r.fgRequired = fgRequired;
        r.pendingStarts.add(new ServiceRecord.StartItem(r, false, r.makeNextStartId(),
                service, neededGrants, callingUid));

        if (fgRequired) {
            // We are now effectively running a foreground service.
            ServiceState stracker = r.getTracker();
            if (stracker != null) {
                stracker.setForeground(true, mAm.mProcessStats.getMemFactorLocked(),
                        r.lastActivity);
            }
            mAm.mAppOpsService.startOperation(AppOpsManager.getToken(mAm.mAppOpsService),
                    AppOpsManager.OP_START_FOREGROUND, r.appInfo.uid, r.packageName, true);
        }

        final ServiceMap smap = getServiceMapLocked(r.userId);
        boolean addToStarting = false;
        if (!callerFg && !fgRequired && r.app == null
                && mAm.mUserController.hasStartedUserState(r.userId)) {
            ProcessRecord proc = mAm.getProcessRecordLocked(r.processName, r.appInfo.uid, false);
            if (proc == null || proc.getCurProcState() > ActivityManager.PROCESS_STATE_RECEIVER) {
                // If this is not coming from a foreground caller, then we may want
                // to delay the start if there are already other background services
                // that are starting.  This is to avoid process start spam when lots
                // of applications are all handling things like connectivity broadcasts.
                // We only do this for cached processes, because otherwise an application
                // can have assumptions about calling startService() for a service to run
                // in its own process, and for that process to not be killed before the
                // service is started.  This is especially the case for receivers, which
                // may start a service in onReceive() to do some additional work and have
                // initialized some global state as part of that.
                if (DEBUG_DELAYED_SERVICE) Slog.v(TAG_SERVICE, "Potential start delay of "
                        + r + " in " + proc);
                if (r.delayed) {
                    // This service is already scheduled for a delayed start; just leave
                    // it still waiting.
                    if (DEBUG_DELAYED_STARTS) Slog.v(TAG_SERVICE, "Continuing to delay: " + r);
                    return r.name;
                }
                if (smap.mStartingBackground.size() >= mMaxStartingBackground) {
                    // Something else is starting, delay!
                    Slog.i(TAG_SERVICE, "Delaying start of: " + r);
                    smap.mDelayedStartList.add(r);
                    r.delayed = true;
                    return r.name;
                }
                if (DEBUG_DELAYED_STARTS) Slog.v(TAG_SERVICE, "Not delaying: " + r);
                addToStarting = true;
            } else if (proc.getCurProcState() >= ActivityManager.PROCESS_STATE_SERVICE) {
                // We slightly loosen when we will enqueue this new service as a background
                // starting service we are waiting for, to also include processes that are
                // currently running other services or receivers.
                addToStarting = true;
                if (DEBUG_DELAYED_STARTS) Slog.v(TAG_SERVICE,
                        "Not delaying, but counting as bg: " + r);
            } else if (DEBUG_DELAYED_STARTS) {
                StringBuilder sb = new StringBuilder(128);
                sb.append("Not potential delay (state=").append(proc.getCurProcState())
                        .append(' ').append(proc.adjType);
                String reason = proc.makeAdjReason();
                if (reason != null) {
                    sb.append(' ');
                    sb.append(reason);
                }
                sb.append("): ");
                sb.append(r.toString());
                Slog.v(TAG_SERVICE, sb.toString());
            }
        } else if (DEBUG_DELAYED_STARTS) {
            if (callerFg || fgRequired) {
                Slog.v(TAG_SERVICE, "Not potential delay (callerFg=" + callerFg + " uid="
                        + callingUid + " pid=" + callingPid + " fgRequired=" + fgRequired + "): " + r);
            } else if (r.app != null) {
                Slog.v(TAG_SERVICE, "Not potential delay (cur app=" + r.app + "): " + r);
            } else {
                Slog.v(TAG_SERVICE,
                        "Not potential delay (user " + r.userId + " not started): " + r);
            }
        }

        if (allowBackgroundActivityStarts) {
            r.whitelistBgActivityStartsOnServiceStart();
        }

        ComponentName cmp = startServiceInnerLocked(smap, service, r, callerFg, addToStarting);
        return cmp;
    }

         */


    }

    private void 注册服务() {

        // ServiceManager.addService(name, service, allowIsolated);
        // http://androidxref.com/6.0.1_r10/xref/frameworks/base/core/java/android/os/ServiceManager.java
        /*
        public static void addService(String name, IBinder service, boolean allowIsolated) {
            try {
                //～～～
                getIServiceManager().addService(name, service, allowIsolated);
                === ServiceManagerProxy.addService(name, service, allowIsolated);

            } catch (RemoteException e) {
                Log.e(TAG, "error in addService", e);
            }
        }
         */

        // ServiceManagerProxy.addService(name, service, allowIsolated);
        // http://androidxref.com/6.0.1_r10/xref/frameworks/base/core/java/android/os/ServiceManagerNative.java
        // 内含 ServiceManagerProxy 类
        /*
        class ServiceManagerProxy implements IServiceManager {
            private IBinder mRemote;

            // remote === new BinderProxy()
            public ServiceManagerProxy(IBinder remote) {
                mRemote = remote;
            }

            public IBinder asBinder() {
                return mRemote;
            }
            ...
            public void addService(String name, IBinder service, boolean allowIsolated)
                    throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInterfaceToken(IServiceManager.descriptor);
                data.writeString(name);
                data.writeStrongBinder(service);
                data.writeInt(allowIsolated ? 1 : 0);
                //～～～
                mRemote.transact(ADD_SERVICE_TRANSACTION, data, reply, 0);
                === BinderProxy.transact(ADD_SERVICE_TRANSACTION, data, reply, 0);

                reply.recycle();
                data.recycle();
            }
        }
         */

        // BinderProxy.transact(ADD_SERVICE_TRANSACTION, data, reply, 0);
        // http://androidxref.com/6.0.1_r10/xref/frameworks/base/core/java/android/os/Binder.java
        // 内含 BinderProxy 类
        /*
        final class BinderProxy implements IBinder {
            final private WeakReference mSelf;
            private long mObject;
            private long mOrgue;

            BinderProxy() {
                mSelf = new WeakReference(this);
            }
            public IInterface queryLocalInterface(String descriptor) {
                return null;
            }

            public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                return transactNative(code, data, reply, flags);
            }

            public native boolean transactNative(int code, Parcel data, Parcel reply,int flags) throws RemoteException;
            === android_util_Binder.android_os_BinderProxy_transact(env,obj,ADD_SERVICE_TRANSACTION,data,reply,0)
        }
         */

        // android_os_BinderProxy_transact(env,obj,ADD_SERVICE_TRANSACTION,data,reply,0)
        // http://androidxref.com/6.0.1_r10/xref/frameworks/base/core/jni/android_util_Binder.cpp
        /*
        static jboolean android_os_BinderProxy_transact(JNIEnv* env, jobject obj,
                    jint code,          // ADD_SERVICE_TRANSACTION
                    jobject dataObj,    // Parcel data
                    jobject replyObj,   // Parcel reply
                    jint flags          // 0
                    ){

            //java Parcel转为native Parcel
            Parcel* data = parcelForJavaObject(env, dataObj);
            Parcel* reply = parcelForJavaObject(env, replyObj);

            //gBinderProxyOffsets.mObject 中保存的是new BpBinder(0)对象
            IBinder* target = (IBinder*) env->GetLongField(obj, gBinderProxyOffsets.mObject);

            //此处便是BpBinder::transact(), 经过native层，进入Binder驱动程序
            status_t err = target->transact(code, *data, reply, flags);
            === BpBinder.transact(ADD_SERVICE_TRANSACTION, *data, reply, flags);

            return JNI_FALSE;
        }
         */

        // BpBinder.transact(ADD_SERVICE_TRANSACTION, *data, reply, 0);
        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/BpBinder.cpp
        /*
        BpBinder::BpBinder(int32_t handle)
            : mHandle(handle)
            , mAlive(1)
            , mObitsSent(0)
            , mObituaries(NULL)    {
            IPCThreadState::self()->incWeakHandle(handle);
        }

        status_t BpBinder::transact(
                    uint32_t code,          // ADD_SERVICE_TRANSACTION
                    const Parcel& data,
                    Parcel* reply,
                    uint32_t flags          // 0
        ){

            if (mAlive) {
                status_t status = IPCThreadState::self()->transact(mHandle, code, data, reply, flags);
                === IPCThreadState::transact(0, ADD_SERVICE_TRANSACTION, data, reply, flags);

                if (status == DEAD_OBJECT) mAlive = 0;
                    return status;
            }

            return DEAD_OBJECT;
        }
         */

        // IPCThreadState::transact(0, ADD_SERVICE_TRANSACTION, data, reply, 0);
        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/IPCThreadState.cpp
        /*
        status_t IPCThreadState::transact(int32_t handle,
                            uint32_t code,      // ADD_SERVICE_TRANSACTION
                            const Parcel& data,
                            Parcel* reply,
                            uint32_t flags      // 0
        ){
            status_t err = data.errorCheck();
            if (err == NO_ERROR) {
                // 传输数据
                err = writeTransactionData(BC_TRANSACTION, flags, handle, code, data, NULL);
            }

            if ((flags & TF_ONE_WAY) == 0) {
                if (reply) {
                    // 等待响应
                    err = waitForResponse(reply);
                } else {
                    Parcel fakeReply;
                    err = waitForResponse(&fakeReply);
                }
            } else {
                err = waitForResponse(NULL, NULL);
            }
            return err;
        }
        */

        // writeTransactionData(BC_TRANSACTION, 0, 0, ADD_SERVICE_TRANSACTION, data, NULL);
        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/IPCThreadState.cpp
        // 传输数据，将数据写入 mOut
        /*
        IPCThreadState::IPCThreadState()
                        : mProcess(ProcessState::self()),
                        mMyThreadId(gettid()),
                         mStrictModePolicy(0),
                        mLastTransactionBinderFlags(0)
        {
            pthread_setspecific(gTLS, this);
            clearCaller();

            mIn.setDataCapacity(256); // 用来接收来自Binder设备的数据，默认大小为256字节；
            mOut.setDataCapacity(256);// 用来存储发往Binder设备的数据，默认大小为256字节。
        }

        status_t IPCThreadState::writeTransactionData(
                            int32_t cmd,                // BC_TRANSACTION
                            uint32_t binderFlags,       // 0
                            int32_t handle,             // 0
                            uint32_t code,              // ADD_SERVICE_TRANSACTION
                            const Parcel& data,         // data
                            status_t* statusBuffer)     // null
        {
            binder_transaction_data tr;
            tr.target.ptr = 0;
            tr.target.handle = handle;  // 0
            tr.code = code;             // ADD_SERVICE_TRANSACTION
            tr.flags = binderFlags;     // 0
            tr.cookie = 0;
            tr.sender_pid = 0;
            tr.sender_euid = 0;

            const status_t err = data.errorCheck();
            if (err == NO_ERROR) {
                tr.data_size = data.ipcDataSize();      // mDataSize
                tr.data.ptr.buffer = data.ipcData();    // mData
                tr.offsets_size = data.ipcObjectsCount()*sizeof(binder_size_t);
                tr.data.ptr.offsets = data.ipcObjects();
            } else if (statusBuffer) {
            } else {
                return (mLastError = err);
            }

            mOut.writeInt32(cmd);           // 写入 BC_TRANSACTION
            mOut.write(&tr, sizeof(tr));    // 写入 binder_transaction_data 数据
            return NO_ERROR;
        }
         */

        // waitForResponse(reply)
        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/IPCThreadState.cpp
        // 等待响应，talkWithDriver，executeCommand
        /*
        status_t IPCThreadState::waitForResponse(Parcel *reply, status_t *acquireResult)
        {
            uint32_t cmd;
            int32_t err;

            while (1) {
                // 与binder驱动交互
                if ((err=talkWithDriver()) < NO_ERROR) break;
                ...
                if (mIn.dataAvail() == 0) continue;

                cmd = (uint32_t)mIn.readInt32();
                switch (cmd) {
                    case BR_TRANSACTION_COMPLETE: ...
                    case BR_DEAD_REPLY: ...
                    case BR_FAILED_REPLY: ...
                    case BR_ACQUIRE_RESULT: ...
                    case BR_REPLY: ...
                        goto finish;

                    default:
                        err = executeCommand(cmd);
                        if (err != NO_ERROR) goto finish;
                        break;
                }
            }
            return err;
        }
         */

        // talkWithDriver
        /*
        status_t IPCThreadState::talkWithDriver(bool doReceive)
        {
            ...
            binder_write_read bwr;
            const bool needRead = mIn.dataPosition() >= mIn.dataSize();
            const size_t outAvail = (!doReceive || needRead) ? mOut.dataSize() : 0;

            bwr.write_size = outAvail;
            bwr.write_buffer = (uintptr_t)mOut.data(); // 将数据放入bwr

            if (doReceive && needRead) {
                //接收数据缓冲区信息的填充。如果以后收到数据，就直接填在mIn中了。
                bwr.read_size = mIn.dataCapacity();
                bwr.read_buffer = (uintptr_t)mIn.data();
            } else {
                bwr.read_size = 0;
                bwr.read_buffer = 0;
            }
            //当读缓冲和写缓冲都为空，则直接返回
            if ((bwr.write_size == 0) && (bwr.read_size == 0)) return NO_ERROR;

            bwr.write_consumed = 0;
            bwr.read_consumed = 0;
            status_t err;
            do {
                //通过ioctl不停的读写操作，跟Binder Driver进行通信
                if (ioctl(mProcess->mDriverFD, BINDER_WRITE_READ, &bwr) >= 0)
                    err = NO_ERROR;
                ...
            } while (err == -EINTR); //当被中断，则继续执行
            ...
            return err;
        }
         */

        // 通过给
        // service_manager.c#do_add_service
        /*
        int do_add_service(struct binder_state *bs,
                            const uint16_t *s,
                            size_t len,
                            uint32_t handle,
                            uid_t uid,
                            int allow_isolated,
                            pid_t spid){

            struct svcinfo *si;
200
201    //ALOGI("add_service('%s',%x,%s) uid=%d\n", str8(s, len), handle,
202    //        allow_isolated ? "allow_isolated" : "!allow_isolated", uid);
203
204    if (!handle || (len == 0) || (len > 127))
205        return -1;
206
207    if (!svc_can_register(s, len, spid)) {
208        ALOGE("add_service('%s',%x) uid=%d - PERMISSION DENIED\n",
209             str8(s, len), handle, uid);
210        return -1;
211    }
212
213    si = find_svc(s, len);
214    if (si) {
215        if (si->handle) {
216            ALOGE("add_service('%s',%x) uid=%d - ALREADY REGISTERED, OVERRIDE\n",
217                 str8(s, len), handle, uid);
218            svcinfo_death(bs, si);
219        }
220        si->handle = handle;
221    } else {
222        si = malloc(sizeof(*si) + (len + 1) * sizeof(uint16_t));
223        if (!si) {
224            ALOGE("add_service('%s',%x) uid=%d - OUT OF MEMORY\n",
225                 str8(s, len), handle, uid);
226            return -1;
227        }
228        si->handle = handle;
229        si->len = len;
230        memcpy(si->name, s, (len + 1) * sizeof(uint16_t));
231        si->name[len] = '\0';
232        si->death.func = (void*) svcinfo_death;
233        si->death.ptr = si;
234        si->allow_isolated = allow_isolated;
235        si->next = svclist;
236        svclist = si;
237    }
238
239    binder_acquire(bs, handle);
240    binder_link_to_death(bs, handle, &si->death);
241    return 0;
242}
         */

    }

    private void getIServiceManager() {
        /*

        /0/ xref: /frameworks/base/core/java/android/os/ServiceManager.java
        private static IServiceManager sServiceManager;

        /2/ xref: /frameworks/base/core/java/android/os/ServiceManager.java#getIServiceManager
        private static IServiceManager getIServiceManager() {
            if (sServiceManager != null) {
                return sServiceManager;
            }

            sServiceManager = ServiceManagerNative.asInterface(BinderInternal.getContextObject());
            等价于==
            sServiceManager = new ServiceManagerProxy(new BinderProxy())
            return sServiceManager;
        }

        /2.1/ xref: /frameworks/base/core/java/com/android/internal/os/BinderInternal.java#getContextObject
        public static final native IBinder getContextObject(); // JNI方法

        /2.1/ xref: /frameworks/base/core/jni/android_util_Binder.cpp#getContextObject
        static jobject android_os_BinderInternal_getContextObject(JNIEnv* env, jobject clazz){
            sp<IBinder> b = ProcessState::self()->getContextObject(NULL);
            return javaObjectForIBinder(env, b);
        }
         */
    }
}
