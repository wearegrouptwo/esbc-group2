import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;

public class LogMapper extends Mapper<LongWritable, Text, Text, Log_Message> {

    //拿到日志中的一行数据，并切分成各个字段，，抽取出我们需要的字段
    //：手机号，上行流量、下行流量  接着封装成k-v发送出去


    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {




        //拿一行数据出来
        String line = value.toString();
        //切分
        String[] fields = StringUtils.split(line, ' ');

        //取出需要的字段
        String time = fields[0];
        int log_status = Integer.parseInt(fields[1]);
        String uid = fields[2];

        System.out.println("m-"+time);

        //封装数据并输出
        context.write(new Text(uid), new Log_Message(time,log_status,uid));



    }
}