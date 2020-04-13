package test;

/**
 * Be an attitude rather than personality programmer
 *
 * @author kerven han
 * @Package test
 * @date 2020/4/6 0:10
 */
public class StringIntern {
    public static void main(String[] args) {

        String str =  "hankl";

        String string = new String("fsfsdf");
        System.out.println(str.intern() == str);
        System.out.println(string.intern() == string
        );


    }




}
