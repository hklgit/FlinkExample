package test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/4 11:07
 */
public class SimpleDateForamtThreadSafe {

    public static void main(String[] args) {

        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDt = "2018-04-03 12:00:03";
        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                try {
                    System.out.println(sdf.parse(strDt));
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }


        /*new Thread(()->{
            String create_time = sdf.format(date);
            System.out.println(create_time);
        },"bbb").start();

        new Thread(()->{
            String create_time = sdf.format(date);
            System.out.println(create_time);
        },"ccc").start();*/


    }

}
