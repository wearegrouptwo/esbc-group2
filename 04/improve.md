一.调优效果
- 原本上限: 3.2万条/秒;
- 调优后上限: 4.5万条/秒。

二.调优的普遍测略
- 1.代码调优;
- 2.调整spark,kafka等的相关参数;
- 3.其他

三.我们的调优方法
- 我们组主要采取调整参数的方法，对spark-env.sh, spark-defaults.conf以及kafka的partirion进行调整;
- 对于spark-env.sh：
	- 将export SPARK_WORKER_CORES=1,改为export SPARK_WORKER_CORES=4;
- 对于spark-defaults.conf：
	- 添加如下参数以及其值：
#
		spark.default.parallelism	1000
		spark.shuffle.file.buffer	64k
		spark.reducer.maxSizeInFlight 	96m

#
		spark.executor.instances	64
		spark.executor.cores		1
		spark.executor.memory		2g
		spark.driver.memory		4g
#
<br>(一般对于spark.streaming.kafka.maxRatePerPartition，这个参数 × topic的partition略大于Input Rate较好)		
#
		spark.streaming.kafka.maxRatePerPartition 	22500
#

- 对于kafka:
	- 新建topic，将partition设定为2。
<br>(我尝试过设定partirion为4，效果非常差，而且情况非常奇怪，Input Rate为0都会炸掉)
