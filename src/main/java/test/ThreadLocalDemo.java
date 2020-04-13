package test;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/5 17:35
 */
public class ThreadLocalDemo {

    //定义了了一个全局的threadlocal
    private static ThreadLocal<String> tl = new ThreadLocal();
    private String content;

    public String getContent() {
//        return content;
        return tl.get();
    }

    public void setContent(String content) {
//        this.content = content;
        tl.set(content);

    }

    public static void main(String[] args) {


        ThreadLocalDemo tld = new ThreadLocalDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
               tld.setContent(Thread.currentThread().getName());
                System.out.println("设置的值===>"+Thread.currentThread().getName() + "获取的值 ==>" + new ThreadLocalDemo().getContent());
            },""+i).start();
        }




    }
}
