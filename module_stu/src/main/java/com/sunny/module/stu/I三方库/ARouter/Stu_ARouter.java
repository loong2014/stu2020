package com.sunny.module.stu.I三方库.ARouter;

import com.sunny.module.stu.base.StuImpl;

/**
 * 使用
 * https://blog.csdn.net/u010227042/article/details/104366139
 * 原理
 * https://blog.csdn.net/LY010106/article/details/119004856
 * https://www.jianshu.com/p/857aea5b54a8
 */
public class Stu_ARouter extends StuImpl {
    @Override
    public void a_是什么() {
        // 阿里的路由框架
    }

    @Override
    public void c_功能() {
        // 实现模块间的页面跳转
        /*
        // 构建标准的路由请求，startActivityForResult
        // navigation的第一个参数必须是Activity，第二个参数则是RequestCode
        ARouter.getInstance().build("/home/main", "ap").navigation(this, 5);

        //
        Postcard postcard = ARouter.getInstance().build("/module2/activity");
        LogisticsCenter.completion(postcard);
        Class<?> destination = postcard.getDestination();

        Intent intent = new Intent(getContext(),destination);
        startActivityForResult(intent,requestCode);

        // ARouter会自动对字段进行赋值，无需主动获取

        @Autowired(name = "girl") // 通过name来映射URL中的不同参数
        boolean boy;

        ARouter.getInstance().inject(this);
         */

        // 实现模块间数据交互
        /*
        HelloService helloService =   (HelloService) ARouter.getInstance().build("/service/hello").navigation();
        helloService.sayHello("Vergil");

        Fragment fragment = (Fragment) ARouter.getInstance().build("/test/fragment").navigation();

         */
    }

    @Override
    public void p_流程() {
        /*
        1、就是代码的书写，在Application中进行ARouter初始化，在要跳转的目标activity上加Route注解。

        2、就是在编译阶段，编译阶段时，ARouter的apt注解管理器会获取所有注解Arouter的元素，根据poet框架来构造出loadInto方法，
        在根据获得的元素，创建出RouteMeta对象，然后根据group来进行分组，然后对每个routeMeta对象生成2个java文件，
        IRouteRoot和IRouteGroup的实现类，这两个实现类存放了path和activityClass映射关系的类文件，
        两个map：<group,IRouterGroup>,<path,RouteMeta>。

        3、初始化，调用的ARouter.init（），有一个类叫Warehouse，这个类里面有几个map，对应我们APT生成的类的map存放数据。
        这个init通过层层调用，最后到达一个LogisticsCenter.init（）方法，在这里会获取到所有配置route的文件，
        然后通过反射拿到IRouterRoot的实现类，拿到了group和IRouterGroup，将这写入Warehouse的某个map当中。

        4、跳转阶段：调用ARouter.getInstance().build("/login/login1").navigation()，
        这个会调用-Route.build方法，根据传入path，构造出一个postCard（存放了group和path，是RouteMeta的子类）
        ，然后通过postCard去获取IRouteGroup类即间接获取RouteMeta，有缓存则直接拿，没有缓存则从Warehouse的map中去拿，
        拿到后反过来完善postCard的信息，然后调用navigation时，实际上调用的是_ARouter的-navigation方法，
        这里面自动new一个intent，把postCard中存的activity传入，然后调用runInMainThread回到主线程跳转。
        （runInMainThread是通过得到主线程的Looper，再判断当前线程是不是主线程，如果不是就通过handler发消息，如果是就直接run。）
         */
    }
}
