package com.sunny.module.stu.AJAVA.A泛型;

import com.sunny.module.stu.base.StuImpl;

import java.util.List;

class 总结泛型 extends StuImpl {

    @Override
    public void a_是什么() {
        // 参数化类型
    }

    @Override
    public void b_作用() {
        // 类型安全，编译期进行【类型检查】
        // 代码复用，
    }

    @Override
    public void l_限制() {
        // 不支持泛型数组
    }

    @Override
    public void s_面试点() {
        //Q: 可以把List<String>传递给一个接受List<Object>参数的方法吗？
        //A: 不能，编译后，方法的参数类型是List，

    }

    @Override
    public void n_知识点() {
        通配符();

        // java的泛型是 伪泛型。
        // 在编译期会进行【类型擦除】，即通过【类型检查】和【类型推导】将【泛型类型】转换成为【原始类型】
        类型擦除();

        // 泛型最大的好处就是在编译期进行类型检查，解决运行期类型转换异常
        类型检查();

        // 通过上下文推导出 T 的类型
        类型推导();

        // 如果子类重写了父类的方法，则编译时会在子类中生成一个桥方法
        桥方法();

    }

    private void 通配符() {
        /*
            常用通配符 T，E，K，V，？
                ？表示不确定的 java 类型
                T (type) 表示具体的一个java类型
                K V (key value) 分别代表java键值中的Key Value
                E (element) 代表Element
         */

        /*
            ？无界通配符
                表示不确定的 java 类型
         */

        /*
            上界通配符 < ? extends E>
                参数类型必须是 E 或者 E 的子类
         */

        /*
            下界通配符 < ? super E>
                参数类型必须是 E 或者 E 的父类，直到 Object
         */

        /*
            区别
                无界通配符：方法中不依赖于类型参数泛型类中的方法
                例如：List.size或List.clear。事实上，经常使用Class<?> 是因为类Class<T>中的大多数方法不依赖于T。

                上界通配符：用于读数据，方法中可以调用 E 的方法
                下界通配符：用于写数据

         */

    }

    private void 类型擦除() {
        /*
            在编译期，将【泛型类型】转换为【原始类型】，如：将 List<String> 转换为 List
            【泛型类型】：带有<T>的类型，比如 List<String>
            【原始类型(Raw Type)】：指的是忽略类型参数的泛型类，比如 List
         */

        /*
            类型检查：只针对【泛型方法】进行类型检查，判断传递的类型是否能转换为
                默认为Object
                如果指定了上界< T extent XXX >，则使用 XXX

         */

        /*
            类型推导：通过上下文关系，推断出 T 的类型
                参数类型：
                目标类型：
                返回类型：
         */

        /*
            类型擦除
                <T> void setValue(T t) 方法会被转换为
                void setValue(Object t)

                <T extends Number >  void setValue(T t) 会被转换为
                void setValue(Number t)
         */

        /*
            桥方法：如果方法是【重写】了父类的方法，编译器会自动生成一个【桥方法】

                父类方法：T getName()
                子类方法：String getName()，重写了父类的方法
            编译后
                子类方法1：Object getName() // 父类经过类型擦除后 T 转换为Object
                子类方法2：String getName(){
                    return (String) getName() //将父类返回值强转为String类型
                }
                // 方法1就是桥方法，是真正【重写】父类的方法，这种方法只能编译器来添加，开发中这种写法是错误的
         */

    }

    private void 类型检查() {
        /*
            类型检查：只针对【泛型方法】进行类型检查，判断传递的类型是否合法


            在编译期，编译器是知道传递给方法的参数是什么类型，然后通过类型
            在编译期通过类型推导，推断出传递给方法的参数类型，然后
         */
    }

    private void 类型推导() {
        /*
            类型推导：通过上下文关系，推断出 T 的类型
                参数类型：
                    默认为类型为Object
                    如果指定了上界< T extent XXX >，则类型为 XXX
                返回类型1.7：
                目标类型1.8：
         */
    }

    private void 桥方法() {
        /*
            桥方法：如果方法是【重写】了父类的方法，编译器会自动生成一个【桥方法】
                父类方法：T getName()
                子类方法：String getName()，重写了父类的方法
            编译后
                子类方法1：Object getName() // 父类经过类型擦除后 T 转换为Object
                子类方法2：String getName(){
                    return (String) getName() //将父类返回值强转为String类型
                }
                // 方法1就是桥方法，是真正【重写】父类的方法，这种方法只能编译器来添加，开发中这种写法是错误的
         */
    }

    @Override
    public void v_版本() {
        // jdk1.5 新增
    }

    @Override
    public void d_怎么用() {
        // 通过 <T> 来声明

        // 泛型类
        // class GenericClass<T>
        AUseDemo.GenericClass<String> clazz = new AUseDemo.GenericClass<>();
        clazz.setData("张三");
        String name = clazz.getData();


        // 泛型接口
        // interface GenericInterface<T>
        AUseDemo.GenericInterface<Integer> clazz2 = new AUseDemo.GenericInterfaceImpl<>();
        clazz2.setData(18);
        Integer age = clazz2.getData();


        // 泛型方法
        // public <T> List<T> genericMethod(T[] array)
        AUseDemo.NormalClass clazz3 = new AUseDemo.NormalClass();
        String[] array = {"aaa", "bbb", "ccc"};
        List<String> list = clazz3.genericMethod(array);
    }


}
