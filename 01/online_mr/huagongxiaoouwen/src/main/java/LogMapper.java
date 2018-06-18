import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;


/**
 * Created by Handa on 2018/6/17.
 */
public class LogMapper extends Mapper<Object,Text,Text,DataBean> {

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException{
        String line = value.toString();
        String[] fields = line.split("  ");
        String id = fields[1];
        String date= fields[0];

        boolean status;
        if (fields[2] == "0")
            status=false;
        else status=true;

        context.write(new Text(id),new DataBean(id,date,status));
    }


}
