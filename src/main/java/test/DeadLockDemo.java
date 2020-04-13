package test;

import org.apache.flink.shaded.netty4.io.netty.util.DefaultAttributeMap;

import java.util.concurrent.TimeUnit;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/5 13:36
 */
public class DeadLockDemo implements  Runnable {
    private int flag; // 标记
    //注意这里的锁必须是静态的，这样的话才能保证锁是一样的，因为在后面我们使用的时候我们会new DeadLockDemo的实例对象
    private static Object obj1 = new Object(); // 锁1
    private static Object obj2 = new Object(); //  锁2


    public DeadLockDemo(){ }
    public DeadLockDemo(int flag){
        this.flag = flag ;
    }

    @Override
    public void run() {
        if( 1 == flag){
            //持有锁1然后要获取锁2
            synchronized (obj1){
                System.out.println(Thread.currentThread().getName()+"持有锁1,等待锁2");
                try {
                    TimeUnit.SECONDS.sleep(1); // 让出cpu的执行权让thread2 来执行
                } catch (InterruptedException e) { }
                synchronized (obj2){
                    System.out.println(Thread.currentThread().getName()+"持有锁2,等待锁1");
                }
            }
        }else{
            //持有锁2然后要获取锁1
            synchronized (obj2){
                System.out.println(Thread.currentThread().getName()+"持有锁2,等待锁1");
                try {
                    TimeUnit.SECONDS.sleep(1); // 让出cpu的执行权让thread2 来执行
                } catch (InterruptedException e) { }
                synchronized (obj1){
                    System.out.println(Thread.currentThread().getName()+"持有锁1,等待锁2");
                }
            }
        }




    }
}
