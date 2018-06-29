import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class NewLog {







    public  static  class TokenizerMapper
            extends Mapper<Object, Text, Text, Text> {

        private String id;
        private Text word = new Text();
        private Text keyID=new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {

           int count=8000000;//每天用户
           int NumberLog=30;//每天的日志数
           int out=0;
           int in=1;

            SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            SimpleDateFormat format1=new SimpleDateFormat("HH-mm-ss");
            String initDateStr="2017-01-01-00:00:00";
            Date initDate= null;//str表示yyyy年MM月dd HH:mm:ss格式字符串
            try {
                initDate = format.parse(initDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }






          for(int i=0;i<count;i++)
          {
              System.out.println(i);
              for(int j=0;j<NumberLog;j++) {
                  Date  date=new   Date();
                  Calendar calendar = new GregorianCalendar();
                  calendar.setTime(initDate);
                  calendar.add(calendar.DATE,j);

                  Random r=new Random();

                 int randInt1=r.nextInt(24);
                 int randInt2=r.nextInt(24);
                 int max=(randInt1>=randInt2? randInt1:randInt2);
                 int min=(randInt1>=randInt2? randInt2:randInt1);
                 int min1= r.nextInt(60);
                 int min2= r.nextInt(60) ;
                 int  second1=r.nextInt(60);
                 int second2=r.nextInt(60);
                 keyID=new Text(String.valueOf(i));
                  calendar.add(calendar.HOUR,min); 
                  calendar.add(calendar.MINUTE,min1);
                  calendar.add(calendar.SECOND,second1);
                  date=calendar.getTime(); 


                  String intTime=format.format(date);
                
                  word=new Text(intTime+" "+String.valueOf(in));
                  context.write(word,keyID);

                  calendar.add(calendar.HOUR,(max-min)); 
                  calendar.add(calendar.MINUTE,min2);
                  calendar.add(calendar.SECOND,second2);
                  date=calendar.getTime(); 
                  String outTime=format.format(date);
                  word=new Text(outTime+" "+String.valueOf(out));
                  context.write(word,keyID);

              }

          }

        }
    }

    public  static class IntSumReducer
            extends Reducer<Text, Text ,Text,Text> {
        private String word,hour,id,state;
        private Text dateTime=new Text();
        private Text keyId=new Text();
        public void reduce(Text key, Iterable<Text> values,
                           Context context
        ) throws IOException, InterruptedException {

            for ( Text val : values) {
                   word=val.toString();
                   keyId=new Text(word);
                    context.write(key,keyId);

            }




        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "new log");



        job.setJarByClass(NewLog.class);
        job.setMapperClass(NewLog.TokenizerMapper.class);
        job.setCombinerClass(NewLog.IntSumReducer.class);
        job.setReducerClass(NewLog.IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
       // job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        boolean success=  job.waitForCompletion(true);


        System.out.println("success "+success);
        
    }
}
