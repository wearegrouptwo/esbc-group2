
import pyspark
from pyspark import SparkContext
from pyspark import SparkConf
import time
import datetime


#连接spark集群
conf = SparkConf().setAppName("processLog").setMaster("spark://116.56.136.76:7077")

log = SparkContext.getOrCreate(conf)
#读取log日志
rawData = log.textFile("/user/log/log1") 


def mapLog(line):
  
  #分割空格每一行的字符
  word = line.split(" ")
  #计算时间的时刻毫秒数
  mill = long( word[0].split(':')[3])
  sec = word[0].split(':')
  sec = sec[0]+":"+sec[1]+":"+sec[2]
  millsec = long(time.mktime(time.strptime(sec, "%Y-%m-%d-%H:%M:%S")))*1000 + mill 

  
   
  if int(word[1])==0:
    millsec = -millsec
    
  return (line.split(" ")[2],millsec)

#计算每个用户的在线总时间
EachUserTime= rawData.map(mapLog).reduceByKey(lambda x, y: x+y)
#用户的个数
UserNum=EachUserTime.count()
#在线时间天数
dateN=100000000/(60*60*1000)*24
#计算每个用户每天在线时间
result = EachUserTime.map(lambda x: (1,x[1])).reduceByKey(lambda x, y: x+y).map(lambda x: (1,float(x[1])/(eve_count*1000*60*60)*dateN))

#输出结果保存下来
result.saveAsTextFile("/user/log/result")

