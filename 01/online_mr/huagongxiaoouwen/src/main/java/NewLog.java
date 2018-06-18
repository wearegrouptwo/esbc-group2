//import java.io.DataInput;
//import java.io.DataOutput;
//import java.io.File;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.io.Writable;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.util.StringUtils;
//
//
//public class NewLog {
//
//
//    static Log_Message lmsg = new Log_Message();
//
//
//
//
//    public static  class TokenizerMapper
//            extends Mapper<Object, Text, Text,Log_Message> {
//
//
//        public void map(Object key, Text value, Context context
//        ) throws IOException, InterruptedException {
//
//
//
//            //拿一行数据出来
//            String line = value.toString();
//            //切分
//            String[] fields = StringUtils.split(line, ' ');
//
//            //取出需要的字段
//            String time = fields[0];
//            int log_status = Integer.parseInt(fields[1]);
//            String uid = fields[2];
//
//            System.out.println(uid);
//
//            lmsg.setUid(uid);
//            lmsg.setTime(time);
//            lmsg.setLog_status(log_status);
//
//
//            //封装数据并输出
//            context.write(new Text(uid), lmsg);
//
//
//        }
//    }
//
//
//
//    public static class IntSumReducer
//            extends Reducer<Text, Log_Message ,Text,Log_Message> {
//
//
//
//        public void reduce(Text key, Iterable<Log_Message> values,
//                           Context context
//        ) throws IOException, InterruptedException {
//
//
//            //System.out.println(1234);
//
//
//            BigInteger in_time = BigInteger.valueOf(0);
//            BigInteger out_time=BigInteger.valueOf(0);
//
//            SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SS");
//
//            //System.out.println("adadadada");
//
//
//            for(Log_Message b : values){
//
//                System.out.println(b.getUid());
//
//
//                if(b.getLog_status() == 0){  // if login
//
//
//                    try {
//                        in_time.add(  BigInteger.valueOf(format.parse(b.getTime()).getTime())); //毫秒   );
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }else{  // if logout
//
//                    try {
//                        out_time.add(  BigInteger.valueOf(format.parse(b.getTime()).getTime())); //毫秒   );
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//
//
//
//
//            //写出时，value是一个FlowBean对象，因为要写到文件中去，所以要重写其toString()方法
//            context.write(key,lmsg  );     //out_time.subtract(in_time).divide( BigInteger.valueOf(1000*60*60)  )
//
//
//
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        Configuration conf = new Configuration();
//        Job job = Job.getInstance(conf, "new log");
//
//
//
//        job.setJarByClass(NewLog.class);
//
//        job.setMapperClass(NewLog.TokenizerMapper.class);
//        job.setCombinerClass(NewLog.IntSumReducer.class);
//        job.setReducerClass(NewLog.IntSumReducer.class);
//
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(Log_Message.class);
//
//        FileInputFormat.addInputPath(job, new Path(args[0]));
//        FileOutputFormat.setOutputPath(job, new Path(args[1]));
//        boolean success=  job.waitForCompletion(true);
//
//
//        System.out.println("success "+success);
//        // System.exit(job.waitForCompletion(true) ? 0 : 1);
//    }
//}
