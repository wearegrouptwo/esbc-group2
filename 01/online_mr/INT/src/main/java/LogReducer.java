import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.io.IntWritable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Handa on 2018/6/17.
 */
public class LogReducer extends Reducer<Text,DataBean,Text,LongWritable>{

    @Override
    protected void reduce(Text key, Iterable<DataBean> value, Context context)
        throws java.io.IOException ,InterruptedException{

        ArrayList al=new ArrayList(3);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd//HH:mm:ss");
        long time = 0;

        for(DataBean b:value){
        al.add(b.getDate());
        }

        /*for(int i=0;i<(al.size()-1);i=i+2)
        {
            //try {
                int date1 = Integer.parseInt(al.get(i).toString());
                int date2 = Integer.parseInt(al.get(i+1).toString());
                time += date2 - date1;
            //} catch (ParseException e) {
                //e.printStackTrace();
            //}

        }*/

        for(int i=0;i<(al.size()-1);i=i+2)
        {
            try {
                Date date1 = df.parse(al.get(i).toString());
                Date date2 = df.parse(al.get(i+1).toString());
                time += date1.getTime() - date2.getTime();
                System.out.println(">>>date:"+" "+date1+" "+date2);
            } catch (ParseException e){
                e.printStackTrace();
            }

        }

        context.write(key, new LongWritable(time));
    }

}
