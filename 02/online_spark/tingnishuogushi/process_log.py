
#encoding:utf-8

from pyspark import SparkContext
import pyspark
from pyspark import SparkConf
import time
import datetime



conf = SparkConf().setAppName("test").setMaster("spark://116.56.136.76:7077") #设置spark masster进程的位置如果是本机则使用 local[*]
sc = SparkContext.getOrCreate(conf)

rawData = sc.textFile("/user/su/su_logs_yiyi2") #   spark默认使用hdfs路径，如果采用本机文件则路径为：file:/usr/local/log1
print rawData.first()

def mapper(x):
  
  xsplit = x.split(" ")
  mill = long( xsplit[0].split(':')[3])

  #将日期转成秒时间戳
  sec = xsplit[0].split(':')
  sec = sec[0]+":"+sec[1]+":"+sec[2]
  millsec = long(time.mktime(time.strptime(sec, "%Y-%m-%d-%H:%M:%S")))*1000 + mill 

  

  if int(xsplit[1])==0:
    millsec = -millsec

  return (x.split(" ")[2],millsec)  #x.split(" ")[2]


result = rawData.map(mapper).reduceByKey(lambda x, y: x+y)
eve_count = result.count()
result = result.map(lambda x: (0,x[1])).reduceByKey(lambda x, y: x+y).map(lambda x: (0,float(x[1])/(eve_count*1000*60*60)/(100000000/(60*60*1000))*24))



result.saveAsTextFile("/user/su/su_logs_yiyi2_result")









