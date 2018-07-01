import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



public class log_generater {

    //生成过程，参数为log数
    public static void generate_logs(long logs_num, int users_num, OutputStreamWriter writer)
            throws IOException {
        //用户id，用户状态数组
        int user_id;
        int[] user_status = new int[users_num];
        //每个时间点与用户数
        int per_time_num = 1;
        //日志缓冲
        String log_buffer;


        //日期格式
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String initDateStr="2017-05-01-00:00:00";
        Date initDate= null;
        try {

            initDate = format.parse(initDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(initDate);

        Date date = new Date();


        //模拟步骤
        String time_tmp;
        long i =0L;
        for(i = 0L; i < logs_num;  calendar.add(calendar.SECOND,1)){
            //随机产生时间点用户数
            per_time_num = (int) (Math.random() * 5);

            //根据时间点用户数生成日志
            for(int j = 0; j < per_time_num; j++,i++){
                //随机用户
                user_id = (int) (Math.random() * users_num);

                //判断状态
                if(user_status[user_id] == 0){

                    //取得时间
                    time_tmp=format.format(calendar.getTime());

                    //输出日志
                    log_buffer = time_tmp + " " + 0 + " " + user_id + "\n";
                    writer.write(log_buffer);

                    user_status[user_id] = 1;

                }else {

                    time_tmp=format.format(calendar.getTime());

                    log_buffer = time_tmp + " " + 0 + " " + user_id + "\n";
                    writer.write(log_buffer);

                    user_status[user_id] = 0;


                }
            }

        }


        //将未登出的用户登出
        for(int i = 0; i < users_num; i++){

            if(user_status[i] == 1){

                time_tmp = format.format(calendar.getTime());

                //System.out.println(outTime+" "+1+" "+i);
                log_buffer = (time_tmp + " " + 1+ " "+ i + "\n");
                writer.write(log_buffer);

                user_status[i] = 0;


            }
            calendar.add(calendar.SECOND,1);
        }

        //guanbi
        writer.close();
    }


    //main函数
    public static void main(String[] args) throws IOException {
        //输出文件，以及输出格式
        String file_output = "/usr/local/flume/log_test_ray";
        String charSet = "UTF-8";
        FileOutputStream stream_ouput = new FileOutputStream(file_output);
        OutputStreamWriter writer= new OutputStreamWriter(stream_ouput, charSet);

        //生成日志
        long logs_num = 200000;
        int users_num = 8000000;

        generate_logs(logs_num, users_num, writer);

    }

}
