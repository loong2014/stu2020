package com.sunny.module.stu.F类加载器ClassLoader.android;

import com.sunny.module.stu.base.StuImpl;

/*
https://blog.csdn.net/static_zh/article/details/100084370

源码目录：
http://androidxref.com/9.0.0_r3/xref/libcore/dalvik/src/main/java/dalvik/system/
 */
public class Stu_Android_ClassLoader extends StuImpl {
    @Override
    public void a_是什么() {

        // 类加载器
        /*
            系统是通过ClassLoader来将类加载到内存中，然后解析使用，
            在java中也有ClassLoader，但是因为java编译出来的是Class文件，而Android的APK中包含的确实dex文件，
            dex文件是将所需的所有Class文件重新打包，打包的规则不是简单地压缩，
            而是完全对Class文件内部的各种函数表、变量表等进行优化，并产生一个新的文件，
            所以java中和Android中的ClassLoader也不一样，这里主要来说一下Android中的ClassLoader。

            ClassLoader 是一个抽象类，定义了ClassLoader的主要功能

            BaseDexClassLoader 继承自 ClassLoader，是抽象类ClassLoader的具体实现类，
                PathClassLoader 和 DexClassLoader 都继承它。

            BootClassLoader 是 ClassLoader 的内部类
         */
    }

    @Override
    public void s_数据结构() {
        // super(dexPath, new File(optimizedDirectory), libraryPath, parent);
        /*
            参一 dexpath：要加载的dex文件的路径

            参二 optimizedDirectory：dex文件首次加载时会进行优化操作，这个参数即为优化后的odex文件的存放目录，
            官方推荐使用应用私有目录来缓存优化后的dex文件，dexOutputDir = context.getDir(“dex”, 0);

            参三 libraryPath：动态库的路径

            参四 parent：当前加载器的父类加载器
         */
    }

    @Override
    public void b_作用() {

        //
        stu_findLoadedClass();

        //
        stu_loadClass();

        //
        stu_findClass();
    }

    private void stu_findLoadedClass() {
        /*
    protected final Class<?> findLoadedClass(String name) {
        ClassLoader loader;
        if (this == BootClassLoader.getInstance())
            loader = null;
        else
            loader = this;
        return VMClassLoader.findLoadedClass(loader, name);
    }
         */
    }

    private void stu_loadClass() {
        /*
    protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    if (parent != null) {
                    // 双亲委派
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    // 自己查找
                    c = findClass(name);
                }
            }
            return c;
    }
         */
    }

    private void stu_findClass() {
        //BaseDexClassLoader.findClass
        /*
126        this.pathList = new DexPathList(this, dexFiles);
        =========
        protected Class<?> findClass(String name) throws ClassNotFoundException {
131        List<Throwable> suppressedExceptions = new ArrayList<Throwable>();
132        Class c = pathList.findClass(name, suppressedExceptions);
133        if (c == null) {
134            ClassNotFoundException cnfe = new ClassNotFoundException(
135                    "Didn't find class \"" + name + "\" on path: " + pathList);
136            for (Throwable t : suppressedExceptions) {
137                cnfe.addSuppressed(t);
138            }
139            throw cnfe;
140        }
141        return c;
142    }
         */
        // DexPathList.findClass()
        /*
108        this.dexElements = makeInMemoryDexElements(dexFiles, suppressedExceptions);
===============

      public Class<?> findClass(String name, List<Throwable> suppressed) {
485        for (Element element : dexElements) {
486            Class<?> clazz = element.findClass(name, definingContext, suppressed);
487            if (clazz != null) {
488                return clazz;
489            }
490        }
491
492        if (dexElementsSuppressedExceptions != null) {
493            suppressed.addAll(Arrays.asList(dexElementsSuppressedExceptions));
494        }
495        return null;
496    }
         */

        // Element.findClass
        /*
 public Class<?> findClass(String name, ClassLoader definingContext,
723                List<Throwable> suppressed) {
724            return dexFile != null ? dexFile.loadClassBinaryName(name, definingContext, suppressed)
725                    : null;
726        }
         */

        // DexFile.loadClassBinaryName
        /*
        public Class loadClassBinaryName(String name, ClassLoader loader, List<Throwable> suppressed) {
276        return defineClass(name, loader, mCookie, this, suppressed);
277    }
278
279    private static Class defineClass(String name, ClassLoader loader, Object cookie,
280                                     DexFile dexFile, List<Throwable> suppressed) {
281        Class result = null;
282        try {
283            result = defineClassNative(name, loader, cookie, dexFile);
284        } catch (NoClassDefFoundError e) {
285            if (suppressed != null) {
286                suppressed.add(e);
287            }
288        } catch (ClassNotFoundException e) {
289            if (suppressed != null) {
290                suppressed.add(e);
291            }
292        }
293        return result;
294    }
=============
392    private static native Class defineClassNative(String name, ClassLoader loader, Object cookie,
393                                                  DexFile dexFile)
         */

        //
    }

    private void 插件化中加载dex的原理() {

//        理解了上面的流程就很好理解在插件化中加载第三方插件的原理，因为插件化中我们的插件apk不会让系统加载，
//        所以我们需要自己加载，然后把插件apk的dex插入到数组apk的dex中，插入到哪里呢？
//        看上面的加载过程，就是通过Hook的方式把插件apk中的dex插入到宿主apk的 dexElements数组中，
//        这样系统在加载宿主的dex的时候就会加载插件的dex，通过这样的方式就骗过了系统。

//        这里多思考一下，如果插件的dex中存在了和宿主相同的class，那么最后会使用哪个class呢？
//        根据上面的findClass源码，在找到需要的Class对象以后就会返回，不会在继续遍历dexElements数组，
//        所以前面的类会覆盖掉后面的类，热修复框架实现的原理就只这个。


    }

}
