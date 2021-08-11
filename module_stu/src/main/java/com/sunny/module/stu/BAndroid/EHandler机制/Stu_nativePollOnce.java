package com.sunny.module.stu.BAndroid.EHandler机制;

public class Stu_nativePollOnce {

    // private native void nativePollOnce(long ptr, int timeoutMillis); /*non-static for callbacks*/
    /*
        ---> pollOnce
        --- ---> pollInner
        --- --- ---> epoll_wait
     */


    // xref: /frameworks/base/core/jni/android_os_MessageQueue.cpp
    // android_os_MessageQueue_nativePollOnce
    /*
188     static void android_os_MessageQueue_nativePollOnce(JNIEnv* env, jobject obj,
189             jlong ptr, jint timeoutMillis) {
190         NativeMessageQueue* nativeMessageQueue = reinterpret_cast<NativeMessageQueue*>(ptr);
            //***【1】***
191         nativeMessageQueue->pollOnce(env, obj, timeoutMillis);
192     }
     */

    // xref: /frameworks/base/core/jni/android_os_MessageQueue.cpp
    // pollOnce
    /*
107    void NativeMessageQueue::pollOnce(JNIEnv* env, jobject pollObj, int timeoutMillis) {
108    mPollEnv = env;
109    mPollObj = pollObj;
       //***【2】***
110    mLooper->pollOnce(timeoutMillis);
111    mPollObj = NULL;
112    mPollEnv = NULL;
113
114    if (mExceptionObj) {
115        env->Throw(mExceptionObj);
116        env->DeleteLocalRef(mExceptionObj);
117        mExceptionObj = NULL;
118    }
119}
     */

    // xref: /system/core/libutils/Looper.cpp
    // pollOnce
    /*
178int Looper::pollOnce(int timeoutMillis, int* outFd, int* outEvents, void** outData) {
179    int result = 0;
180    for (;;) {
181        while (mResponseIndex < mResponses.size()) {
182            const Response& response = mResponses.itemAt(mResponseIndex++);
183            int ident = response.request.ident;
184            if (ident >= 0) {
185                int fd = response.request.fd;
186                int events = response.events;
187                void* data = response.request.data;
193                if (outFd != NULL) *outFd = fd;
194                if (outEvents != NULL) *outEvents = events;
195                if (outData != NULL) *outData = data;
196                return ident;
197            }
198        }
199
200        if (result != 0) {
204            if (outFd != NULL) *outFd = 0;
205            if (outEvents != NULL) *outEvents = 0;
206            if (outData != NULL) *outData = NULL;
207            return result;
208        }
209
           //***【3】***
210        result = pollInner(timeoutMillis);
211    }
212}
     */

    // xref: /system/core/libutils/Looper.cpp
    // pollInner
    /*
214int Looper::pollInner(int timeoutMillis) {
219    // Adjust the timeout based on when the next message is due.
220    if (timeoutMillis != 0 && mNextMessageUptime != LLONG_MAX) {
221        nsecs_t now = systemTime(SYSTEM_TIME_MONOTONIC);
222        int messageTimeoutMillis = toMillisecondTimeoutDelay(now, mNextMessageUptime);
223        if (messageTimeoutMillis >= 0
224                && (timeoutMillis < 0 || messageTimeoutMillis < timeoutMillis)) {
225            timeoutMillis = messageTimeoutMillis;
226        }
231    }
232
233    // Poll.
234    int result = POLL_WAKE;
235    mResponses.clear();
236    mResponseIndex = 0;
237
238    // We are about to idle.
239    mPolling = true;
240
241    struct epoll_event eventItems[EPOLL_MAX_EVENTS];
       //***【4】***，阻塞当前线程，等待被唤醒
242    int eventCount = epoll_wait(mEpollFd, eventItems, EPOLL_MAX_EVENTS, timeoutMillis);
243
244    // No longer idling.
245    mPolling = false;
246
247    // Acquire lock.
248    mLock.lock();
249
250    // Rebuild epoll set if needed.
251    if (mEpollRebuildRequired) {
252        mEpollRebuildRequired = false;
253        rebuildEpollLocked();
254        goto Done;
255    }
256
257    // Check for poll error.
258    if (eventCount < 0) {
259        if (errno == EINTR) {
260            goto Done;
261        }
262        ALOGW("Poll failed with an unexpected error: %s", strerror(errno));
263        result = POLL_ERROR;
264        goto Done;
265    }
266
267    // Check for poll timeout.
268    if (eventCount == 0) {
272        result = POLL_TIMEOUT;
273        goto Done;
274    }
275
276    // Handle all events.
281    for (int i = 0; i < eventCount; i++) {
282        int fd = eventItems[i].data.fd;
283        uint32_t epollEvents = eventItems[i].events;
284        if (fd == mWakeEventFd) {
285            if (epollEvents & EPOLLIN) {
                   //***【5】***，唤醒
286                awoken();
287            } else {
288                ALOGW("Ignoring unexpected epoll events 0x%x on wake event fd.", epollEvents);
289            }
290        } else {
291            ssize_t requestIndex = mRequests.indexOfKey(fd);
292            if (requestIndex >= 0) {
293                int events = 0;
294                if (epollEvents & EPOLLIN) events |= EVENT_INPUT;
295                if (epollEvents & EPOLLOUT) events |= EVENT_OUTPUT;
296                if (epollEvents & EPOLLERR) events |= EVENT_ERROR;
297                if (epollEvents & EPOLLHUP) events |= EVENT_HANGUP;
298                pushResponse(events, mRequests.valueAt(requestIndex));
299            } else {
300                ALOGW("Ignoring unexpected epoll events 0x%x on fd %d that is "
301                        "no longer registered.", epollEvents, fd);
302            }
303        }
304    }
305Done: ;
306
307    // Invoke pending message callbacks.
308    mNextMessageUptime = LLONG_MAX;
309    while (mMessageEnvelopes.size() != 0) {
310        nsecs_t now = systemTime(SYSTEM_TIME_MONOTONIC);
311        const MessageEnvelope& messageEnvelope = mMessageEnvelopes.itemAt(0);
312        if (messageEnvelope.uptime <= now) {
313            // Remove the envelope from the list.
314            // We keep a strong reference to the handler until the call to handleMessage
315            // finishes.  Then we drop it so that the handler can be deleted *before*
316            // we reacquire our lock.
317            { // obtain handler
318                sp<MessageHandler> handler = messageEnvelope.handler;
319                Message message = messageEnvelope.message;
320                mMessageEnvelopes.removeAt(0);
321                mSendingMessage = true;
322                mLock.unlock();
323
328                handler->handleMessage(message);
329            } // release handler
330
331            mLock.lock();
332            mSendingMessage = false;
333            result = POLL_CALLBACK;
334        } else {
335            // The last message left at the head of the queue determines the next wakeup time.
336            mNextMessageUptime = messageEnvelope.uptime;
337            break;
338        }
339    }
340
341    // Release lock.
342    mLock.unlock();
343
344    // Invoke all response callbacks.
345    for (size_t i = 0; i < mResponses.size(); i++) {
346        Response& response = mResponses.editItemAt(i);
347        if (response.request.ident == POLL_CALLBACK) {
348            int fd = response.request.fd;
349            int events = response.events;
350            void* data = response.request.data;
355            // Invoke the callback.  Note that the file descriptor may be closed by
356            // the callback (and potentially even reused) before the function returns so
357            // we need to be a little careful when removing the file descriptor afterwards.
358            int callbackResult = response.request.callback->handleEvent(fd, events, data);
359            if (callbackResult == 0) {
360                removeFd(fd, response.request.seq);
361            }
362
363            // Clear the callback reference in the response structure promptly because we
364            // will not clear the response vector itself until the next poll.
365            response.request.callback.clear();
366            result = POLL_CALLBACK;
367        }
368    }
369    return result;
370}
     */
}
