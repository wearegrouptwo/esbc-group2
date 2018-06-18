import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LogReducer extends Reducer<Text, Log_Message, Text, Text> {


    //框架每传递一组数据<1387788654,{FlowBean,FlowBean,FlowBean...}>调用一次reduce方法
    //reduce中的业务逻辑是遍历values，然后进行累加求和后输出
    protected void reduce(Text key, Iterable<Log_Message> value, Context context)
            throws java.io.IOException ,InterruptedException {



        BigInteger in_time = BigInteger.valueOf(0);
        BigInteger out_time=BigInteger.valueOf(0);;

        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SS");



        for(Log_Message b : value){


            if(b.getLog_status() == 0){  // if login


                try {
                    in_time = in_time.add(  BigInteger.valueOf(format.parse(b.getTime()).getTime())); //毫秒   );

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }else{  // if logout

                try {

                    //System.out.println(   format.parse(b.getTime()).getTime()  )  ;

                    out_time =  out_time.add(  BigInteger.valueOf(format.parse(b.getTime()).getTime())    ); //毫秒   );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }


        out_time = out_time.subtract(in_time);
        //out_time = out_time.divide( BigInteger.valueOf(1000*60*60));
        System.out.println("r-"+out_time);

        //写出时，value是一个FlowBean对象，因为要写到文件中去，所以要重写其toString()方法
        context.write(key, new Text( String.valueOf(out_time))  );     //out_time.subtract(in_time).divide( BigInteger.valueOf(1000*60*60)  )

    }
}