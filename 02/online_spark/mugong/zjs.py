#encoding:utf-8

from pyspark import SparkContext
import pyspark
from pyspark import SparkConf
import time
import datetime



conf = SparkConf().setAppName("testZjs").setMaster("spark://116.56.136.76:7077")
sc = SparkContext.getOrCreate(conf)
data = sc.textFile("/user/su/su_logs_yiyi2") 

print data.first()

def mapper(line):
  
  lsplit = line.split(" ")

  ms = long( lsplit[0].split(':')[3])
  sec = lsplit[0].split(':')
  sec = sec[0]+":"+sec[1]+":"+sec[2]
  totms = long(time.mktime(time.strptime(sec, "%Y-%m-%d-%H:%M:%S")))*1000 + ms 

  if int(xsplit[1])==0:
    totms = -totms

  return (line.split(" ")[2],totms)  


result = rawData.map(mapper).reduceByKey(lambda x, y: x+y)
eve_count = result.count()
result = result.map(lambda x: (0,x[1])).reduceByKey(lambda x, y: x+y).map(lambda x: (0,float(x[1])/(eve_count*1000*60*60)/(100000000/(60*60*1000))*24))



result.saveAsTextFile("file:/usr/local/zjs_test_result1")
