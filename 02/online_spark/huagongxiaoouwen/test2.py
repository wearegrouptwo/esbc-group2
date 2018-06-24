#encoding=utf-8
from pyspark import SparkConf, SparkContext
import time

conf = SparkConf()
#conf.setMaster("local[*]")
conf.setMaster("spark://116.56.136.75:7077")
conf.setAppName("logCountByHanda")
sc = SparkContext(conf=conf)
#输入文件和输出文件
#logFile = "file:/usr/local/log1"
#outFile = "file:/usr/local/log2"

logFile = "/user/su/su_logs_yiyi2"
outFile = "/user/liang/liang_logs_yiyi2_result"

#count
text_file = sc.textFile(logFile)

def func1(x):
    #print x+"\n"
    y=x.split(" ")
    #print y
    t1=y[0]
    t2=y[1]
    t3=y[2]
    millsec=float(t1[-3:]) 
    #print millsec 
    ts=time.mktime(time.strptime(t1[:-4],"%Y-%m-%d-%H:%M:%S"))*1000
    time_total=ts+millsec
    if(t2=="0"):
        time_total=-time_total
    return  (t3,time_total)

rdd = text_file.map(func1).reduceByKey(lambda a,b:a+b)
#求平均时间
#先求出用户数量，然后求总时间(天数)，最后求平均每人每天在线时间，把结果写入文档
total_user=rdd.count()
total_day=100000000/(24*60*60*1000)

time_sum=rdd.map(lambda x:(0,x[1])).reduceByKey(lambda a,b:a+b)
time_average=time_sum.map(lambda x:("average_time:",x[1]/(total_user*total_day)))

#.map(lambda line: line.split(" "))
print rdd.collect()
print time_sum.collect()
print time_average
time_average.saveAsTextFile(outFile)  



    

