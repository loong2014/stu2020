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
        // copy_from_user   // 用户空间数据copy到内核空间
        // copy_to_user     // 内核空间数据copy到用户空间
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

        // binder_thread_write
        // binder_transaction(proc, thread, &tr, cmd == BC_REPLY)
        /*
        struct binder_transaction_data tr
        copy_from_user(&tr, ptr, sizeof(tr))
        binder_transaction(proc, thread, &tr, cmd == BC_REPLY)

        static int binder_thread_write(struct binder_proc *proc,
                        struct binder_thread *thread,       // thread
                        binder_uintptr_t binder_buffer,     // bwr.write_buffer
                        size_t size,                        // bwr.write_size
                        binder_size_t *consumed)            // &bwr.write_consumed
        {
            uint32_t cmd;
            void __user *buffer = (void __user *)(uintptr_t)binder_buffer;
            void __user *ptr = buffer + *consumed;
            void __user *end = buffer + size;
            while (ptr < end && thread->return_error == BR_OK) {
                //拷贝用户空间的cmd命令，此时为BC_TRANSACTION
                if (get_user(cmd, (uint32_t __user *)ptr)) -EFAULT;
                ptr += sizeof(uint32_t);
                switch (cmd) {
                case BC_TRANSACTION:
                case BC_REPLY: {
                    struct binder_transaction_data tr;
                    //拷贝用户空间的binder_transaction_data
                    if (copy_from_user(&tr, ptr, sizeof(tr)))   return -EFAULT;
                    ptr += sizeof(tr);
                    // 见小节4.3】
                    binder_transaction(proc, thread, &tr, cmd == BC_REPLY);
                    break;
                }
                ...
            }
            *consumed = ptr - buffer;
          }
          return 0;
        }
         */

        // binder_transaction(proc, thread, &tr, cmd == BC_REPLY)
        /*
        struct binder_transaction *t;
        struct binder_work *tcomplete;

        static void binder_transaction(struct binder_proc *proc,
                       struct binder_thread *thread,
                       struct binder_transaction_data *tr, int reply){
            struct binder_transaction *t;
            struct binder_work *tcomplete;
            ...

            if (reply) {
                ...
            }else {
                if (tr->target.handle) {
                    ...
                } else {
                    // handle=0则找到servicemanager实体
                    target_node = binder_context_mgr_node;
                }
                //target_proc为servicemanager进程
                target_proc = target_node->proc;
            }

            if (target_thread) {
                ...
            } else {
                //找到servicemanager进程的todo队列
                target_list = &target_proc->todo;
                target_wait = &target_proc->wait;
            }

            t = kzalloc(sizeof(*t), GFP_KERNEL);
            tcomplete = kzalloc(sizeof(*tcomplete), GFP_KERNEL);

            //非oneway的通信方式，把当前thread保存到transaction的from字段
            if (!reply && !(tr->flags & TF_ONE_WAY))
                t->from = thread;
            else
                t->from = NULL;

            t->sender_euid = task_euid(proc->tsk);
            t->to_proc = target_proc; //此次通信目标进程为servicemanager进程
            t->to_thread = target_thread;
            t->code = tr->code;  //此次通信code = ADD_SERVICE_TRANSACTION
            t->flags = tr->flags;  // 此次通信flags = 0
            t->priority = task_nice(current);

            //从servicemanager进程中分配buffer
            t->buffer = binder_alloc_buf(target_proc, tr->data_size,
                tr->offsets_size, !reply && (t->flags & TF_ONE_WAY));

            t->buffer->allow_user_free = 0;
            t->buffer->transaction = t;
            t->buffer->target_node = target_node;

            if (target_node)
                binder_inc_node(target_node, 1, 0, NULL); //引用计数加1
            offp = (binder_size_t *)(t->buffer->data + ALIGN(tr->data_size, sizeof(void *)));

            //分别拷贝用户空间的binder_transaction_data中ptr.buffer和ptr.offsets到内核
            copy_from_user(t->buffer->data,
                (const void __user *)(uintptr_t)tr->data.ptr.buffer, tr->data_size);
            copy_from_user(offp,
                (const void __user *)(uintptr_t)tr->data.ptr.offsets, tr->offsets_size);

            off_end = (void *)offp + tr->offsets_size;

            for (; offp < off_end; offp++) {
                struct flat_binder_object *fp;
                fp = (struct flat_binder_object *)(t->buffer->data + *offp);
                off_min = *offp + sizeof(struct flat_binder_object);
                switch (fp->type) {
                    case BINDER_TYPE_BINDER:
                    case BINDER_TYPE_WEAK_BINDER: {
                      struct binder_ref *ref;
                      //【见4.3.1】
                      struct binder_node *node = binder_get_node(proc, fp->binder);
                      if (node == NULL) {
                        //服务所在进程 创建binder_node实体【见4.3.2】
                        node = binder_new_node(proc, fp->binder, fp->cookie);
                        ...
                      }
                      //servicemanager进程binder_ref【见4.3.3】
                      ref = binder_get_ref_for_node(target_proc, node);
                      ...
                      //调整type为HANDLE类型
                      if (fp->type == BINDER_TYPE_BINDER)
                        fp->type = BINDER_TYPE_HANDLE;
                      else
                        fp->type = BINDER_TYPE_WEAK_HANDLE;
                      fp->binder = 0;
                      fp->handle = ref->desc; //设置handle值
                      fp->cookie = 0;
                      binder_inc_ref(ref, fp->type == BINDER_TYPE_HANDLE,
                               &thread->todo);
                    } break;
                    case :...
            }

            if (reply) {
                ..
            } else if (!(t->flags & TF_ONE_WAY)) {
                //BC_TRANSACTION 且 非oneway,则设置事务栈信息
                t->need_reply = 1;
                t->from_parent = thread->transaction_stack;
                thread->transaction_stack = t;
            } else {
                ...
            }

            //将BINDER_WORK_TRANSACTION添加到目标队列，本次通信的目标队列为target_proc->todo
            t->work.type = BINDER_WORK_TRANSACTION;
            list_add_tail(&t->work.entry, target_list);

            //将BINDER_WORK_TRANSACTION_COMPLETE添加到当前线程的todo队列
            tcomplete->type = BINDER_WORK_TRANSACTION_COMPLETE;
            list_add_tail(&tcomplete->entry, &thread->todo);

            //唤醒等待队列，本次通信的目标队列为target_proc->wait
            if (target_wait)
                wake_up_interruptible(target_wait);
            return;
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
        {
            struct svcinfo *si;

            if (!handle || (len == 0) || (len > 127))
                return -1;

            //权限检查
            if (!svc_can_register(s, len, spid)) {
                return -1;
            }

            //服务检索
            si = find_svc(s, len);
            if (si) {
                if (si->handle) {
                    svcinfo_death(bs, si); //服务已注册时，释放相应的服务
                }
                si->handle = handle;
            } else {
                si = malloc(sizeof(*si) + (len + 1) * sizeof(uint16_t));
                if (!si) {  //内存不足，无法分配足够内存
                    return -1;
                }
                si->handle = handle;
                si->len = len;
                memcpy(si->name, s, (len + 1) * sizeof(uint16_t)); //内存拷贝服务信息
                si->name[len] = '\0';
                si->death.func = (void*) svcinfo_death;
                si->death.ptr = si;
                si->allow_isolated = allow_isolated;
                si->next = svclist; // svclist保存所有已注册的服务
                svclist = si;
            }

            //以BC_ACQUIRE命令，handle为目标的信息，通过ioctl发送给binder驱动
            binder_acquire(bs, handle);
            //以BC_REQUEST_DEATH_NOTIFICATION命令的信息，通过ioctl发送给binder驱动，主要用于清理内存等收尾工作。
            binder_link_to_death(bs, handle, &si->death);
            return 0;
        }
         */

    }
}
