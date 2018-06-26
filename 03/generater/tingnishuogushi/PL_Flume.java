
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Integer.parseInt;

public class PL_Flume {


    public static void generate() throws IOException, URISyntaxException {

        String file="/usr/local/flume/test.log";
        String charSet="UTF-8";
        FileOutputStream fileWriter=new FileOutputStream(file);
        OutputStreamWriter writer=new OutputStreamWriter(fileWriter, charSet);



        String buff;



        int user_num = 8000000;
        long log_num = 200000L;
        int[] user_status = new int[user_num];  // 0 login    1  logout    default value is 0
        int r_userid;

        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
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
                buff = outTime+" "+0+" "+r_userid+"\n";
                writer.write(buff);

                user_status[r_userid] = 1;

            }else {

                outTime=format.format(calendar.getTime());

                //System.out.println(outTime+" "+1+" "+r_userid);
                buff = (outTime+" "+1+" "+r_userid+"\n");
                writer.write(buff);

                user_status[r_userid] = 0;

            }

            calendar.add(calendar.MILLISECOND,1);


            if(i % 10000000 == 0){
                System.out.println(i+" yi");
            }


        }



        for(int i = 0; i < user_num; i++){

            if(user_status[i] == 1){

                outTime=format.format(calendar.getTime());

                //System.out.println(outTime+" "+1+" "+i);
                buff = (outTime+" "+1+" "+i+"\n");
                writer.write(buff);

                user_status[i] = 0;


            }
            calendar.add(calendar.MILLISECOND,1);
        }

        writer.close();

    }



    public static void main(String[] args) throws Exception {

        generate();

    }






}
