# encoding: utf-8
#在pyspark模块中引入SparkContext和SparkConf类
#在operator模块中导入add类
from pyspark import SparkContext, SparkConf 
from operator import add
import time


# 定义map函数
def mapper(line):
    # 获取原始字符串数据数组
    log_datas = line.split(" ")
    
    time_s = log_datas[0]
    user_status = log_datas[1]
    user_id = log_datas[2]
    
    # 将时间转化成毫秒
    #先分解开一个字符串，拆开成前半和后半
    time_string_main = time_s[:-4]
    time_string_rest = time_s[-3:]

    #将前半转换成秒，再换成毫秒。将后半转
    time_main = time.strptime(time_string_main, "%Y-%m-%d-%H:%M:%S")
    time_main = time.mktime(time_main)*1000

    time_rest = float(time_string_rest)

    time_total = time_main + time_rest

    #判断是登入还是登出，判断是否取反
    if user_status == "0":
        time_total = -time_total

    #生成键值对
    return (user_id, time_total)


#应用程序名
#初始化一个SparkContext，现在sc就是一个SparkContext的实例化对象，然后方可创建RDD。
appName = "average_time"
conf = SparkConf().setAppName(appName).setMaster("spark://116.56.136.75:7077")
#conf = SparkConf().setAppName(appName).setMaster("local")
sc = SparkContext(conf=conf)

#文件路径
inputFiles = "/user/su/su_logs_yiyi2"
outputFile = "/user/INT/ray_logs_yiyi2_result"
#inputFiles = "file:/home/hadoop/input"
#outputFile = "file:/home/hadoop/output"

#生成RDD
raw_data = sc.textFile(inputFiles)

#一些原始数据
users_num = 0
ms_of_hour = 60*60*1000
days_of_log = 100000000.0 / (ms_of_hour * 24)  

#求平均
total_time_per_user = raw_data.map(mapper) \
             .reduceByKey(lambda a, b: a+b) 
users_num = total_time_per_user.count()
average_time = total_time_per_user.map(lambda x: ("all_users", x[1])) \
             .reduceByKey(lambda a, b: a+b) \
	     .map(lambda x: ("average_time", (x[1] / ms_of_hour) / (days_of_log * users_num)))

average_time.saveAsTextFile(outputFile)


