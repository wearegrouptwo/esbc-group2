import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class log_producer {


    public static void generate() throws IOException, URISyntaxException, InterruptedException {

        String file = "/usr/local/flume/test.log";
        String charSet = "UTF-8";
        FileOutputStream fileWriter = new FileOutputStream(file, true);
        OutputStreamWriter writer = new OutputStreamWriter(fileWriter, charSet);


        String buff;


        int user_num = 8000000;
        int per_second = 40000;
        int[] user_status = new int[user_num];  // 0 login    1  logout    default value is 0
        int r_userid;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");


        String now_time = format.format(Calendar.getInstance().getTime());

        while (true) {

            if (!now_time.equals(format.format(Calendar.getInstance().getTime()))) {

                now_time = format.format(Calendar.getInstance().getTime());
                for (long i = 0L; i < per_second; i++) {

                    r_userid = (int) (Math.random() * user_num);

                    if (user_status[r_userid] == 0) {

                        buff = now_time + " " + 0 + " " + r_userid + "\n";
                        writer.write(buff);
                        //System.out.println(now_time);

                        user_status[r_userid] = 1;

                    } else {

                        buff = (now_time + " " + 1 + " " + r_userid + "\n");
                        writer.write(buff);
                        //System.out.println(now_time);
                        user_status[r_userid] = 0;

                    }
                }
            }
        }
    }



    public static void main(String[] args) throws Exception {

        generate();

    }

}
