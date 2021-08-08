package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

public class EIPCThreadState extends StuImpl {

    @Override
    public void 是什么() {
//    在Android中，每个参与Binder通信的线程都会有一个IPCThreadState实例与之关联。

    }

    /**
     * 数据结构
     * https://blog.csdn.net/u010758403/article/details/17397417
     * <p>
     * 关键方法
     * https://blog.csdn.net/liuzhengzlh/article/details/108474280
     */
    /*
class IPCThreadState
{
public:
    static  IPCThreadState*     self(); // 获取单例对象
    static  IPCThreadState*     selfOrNull();  // self(), but won't instantiate

            sp<ProcessState>    process();

          。。。

            void                joinThreadPool(bool isMain = true);

            // Stop the local process.
            void                stopProcess(bool immediate = true);

            status_t            transact(int32_t handle,
                                         uint32_t code, const Parcel& data,
                                         Parcel* reply, uint32_t flags); // 传输数据

          。。。



private:
                                IPCThreadState();
                                ~IPCThreadState();

            status_t            sendReply(const Parcel& reply, uint32_t flags);
            status_t            waitForResponse(Parcel *reply,
                                                status_t *acquireResult=NULL);
            status_t            talkWithDriver(bool doReceive=true);
            status_t            writeTransactionData(int32_t cmd,
                                                     uint32_t binderFlags,
                                                     int32_t handle,
                                                     uint32_t code,
                                                     const Parcel& data,
                                                     status_t* statusBuffer);
            status_t            executeCommand(int32_t command);

            void                clearCaller();

    static  void                threadDestructor(void *st);
    static  void                freeBuffer(Parcel* parcel,
                                           const uint8_t* data, size_t dataSize,
                                           const size_t* objects, size_t objectsSize,
                                           void* cookie);

    const   sp<ProcessState>    mProcess;
    const   pid_t               mMyThreadId;
            Vector<BBinder*>    mPendingStrongDerefs;
            Vector<RefBase::weakref_type*> mPendingWeakDerefs;

            Parcel              mIn;
            Parcel              mOut;
            status_t            mLastError;
            pid_t               mCallingPid;
            uid_t               mCallingUid;
            int32_t             mStrictModePolicy;
            int32_t             mLastTransactionBinderFlags;
};

 */
    @Override
    public void 数据结构() {
        //关键方法
        /*
                                // 在TLS中创建 IPCThreadState 对象
    static  IPCThreadState*     self(); // 【1】获取单例对象

            void                joinThreadPool(bool isMain = true); //启动线程池

【2】传输数据，包含了【3】writeTransactionData 和【4】waitForResponse
            status_t            transact(int32_t handle,
                                         uint32_t code,
                                         const Parcel& data, //携带的数据
                                         Parcel* reply, // 需要返回的数据
                                          uint32_t flags);

            status_t            sendReply(const Parcel& reply, uint32_t flags); //回传数据

【4】调用【5】talkWithDriver 进行数据交互，通过mIn.readInt32，获取cmd操作
            status_t            waitForResponse(Parcel *reply,
                                                status_t *acquireResult=NULL); //等待回复

// binder_write_read bwr 数据结构
【5】通过【ioctl】完成 mOut数据的发送，mIn数据的接收
            status_t            talkWithDriver(bool doReceive=true); //调用ioctl(BINDER_WRITE_READ)完成真正的数据接发。

【3】 完成数据从【用户空间】到【内核空间】的复制 ～～～ 最终数据在 IPCThreadState::【mOut】 中
                                // 将 data 数据转换成 binder_transaction_data
            status_t            writeTransactionData(int32_t cmd,
                                                     uint32_t binderFlags,
                                                     int32_t handle,
                                                     uint32_t code,
                                                     const Parcel& data,
                                                     status_t* statusBuffer);

            status_t            executeCommand(int32_t command);

            void                clearCaller();

    static  void                threadDestructor(void *st);
    static  void                freeBuffer(Parcel* parcel,
                                           const uint8_t* data, size_t dataSize,
                                           const size_t* objects, size_t objectsSize,
                                           void* cookie);

    const   sp<ProcessState>    mProcess;
    const   pid_t               mMyThreadId;
            Vector<BBinder*>    mPendingStrongDerefs;
            Vector<RefBase::weakref_type*> mPendingWeakDerefs;

            Parcel              mIn;
            Parcel              mOut;
            status_t            mLastError;
            pid_t               mCallingPid;
            uid_t               mCallingUid;
            int32_t             mStrictModePolicy;
            int32_t             mLastTransactionBinderFlags;
};

 */

    }


    /**
     * https://blog.csdn.net/liuzhengzlh/article/details/108474280
     */
    @Override
    public void 功能() {
        /*
        通过IPCThreadState::transact来完成的数据传输工作，其工作可以分为两步：
        1. 发送数据
            writeTransactionData只是将数据转换成binder_transaction_data结构并重新写入到IPCThreadState::mOut中。


        2. 接收数据


         */

    }


    /**
     * 用于返回单实例的IPCThreadState。
     */
    private void fun_public_static_self() {
        /*
IPCThreadState* IPCThreadState::self()
{
    if (gHaveTLS) {
restart:
        const pthread_key_t k = gTLS;
        IPCThreadState* st = (IPCThreadState*)pthread_getspecific(k);
        if (st) return st;
        return new IPCThreadState;
    }

    if (gShutdown) return NULL;

    pthread_mutex_lock(&gTLSMutex);
    if (!gHaveTLS) {
        if (pthread_key_create(&gTLS, threadDestructor) != 0) {
            pthread_mutex_unlock(&gTLSMutex);
            return NULL;
        }
        gHaveTLS = true;
    }
    pthread_mutex_unlock(&gTLSMutex);
    goto restart;
}

         */
    }

    /**
     * 构造函数
     * IPCThreadState的构造函数也是私有的，也只能通过IPCThreadState::self()来初始化类的实例。
     */
    private void IPCThreadState() {
        /*
IPCThreadState::IPCThreadState()
    : mProcess(ProcessState::self()),
      mMyThreadId(androidGetTid()),
      mStrictModePolicy(0),
      mLastTransactionBinderFlags(0)
{
    pthread_setspecific(gTLS, this);
    clearCaller();
    mIn.setDataCapacity(256);
    mOut.setDataCapacity(256);
}
         */
    }

    private void _joinThreadPool() {
        /*
void IPCThreadState::joinThreadPool(bool isMain)
{
    LOG_THREADPOOL("**** THREAD %p (PID %d) IS JOINING THE THREAD POOL\n", (void*)pthread_self(), getpid());

    mOut.writeInt32(isMain ? BC_ENTER_LOOPER : BC_REGISTER_LOOPER); //创建Binder线程

    // This thread may have been spawned by a thread that was in the background
    // scheduling group, so first we will make sure it is in the foreground
    // one to avoid performing an initial transaction in the background.
    set_sched_policy(mMyThreadId, SP_FOREGROUND);

    status_t result;
    do {
        processPendingDerefs();
        // now get the next command to be processed, waiting if necessary
        result = getAndExecuteCommand();

        if (result < NO_ERROR && result != TIMED_OUT && result != -ECONNREFUSED && result != -EBADF) {
            ALOGE("getAndExecuteCommand(fd=%d) returned unexpected error %d, aborting",
                  mProcess->mDriverFD, result);
            abort();
        }

        // Let this thread exit the thread pool if it is no longer
        // needed and it is not the main process thread.
        if(result == TIMED_OUT && !isMain) {
            break;
        }
    } while (result != -ECONNREFUSED && result != -EBADF);

    LOG_THREADPOOL("**** THREAD %p (PID %d) IS LEAVING THE THREAD POOL err=%p\n",
        (void*)pthread_self(), getpid(), (void*)result);

    mOut.writeInt32(BC_EXIT_LOOPER);
    talkWithDriver(false); //false代表bwr数据的read_buffer为空
}
         */
    }

}
