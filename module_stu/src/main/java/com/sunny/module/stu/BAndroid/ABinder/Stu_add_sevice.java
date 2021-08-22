package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;


/**
 * http://gityuan.com/2015/11/14/binder-add-service/
 */
public class Stu_add_sevice extends StuImpl {

    @Override
    public void p_流程() {

        // 注册多媒体服务
        // http://androidxref.com/6.0.1_r10/xref/frameworks/av/media/mediaserver/main_mediaserver.cpp
        // main_mediaserver::main();
        /*
        int main(int argc __unused, char** argv)
        {
            ...
            InitializeIcuOrDie();
            //获得ProcessState实例对象
            sp<ProcessState> proc(ProcessState::self());

            //获取BpServiceManager对象
            sp<IServiceManager> sm = defaultServiceManager();
            AudioFlinger::instantiate();

            //注册多媒体服务
            MediaPlayerService::instantiate();
            ResourceManagerService::instantiate();
            CameraService::instantiate();
            AudioPolicyService::instantiate();
            SoundTriggerHwService::instantiate();
            RadioService::instantiate();
            registerExtensions();

            //启动Binder线程池
            ProcessState::self()->startThreadPool();

            //当前线程加入到线程池
            IPCThreadState::self()->joinThreadPool();
         }


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

        // http://androidxref.com/6.0.1_r10/xref/frameworks/av/media/libmediaplayerservice/MediaPlayerService.cpp
        // MediaPlayerService::instantiate();
        /*
        void MediaPlayerService::instantiate() {
            defaultServiceManager()->addService(String16("media.player"), new MediaPlayerService());
        }

        defaultServiceManager()返回的是 BpServiceManager 对象，用于跟servicemanager进程通信;
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
