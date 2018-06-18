import java.net.URI;import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Integer.parseInt;

public class Produce_Log {


 public static void generate() throws IOException, URISyntaxException {


     //获取Hadoop的配置信息
     Configuration conf = new Configuration();
     // 不设置该代码会出现错误：java.io.IOException: No FileSystem for scheme: hdfs
     conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

     //告诉系统HDFS的路径在哪里    hdfs://116.56.136.75:9000/user/su/su_logs_qiqian
     String filePath = "input/test"; //其中hdfs://group2是在Hadoop配置文件中写好的路径，不需要带端口号，/test.txt就是在HDFS根目录下的test.txt
     Path path = new Path(filePath);

     // 这里需要设置URI，否则出现错误：java.lang.IllegalArgumentException: Wrong FS: hdfs://127.0.0.1:9000/test/test.txt, expected: file:///
     FileSystem fs = FileSystem.get(new URI(filePath), conf);
     byte[] buff;
     FSDataOutputStream os = fs.create(path);



     int user_num = 8000000;
     long log_num = 70000000L;
     int[] user_status = new int[user_num];  // 0 login    1  logout    default value is 0
     int r_userid;

     SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SS");
     String initDateStr="2017-05-01-00:00:00:000";
     Date initDate= null;//str表示yyyy年MM月dd HH:mm:ss格式字符�?
     try {

         initDate = format.parse(initDateStr);
     } catch (ParseException e) {
         e.printStackTrace();
     }

     Calendar calendar = new GregorianCalendar();
     calendar.setTime(initDate);

     Date date=new Date();
     String outTime;


     for(long i = 0L ; i <log_num; i++){

         // random user id
         r_userid = (int) (Math.random()*user_num);
         //System.out.println(r_userid);
         //System.out.println(Math.random()*user_num);


         if(user_status[r_userid] == 0){


             outTime=format.format(calendar.getTime());

             //System.out.println(outTime+" "+0+" "+r_userid+"\n");
             buff = (outTime+" "+0+" "+r_userid+"\n").getBytes();
             os.write(buff, 0, buff.length);

             user_status[r_userid] = 1;

         }else {

             outTime=format.format(calendar.getTime());

             //System.out.println(outTime+" "+1+" "+r_userid);
             buff = (outTime+" "+1+" "+r_userid+"\n").getBytes();
             os.write(buff, 0, buff.length);

             user_status[r_userid] = 0;

         }

         calendar.add(calendar.MILLISECOND,1);


         if(i % 1000000 == 0){
             System.out.println(i+" yi");
         }


         }



         for(int i = 0; i < user_num; i++){

            if(user_status[i] == 1){

                outTime=format.format(calendar.getTime());

                System.out.println(outTime+" "+1+" "+i);
                buff = (outTime+" "+1+" "+i+"\n").getBytes();
                os.write(buff, 0, buff.length);

                user_status[i] = 0;


            }
             calendar.add(calendar.MILLISECOND,1);
         }

     os.close();
     fs.close();

 }



    public static void main(String[] args) throws Exception {

        generate();

    }






}
