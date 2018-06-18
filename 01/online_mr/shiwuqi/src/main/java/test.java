import java.math.BigInteger;
import java.text.SimpleDateFormat;

import static java.lang.System.*;

public class test {



    public static void main(String[] args) throws Exception {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        BigInteger millionSeconds1 = BigInteger.valueOf(sdf.parse("20120809030000").getTime());//毫秒
        BigInteger millionSeconds2 = BigInteger.valueOf(sdf.parse("20120811030000").getTime());//毫秒

        millionSeconds2.subtract(millionSeconds1);

        out.println(millionSeconds2.subtract(millionSeconds1));


    }



}
