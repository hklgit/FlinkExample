package test;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/5 14:53
 */
public class ThreadCommutionDemo {

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

class OddEvenNumPrint {
    private int num;
    private Object obj = new Object();

    //打印奇数的方法
    public void odd() {
        //判断当前的num是奇数还是偶数，是奇数打印，否则就是等待
        while (num < 10) {
            synchronized (obj) {
                if (num % 2 == 1) {
                    System.out.println(num);
                    num++;
                    obj.notify();
                } else {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //打印偶数的方法
    public void even() {
//判断当前的num是奇数还是偶数，是偶数打印否则就是等待

        while (num < 10) {
            synchronized (obj) {
                if (num % 2 == 0) {
                    System.out.println(num);
                    num++;
                    obj.notify();
                } else {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}
