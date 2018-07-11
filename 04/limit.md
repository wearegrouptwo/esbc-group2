一.测试出的上限<br>
  上限：3.2万条/秒

二.基本概念：<br>
-1.Batch:<br>
    - Spark Streaming是以batch的形式进行处理的，batch的划分根据“处理间隔”，这个是由用户设定的。<br>
    *Batch分为提交和处理两个步骤;<br>
  2.处理间隔：<br>
    初始化的时候，StreamingContext(sc, 处理间隔)。<br>
    如果上一个batch的处理时间大于处理间隔，将影响下一个batch的提交;
  3.Input Rate
    Streaming读入数据的速度;
  4.Processing Time
    处理每个batch所花费的时间，streaming UI中一个重要指标;
  5.Scheduling Delay
    当前的batch B,因为上一个batch A的处理时间大于“处理间隔"，因而相对计划提交时间的delay提交。
      
三.测试方法
  1.提交代码，访问这个地址：主机ip:4040，进入spark UI，点击Streaming条目进入其监控页面;
  2.在生成日志的代码中，编写成可以控制每秒生成条数的形式，而在uv处理的代码中，把Streaming处理间隔设定为1秒，
这样方便控制变量，以及计算结果。  
  3.观察Input Rate，Processing Time，Scheduling Delay，Total Delay三个曲线图，以及其中Avg示数，
以前三个指标为主。
  4.首先对比一下Input Rate，与自己在生成日志代码中设定的速度死否一致;
  5.主要观察Processing Time，Scheduling Delay这两项指标，
    如果Processing Time的Avg值，超过处理间隔，Scheduling Delay的曲线图和Avg值会持续上涨。
    Processing Time主要观察Avg值，因为我猜测是由于网络原因或者其他，有时其会变得很大。
    Scheduling Delay的Avg值不接近0，也是正常的，主要观察其曲线，和下面Completed Batches中的整体状况。
  其中有较大部分在0～1ms，然后由于Processing Time的抖动而升高的值也能衰减回来就可以。
  6.根据上述观察经验，使用区间压缩的方法，不断调整Input Rate，即可得出处理上限。
    我的测试过程大概是这样的：
    区间1：1千条/秒，1万条/秒，5万条/秒
    区间2：2万条/秒，3万条每秒，3.5万条/秒，4万条每秒
    区间3：3.2万条/秒，3.3万条/秒，3.4万条/秒
  7.最后测定出是3.2万条/秒。
  其实3.3，3.4的Processing Time，运行久了之后，其Avg也开始与处理间隔相差不大，
  而且3.3万甚至比3.5万效果要差，我猜测是网络状况导致的，网络状况改善(例如晚上),可能会可行。
