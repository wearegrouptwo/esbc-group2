import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;





public class Map_Reduce {



    public static void main(String[] args) throws Exception {

        //System.out.println(args[1]);


        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        System.out.println(222);

        job.setJarByClass(Map_Reduce.class);

        job.setMapperClass(LogMapper.class);
        job.setReducerClass(LogReducer.class);

        System.out.println(333);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Log_Message.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass( Text.class );

        System.out.println(444);


        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.out.println(555);

        job.waitForCompletion(true);

        System.exit(0);
    }








}
