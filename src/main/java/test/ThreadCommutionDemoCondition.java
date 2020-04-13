package test;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/5 14:53
 */
public class ThreadCommutionDemoCondition {

  private Lock lock = new ReentrantLock();


    public static void main(String[] args) {
        final OddEvenNumPrint num = new OddEvenNumPrint();

        new Thread(() -> {
            num.odd();
        }).start();

        new Thread(() -> {
            num.even();
        }).start();


    }

}


