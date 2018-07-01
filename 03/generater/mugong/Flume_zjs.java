
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

public class Flume_zjs {


    public static void main(String[] args) throws Exception {

        geneLog();

    }


    public static void geneLog() throws IOException, URISyntaxException {

        String file="/usr/local/flume/test_zjs.log";
        String charSet="UTF-8";
        FileOutputStream fileWriter=new FileOutputStream(file);
        OutputStreamWriter writer=new OutputStreamWriter(fileWriter, charSet);



        String buff;



        int user_num = 8000000;
        long log_num = 200000L;
        int[] user_status = new int[user_num];  // 0 login    1  logout    default value is 0
        int r_userid;
	
	//格式化时间
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
        String initDateStr="2017-05-01-00:00:00:000";
        Date initDate= null;
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

           
            r_userid = (int) (Math.random()*user_num);
           

            if(user_status[r_userid] == 0){

                outTime=format.format(calendar.getTime());
                buff = outTime+" "+0+" "+r_userid+"\n";
                writer.write(buff);
                user_status[r_userid] = 1;

            }else {

                outTime=format.format(calendar.getTime());
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






}
