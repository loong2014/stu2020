package com.sunny.module.stu.BAndroid.ABinder;


import com.sunny.module.stu.base.StuImpl;

public class EProcessState extends StuImpl {


    /**
     * https://wenku.baidu.com/view/93966a34ee06eff9aef80756.html
     * 负责打开 binder 驱动，让进程的程序能与 binder 进行沟通
     */
    @Override
    public void 是什么() {

    }

    /**
     * https://blog.csdn.net/u010758403/article/details/17393883
     *
     * 从Zygote创建进程开始，到ProcessState.self
     * https://blog.csdn.net/u012439416/article/details/73135601
     */
    /*
class ProcessState : public virtual RefBase
{
public:
    static  sp<ProcessState>    self();

            void                setContextObject(const sp<IBinder>& object);
            sp<IBinder>         getContextObject(const sp<IBinder>& caller);

            void                setContextObject(const sp<IBinder>& object,
                                                 const String16& name);
            sp<IBinder>         getContextObject(const String16& name,
                                                 const sp<IBinder>& caller);

            void                startThreadPool();

    typedef bool (*context_check_func)(const String16& name,
                                       const sp<IBinder>& caller,
                                       void* userData);

            bool                isContextManager(void) const;
            bool                becomeContextManager(
                                    context_check_func checkFunc,
                                    void* userData);

            sp<IBinder>         getStrongProxyForHandle(int32_t handle);
            wp<IBinder>         getWeakProxyForHandle(int32_t handle);
            void                expungeHandle(int32_t handle, IBinder* binder);

            void                setArgs(int argc, const char* const argv[]);
            int                 getArgC() const;
            const char* const*  getArgV() const;

            void                setArgV0(const char* txt);

            void                spawnPooledThread(bool isMain);

private:
    // 朋友类，友元类也就是说 IPCThreadState 可以访问 ProcessState 的私有成员，方法
    friend class IPCThreadState;

                                ProcessState();
                                ~ProcessState();

                                ProcessState(const ProcessState& o);
            ProcessState&       operator=(const ProcessState& o);

            struct handle_entry {
                IBinder* binder;
                RefBase::weakref_type* refs;
            };

            handle_entry*       lookupHandleLocked(int32_t handle);

            int                 mDriverFD;
            void*               mVMStart;

    mutable Mutex               mLock;  // protects everything below.

            // 记录本进程中所有BpBinder的向量表,在Binder架构中，应用进程是通过“binder句柄”来找到
            Vector<handle_entry>mHandleToObject;

            bool                mManagesContexts;
            context_check_func  mBinderContextCheckFunc;
            void*               mBinderContextUserData;

            KeyedVector<String16, sp<IBinder> >
                                mContexts;


            String8             mRootDir;
            bool                mThreadPoolStarted;
    volatile int32_t            mThreadPoolSeq;
};
     */
    @Override
    public void 数据结构() {
    /*
// 进程中唯一
class ProcessState : public virtual RefBase
{
public:
            //【1】
    static  sp<ProcessState>    self();

            //【2】 方法内调用 【3】，每个进程只有一个binder线程池
            void                startThreadPool();

            //【3】
            void                spawnPooledThread(bool isMain);

            //【4】 返回一个BpBinder对象
            sp<IBinder>         getContextObject(const sp<IBinder>& caller);

private:
    // 朋友类，友元类也就是说 IPCThreadState 可以访问 ProcessState 的私有成员，方法
    friend class IPCThreadState;

            struct handle_entry {
                IBinder* binder;
                RefBase::weakref_type* refs;
            };

            // 它是本进程中记录所有BpBinder的向量表，非常重要,BpBinder是代理端的核心。
            Vector<handle_entry>mHandleToObject;

};
     */
    }


    /**
     * 用于返回单实例的 ProcessState
     */
    private void fun_public_static_self() {
        /*
sp<ProcessState> ProcessState::self()
{
// gProcess是全局变量， 定义在binder/Static.cpp中：
    if (gProcess != NULL) return gProcess;

    AutoMutex _l(gProcessMutex);
    if (gProcess == NULL) gProcess = new ProcessState;
    return gProcess;
}
         */
    }

    /**
     * 构造函数
     * 首先调用 open_driver 方法打开/dev/binder驱动设备，再利用 mmap 映射内核的地址空间，
     *      将Binder驱动的fd赋值ProcessState对象中的变量【mDriverFD】，用于交互操作。
     */
    private void _ProcessState() {

        /*
ProcessState::ProcessState()
    : mDriverFD(open_driver())
    , mVMStart(MAP_FAILED)
    , mThreadCountLock(PTHREAD_MUTEX_INITIALIZER)
    , mThreadCountDecrement(PTHREAD_COND_INITIALIZER)
    , mExecutingThreadsCount(0)
    , mMaxThreads(DEFAULT_MAX_BINDER_THREADS)
    , mManagesContexts(false)
    , mBinderContextCheckFunc(NULL)
    , mBinderContextUserData(NULL)
    , mThreadPoolStarted(false)
    , mThreadPoolSeq(1)
{
    if (mDriverFD >= 0) {
        // XXX Ideally, there should be a specific define for whether we
        // have mmap (or whether we could possibly have the kernel module
        // availabla).
#if !defined(HAVE_WIN32_IPC)
        // mmap the binder, providing a chunk of virtual address space to receive transactions.
        mVMStart = mmap(0, BINDER_VM_SIZE, PROT_READ, MAP_PRIVATE | MAP_NORESERVE, mDriverFD, 0);
        if (mVMStart == MAP_FAILED) {
            // *sigh*
            ALOGE("Using /dev/binder failed: unable to mmap transaction memory.\n");
            close(mDriverFD);
            mDriverFD = -1;
        }
#else
        mDriverFD = -1;
#endif
    }

    LOG_ALWAYS_FATAL_IF(mDriverFD < 0, "Binder driver could not be opened.  Terminating.");
}
         */
    }

    private void _open_driver(){
        /*
static int open_driver()
{
    int fd = open("/dev/binder", O_RDWR);//打开binder驱动
    if (fd >= 0) {
        fcntl(fd, F_SETFD, FD_CLOEXEC);
        int vers = 0;
        status_t result = ioctl(fd, BINDER_VERSION, &vers);//检查版本号
        if (result == -1) {
            ALOGE("Binder ioctl to obtain version failed: %s", strerror(errno));
            close(fd);
            fd = -1;
        }
        if (result != 0 || vers != BINDER_CURRENT_PROTOCOL_VERSION) {
            ALOGE("Binder driver protocol does not match user space protocol!");
            close(fd);
            fd = -1;
        }
        size_t maxThreads = DEFAULT_MAX_BINDER_THREADS;
        result = ioctl(fd, BINDER_SET_MAX_THREADS, &maxThreads);//设置最大线程数,该数值为15
        if (result == -1) {
            ALOGE("Binder ioctl to set max threads failed: %s", strerror(errno));
        }
    } else {
        ALOGW("Opening '/dev/binder' failed: %s\n", strerror(errno));
    }
    return fd;
}
         */
    }

    private void _startThreadPool(){
        /*
void ProcessState::startThreadPool()
{
    AutoMutex _l(mLock);
    if (!mThreadPoolStarted) {
        mThreadPoolStarted = true;
        spawnPooledThread(true);
    }
}
         */
    }

    private void _spawnPooledThread(){
        /*
void ProcessState::spawnPooledThread(bool isMain)
{
    if (mThreadPoolStarted) {
        String8 name = makeBinderThreadName();
        ALOGV("Spawning new pooled thread, name=%s\n", name.string());
        sp<Thread> t = new PoolThread(isMain);
        t->run(name.string());
    }
}
         */
    }
}
