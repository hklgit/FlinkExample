package test;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/5 13:43
 */
public class DeadLockDemoTest {

    public static void main(String[] args) {

        DeadLockDemo lock1 = new DeadLockDemo(1);
        DeadLockDemo lock2 = new DeadLockDemo();

        Thread t1 = new Thread(lock1);
        Thread t2 = new Thread(lock2);
        t1.start();
        t2.start();
//        完美解决问题

    }
}
