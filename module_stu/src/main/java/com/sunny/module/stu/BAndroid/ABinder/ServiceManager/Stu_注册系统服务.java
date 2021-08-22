package com.sunny.module.stu.BAndroid.ABinder.ServiceManager;

import com.sunny.module.stu.base.StuImpl;


/**
 * http://gityuan.com/2015/11/14/binder-add-service/
 */
public class Stu_注册系统服务 extends StuImpl {

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
         */

        // 注册 MediaPlayerService
        // http://androidxref.com/6.0.1_r10/xref/frameworks/av/media/libmediaplayerservice/MediaPlayerService.cpp
        // MediaPlayerService::instantiate();
        /*
        void MediaPlayerService::instantiate() {
            defaultServiceManager()->addService(String16("media.player"), new MediaPlayerService());
        }

        defaultServiceManager()返回的是 BpServiceManager 对象，用于跟servicemanager进程通信;
         */

        // BpServiceManager#addService(String16("media.player"), new MediaPlayerService())
        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/IServiceManager.cpp
        // 内部类 BpServiceManager
        // remote()->transact(ADD_SERVICE_TRANSACTION, data, &reply);
        /*
        virtual status_t addService(
                            const String16& name,           // = media.player
                            const sp<IBinder>& service,     // = MediaPlayerService 对象
                             bool allowIsolated)            // = false
        {
            Parcel data, reply; //Parcel是数据通信包

            //写入头信息"android.os.IServiceManager"
            data.writeInterfaceToken(IServiceManager::getInterfaceDescriptor());

            data.writeString16(name);                   // name为 "media.player"

            // 写入 service 信息，将 service 对象转换成 flat_binder_object 结构，然后写入 data.out 里
            data.writeStrongBinder(service);            // MediaPlayerService对象
            data.writeInt32(allowIsolated ? 1 : 0);     // allowIsolated= false

            //
            status_t err = remote()->transact(ADD_SERVICE_TRANSACTION, data, &reply);
            return err == NO_ERROR ? reply.readExceptionCode() : err;
        }

        remote() == mRemote
         */

        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/include/binder/Binder.h
        // remote() == mRemote
        // mRemote->transact(ADD_SERVICE_TRANSACTION, data, &reply);
        /*
        class BpRefBase : public virtual RefBase
        {
        protected:
            BpRefBase(const sp<IBinder>& o);
            virtual                 ~BpRefBase();
            virtual void            onFirstRef();
            virtual void            onLastStrongRef(const void* id);
            virtual bool            onIncStrongAttempted(uint32_t flags, const void* id);

            inline  IBinder*        remote()                { return mRemote; }
            inline  IBinder*        remote() const          { return mRemote; }

        private:
                                BpRefBase(const BpRefBase& o);
            BpRefBase&          operator=(const BpRefBase& o);
            IBinder* const          mRemote;
            RefBase::weakref_type*  mRefs;
            volatile int32_t        mState;
        };

         */

        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/Binder.cpp
        // mRemote == o.get() == BpBinder
        // BpBinder->transact(ADD_SERVICE_TRANSACTION, data, &reply);
        /*
        IBinder::IBinder()
                : RefBase()
        {
        }

        BpRefBase::BpRefBase(const sp<IBinder>& o)
            : mRemote(o.get()), mRefs(NULL), mState(0)
        {
            extendObjectLifetime(OBJECT_LIFETIME_WEAK);
            if (mRemote) {
                mRemote->incStrong(this);           // Removed on first IncStrong().
                mRefs = mRemote->createWeak(this);  // Held for our entire lifetime.
            }
        }
         */


        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/BpBinder.cpp
        // IPCThreadState::transact(0, ADD_SERVICE_TRANSACTION, data, reply, 0);
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
                if (status == DEAD_OBJECT) mAlive = 0;
                    return status;
            }

            return DEAD_OBJECT;
        }
         */

        // http://androidxref.com/6.0.1_r10/xref/frameworks/native/libs/binder/IPCThreadState.cpp
        // writeTransactionData(BC_TRANSACTION, flags, handle, code, data, NULL);
        // waitForResponse(reply);
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
        // ioctl(mProcess->mDriverFD, BINDER_WRITE_READ, &bwr)
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

        // ioctl -> binder_ioctl -> binder_ioctl_write_read

        /*
        ioctl(mProcess->mDriverFD, BINDER_WRITE_READ, &bwr)
        ===> binder_ioctl(mProcess->mDriverFD, BINDER_WRITE_READ, &bwr)
            ===> binder_ioctl_write_read(filp, cmd, arg, thread)
         */
        // binder_ioctl_write_read
        /*
        static int binder_ioctl_write_read(
                        struct file *filp,
                        unsigned int cmd,
                         unsigned long arg,
                        struct binder_thread *thread)
        {
            struct binder_proc *proc = filp->private_data;
            void __user *ubuf = (void __user *)arg;
            struct binder_write_read bwr;

            //将用户空间bwr结构体拷贝到内核空间
            copy_from_user(&bwr, ubuf, sizeof(bwr));
            ...

            if (bwr.write_size > 0) {
                //将数据放入目标进程【见小节4.2】
                ret = binder_thread_write(proc, thread,
                              bwr.write_buffer,
                              bwr.write_size,
                              &bwr.write_consumed);
                ...
            }
            if (bwr.read_size > 0) {
                //读取自己队列的数据 【见小节】
                ret = binder_thread_read(proc, thread, bwr.read_buffer,
                     bwr.read_size,
                     &bwr.read_consumed,
                     filp->f_flags & O_NONBLOCK);
                if (!list_empty(&proc->todo))
                    wake_up_interruptible(&proc->wait);
                ...
            }

            //将内核空间bwr结构体拷贝到用户空间
            copy_to_user(ubuf, &bwr, sizeof(bwr));
            ...
        }
         */

        //
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
}
