package com.huazai.juc.test.volatiles;


import lombok.extern.slf4j.Slf4j;
import org.openjdk.jcstress.annotations.*;

/**
 * 情况1：线程1 先执行，这时 ready = false，所以进入 else 分支结果为 1
 * 情况2：线程2 先执行 num = 2，但没来得及执行 ready = true，线程1 执行，还是进入 else 分支，结果为1
 * 情况3：线程2 执行到 ready = true，线程1 执行，这回进入 if 分支，结果为 4（因为 num 已经执行过了）
 * <p>
 * JCStress 注解说明
 *
 * @author pyh
 * @JCStressTest 标记一个类为并发测试的类，它有一个org.openjdk.jcstress.annotations.Mode枚举类型的属性value。
 * Mode.Continuous模式表示会运行几个Actor，Ariter线程，并收集统计结果。
 * Mode.Termination模式代表运行具有阻塞/循环操作的单个Actor，看是否响应Singal信号。
 * @State 标记一个类是有状态的，即拥有可以读写的数据，例如类的成员变量。
 * State类只能是public的，不能是内部类（可以是静态内部类），并且得有一个默认构造方法。
 * @Outcome 描述测试的结果，它有3个属性，id属性为一个字符串数组，表示接收的结果，支持正则表达式；
 * expect表示对观测结果的期望，它的值是一个枚举值；desc属性指定一个易于人类理解的对结果的描述。@Outcomes注解可以组合多个结果注解。
 * @Actor 是一个中心测试注解，它标记的方法会被一个特定的线程调用，每一个对象的方法只能被调用一次。
 * 多个Actror方法调用顺序是不保证的，它们是并发执行的，方法可以抛出异常并且会导致测试失败。Actor方法所在的类必须有State或者Result注解。
 * @Arbiter 它的作用其实和@Actor差不多，但是Arbiter标记的方法调用是在所有@Actor标记的方法调用之后，所以它标记的方法一般作为收集最后的结果来使用。
 * @Signal 此注解也是标记方法的，但是它是在JCStressTest的Termination模式下工作的，它的调用是在所有Actor之后。
 * @Result 它标记的类被作为测试结果的类，JCStress自带的org.openjdk.jcstress.infra.results包下就有大量的测试结果类，
 * 不同的类可以用来保持不同的结果。例如Result类有一个int类型的变量r1；
 * <p>
 * 测试方法：
 * 一、新起一个工程，打包执行命令：java -jar target/VisibilityTest2.jar
 * 二、配置程序的主类，org.openjdk.jcstress.Main是JCStress自带的一个启动类；
 * 然后可以配置-t参数设置需要测试的类，当然 -t 后面也可以指定包名，表示执行指定包下的所有测试类。
 * 如果不指定-t参数，默认会扫描项目下所有包的类。
 * <p>
 * 解决办法：volatile 修饰的变量，可以禁用指令重排
 * @datetime
 * @description 指令重排序对多线程的影响
 * @className
 */

@JCStressTest
@State
@Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "normal")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "unNormal")
@Slf4j
public class VisibilityTest2 {
    int num = 0;
    /*
         volatile的有序性是通过插入内存屏障来保证指令按照顺序执行，不会存在后面的指令跑到前面的指令先执行，
         保证编译器优化的时候不会让指令乱序。
         volatile的作用：将修改后的变量立即写回主存；禁止指令重排序
    */
    /*volatile*/ boolean ready = false;

    @Actor
    public void actor1(CResult r) {
        if (ready) {
            r.r1 = num + num;
        } else {
            r.r1 = 1;
        }
    }

    @Actor
    public void actor2(CResult r) {
        /*
         此处赋值可能发生指令重排序，ready变量比num先赋值，num还没来得及赋值，此时发生线程上下文切换，
         其他线程执行actor1，执行r.r1 = num + num;代码，此时r.r1=0；
         */
        num = 2;
        ready = true;
    }

}
