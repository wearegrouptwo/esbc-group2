#### 一. 需求说明书
######  目的：
一周时间内为实验室14台主机安装Hadoop集群开发环境，为后期搭建分布式系统做准备。
* 为14台主机补充Hadoop依赖环境（Ubuntu, Java， SSH）并注册Hadoop用户。
* 为14台主机安装Hadoop2框架
* 确定主机节点分配，选定NN active， NN standby，RM，gateway，slave（DN，NM），kdc
* 进行Hadoop集群配置
* 验证：关掉一个 slave 后，NN 和 RM 主页面上是否能显示这个节点挂掉
* * *
* 跟踪github上发布的issue，及时反馈进度
* 整合安装文档或安装脚本
######  人员：
第二小组（可爱的组）五名成员
######  环境：
* 硬件环境：   b7-331 71-84号，共14台主机。

* 软件环境：
系统：ubuntu 16.04 LTS
内存：31.3 GiB
处理器：Intel® Xeon(R) CPU E3-1225 v6 @ 3.30GHz × 4 
操作系统类型：64 位
磁盘：61.8 GB
* * *


#### 二. 已经有的知识储备
1. Linux shell基础操作
2. Java语言程序设计基础
3. Http通信协议
4. 数据结构与算法基础
5. MySQL基础、Oracle基础、MongoDB基础
6. Docker、KVM基础
7. 前后端平台开发能力
8. github基础使用
9. md基础使用
10. 分布式系统的概要理解，包括分布式系统的定义和特征： 
    * 组件并发
    * 缺乏全局时钟
    *  组件故障独立
11. 分布式系统的应用场景分类
    * 分布式存储系统
    * 分布式计算系统
    * 分布式管理系统
12. Hadoop分布式框架的概要理解，优点和意义
    * 它主要有以下几个优点：
      **高可靠性**。Hadoop按位存储和处理数据的能力值得人们信赖。
      **高扩展性**。Hadoop是在可用的计算机集簇间分配数据并完成计算任务的，这些集簇可以方便地扩展到数以千计的节点中。
      **高效性**。Hadoop能够在节点之间动态地移动数据，并保证各个节点的动态平衡，因此处理速度非常快。
      **高容错性**。Hadoop能够自动保存数据的多个副本，并且能够自动将失败的任务重新分配。
      **低成本**。与一体机、商用数据仓库以及QlikView、Yonghong Z-Suite等数据集市相比，hadoop是开源的，项目的软件成本因此会大大降低。
     * hadoop大数据处理的意义
Hadoop得以在大数据处理应用中广泛应用得益于其自身在数据提取、变形和加载(ETL)方面上的天然优势。Hadoop的分布式架构，将大数据处理引擎尽可能的靠近存储，对例如像ETL这样的批处理操作相对合适，因为类似这样操作的批处理结果可以直接走向存储。Hadoop的MapReduce功能实现了将单个任务打碎，并将碎片任务(Map)发送到多个节点上，之后再以单个数据集的形式加载(Reduce)到数据仓库里。

* * *
#### 三.需要做的事情
* 在每台机子上完成hadoop的安装创建Hadoop用户
更新apt
安装SSH、配置SSH无密码登陆
安装java环境
安装hadoop-2.6.0-cdh5.13.3.tar.gz
编写安装教程
* 编写角色分配矩阵
* 安装zookeeper
搭建Hadoop分布式集群HA模式准备修改Linux主机名
修改主机名和IP的映射关系
ssh免密码登陆
学习Hadoop分布式集群HA模式部署
安装配置zooekeeper集群（在master，slaver1，slaver2上）
解压tar -zxvf [zookeeper-3.4.5.tar.gz](https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxcheckurl?requrl=http%3A%2F%2Fzookeeper-3.4.5.tar.gz&skey=%40crypt_4cc108d0_1827aa3e578ec3e201ead2fdd89c773a&deviceid=e565601905419813&pass_ticket=undefined&opcode=2&scene=1&username=@fec309f4d1f4a9cff719798ed15c188ef45769aba4b112034b9813c1e437f51d)
修改配置
将配置好的zookeeper拷贝到其他节点
* 安装配置hadoop集群（在slaver4上操作）配置HDFS
修改配置文件修改hadoo-env.sh
修改core-site.xml
修改hdfs-site.xml
修改mapred-site.xml
修改yarn-site.xml
修改slaves
* 启动启动zookeeper集群
启动journalnode，为hadoop提供元数据管理（edits）
格式化HDFS
格式化ZKFC
启动HDFS(在slaver3上执行)
启动YARN
* 验证HDFS HA：
* 验证YARN
* 测试集群工作状态的一些指令

* * *
#### 四. 安装文档或安装脚本
#Hadoop HA 集群搭建教程



###在Ubuntu系统下创建Hadoop用户

```shell
sudo useradd -m hadoop -s /bin/bash   #创建了可以登陆的 hadoop 用户，并使用 /bin/bash 作为 shell
sudo passwd hadoop   #设置密码，可简单设置为 hadoop，按提示输入两次密码
sudo adduser hadoop sudo    #为 hadoop 用户增加管理员权限，方便部署
```

最后注销当前用户（点击屏幕右上角的齿轮，选择注销），在登陆界面使用刚创建的 hadoop 用户进行登陆 .

用 hadoop 用户登录后，我们先更新一下 apt，后续我们使用 apt 安装软件，如果没更新可能有一些软件安装不了。按 ctrl+alt+t 打开终端窗口，执行如下命令： 

```shell
sudo apt-get update
```

如果出现错误：无法获得锁 /var/lib/dpkg/lock - open (11: 资源暂时不可用)；则执行下面命令

```shell
sudo rm /var/cache/apt/archives/lock
sudo rm /var/lib/dpkg/lock
```



###安装Java 8环境

```shell
sudo add-apt-repository ppa:webupd8team/java    #添加ppa
sudo apt-get update
sudo apt-get install oracle-java8-installer    #安装oracle-java-installer
sudo update-java-alternatives -s java-8-oracle   #设置系统默认jdk
which java    #检查java是否正确安装
vim ~/.bashrc #在首行加入java 安装地址    export JAVA_HOME = /usr/lib/jvm/java-8-oracle
source ~/.bashrc    # 使变量设置生效
source /etc/profile #使变量设置生效  
echo $JAVA_HOME     # 检验变量值
java -version   #java安装测试
javac -version
```



###安装Hadoop  2.6.0

根据老师的网址  http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.13.3.tar.gz     直接从浏览器下载Hadoop即可

```shell
sudo tar -zxf ~/下载/hadoop-2.6.0-cdh5.13.3.tar.gz  -C /usr/local    # 解压到/usr/local中
cd /usr/local/
sudo mv ./hadoop-2.6.0-cdh5.13.3/ ./hadoop            # 将文件夹名改为hadoop
sudo chown -R hadoop ./hadoop       # 修改文件权限  -R表示递归，该文件下所有文件都赋予hadoop用户的权限
# Hadoop 解压后即可使用。输入如下命令来检查 Hadoop 是否可用，成功则会显示 Hadoop 版本信息
cd /usr/local/hadoop
./bin/hadoop version
```

Hadoop安装目录下各目录解释

1、sbin目录：存放启动或停止hadoop相关服务的脚本

2、bin目录：存放对hadoop相关服务（HDFS,YARN）进行操作的脚本

3、etc目录：hadoop的配置文件目录，存放hadoop的配置文件

4、share目录：存放hadoop的依赖jar包和文档，文档可以被删除掉

5、lib目录：存放hadoop的本地库（对数据进行压缩解压缩功能



###安装SSH、配置SSH无密码登陆

保证resource manager角色和namenode角色的机器能够免密访问到datanode机器，需要配置ssh

下面是master节点生成公匙并加入授权的过程 

```shell
sudo apt-get install openssh-server   # Ubuntu 默认已安装了 SSH client，此外还需要安装 SSH server
ssh localhost    #登陆本机  SSH首次登陆提示时，输入 yes 。然后按提示输入密码 hadoop 即可登陆至本机

#这样登陆是需要每次输入密码的，需要配置成SSH无密码登陆比较方便
exit                           # 退出刚才的 ssh localhost
cd ~/.ssh/                     # 若没有该目录，请先执行一次ssh localhost
ssh-keygen -t rsa              # 会有提示，都按回车就可以
cat ./id_rsa.pub >> ./authorized_keys  # 加入授权
```

需要免密访问其他slave，要将master 公匙传输到 slave 节点、在 slave 节点上加入授权 

在master节点上执行

```shell
scp ~/.ssh/id_rsa.pub hadoop@slave1:/home/hadoop/  #将master上的公钥传输到slave的指定位置，注意要先在/etc/hosts内指定slave1的ip地址
```

在slave1节点上执行

```shell
mkdir ~/.ssh       # 如果不存在该文件夹需先创建，若已存在则忽略
cat ~/id_rsa.pub >> ~/.ssh/authorized_keys  #加入授权
rm ~/id_rsa.pub    # 用完就可以删掉了
```

在master节点上执行

```shell
ssh slave1  #验证是否能够登陆salve1  
```



### 修改hosts配置文件

打开/etc/hosts ，给**所有**机器添加上映射关系，下面是样例

```shell
#添加相应是机器ip和主机名
10.x.xx.80  hadoop100  #NameNode、DFSZKFailoverController(ZKFC)
10.x.xx.81  hadoop101  #NameNode、DFSZKFailoverController(ZKFC)
10.x.xx.82  hadoop102  #ResourceManager
10.x.xx.83  hadoop103  #ResourceManager
10.x.xx.84  hadoop104  #DataNode、NodeManager、JournalNode、QuorumPeerMain、ZooKeeper
10.x.xx.85  hadoop105  #DataNode、NodeManager、JournalNode、QuorumPeerMain、ZooKeeper
10.x.xx.86  hadoop106  #DataNode、NodeManager、JournalNode、QuorumPeerMain、ZooKeeper
10.x.xx.87  hadoop107  #DataNode、NodeManager
```



###Zookeeper安装

zookeeper作用是负责HDFS中NameNode主备节点的选举。当Active NameNode挂掉了，会自动切换Standby NameNode为Active状态。 

从zookeeper的官网(http://www.apache.org/dist/zookeeper/)中选择想要安装的版本下载到本地，解压到地址/usr/local下并将文件夹改名为zookeeper。

在/usr/local/zookeeper/conf/目录下，有zoo_sample.cfg 文件，这是zookeeper配置文件的样例，复制一份一样的样本

```shell
cp zoo_sample.cfg zoo.cfg
vim zoo.cfg
```

 对应的内容解释为

```shell
tickTime=2000 #（是Zookeeper独立的工作时间单元）  
dataDir=/usr/local/zookeeper/tmp	#（存储数据的地址）  
clientPort=2181	#（2181是经常的选择，此处是关于用户和Zookeeper相连的地方）  
initLimit=5    #这个配置项是用来配置 Zookeeper 接受客户端（这里所说的客户端是Zookeeper服务器集群中连接到 Leader的Follower服务器）初始化连接时最长能忍受多少个心跳时间间隔数。当已经超过5个心跳的时间（也就是 tickTime）长度后 Zookeeper服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 5*2000=10 秒 
syncLimit=2    #这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 2*2000=4 秒 
server.A=B：C：D：#其中 A 是一个数字，表示这个是第几号服务器；B 是这个服务器的 ip 地址；C 表示的是这个服务器与集群中的 Leader 服务器交换信息的端口；D 表示的是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是用来执行选举时服务器相互通信的端口。
```

在exitdataDir所定义的目录(/usr/local/zookeeper/tmp)下新建myid文件，填入对应的数字(A的内容)

在每台安装zookeeper后的机器上启动zookeeper服务

```shell
cd /usr/local/zookeeper/bin #进入安装目录下的bin文件夹
./zkServer.sh start  #启动服务
./zkServer.sh status  #查看状态
./zkServer.sh stop  #关闭服务
```



###配置Hadoop

进入安装目录/usr/local/hadoop/etc/hadoop下对几个重要文件进行配置，注意所有机器上的etc目录下的文件保持一致

- hadoop-env.sh

```shell
JAVA_HOME=/usr/lib/jvm/java-8-oracle #改成自己的安装路径
```

- core-site.xml

```xml
<configuration>

    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://cluster1</value>
    </property>
    【这里的值指的是默认的HDFS路径。当有多个HDFS集群同时工作时，集群名称在这里指定,cluster1即为集群名字,该值要与hdfs-site.xml中的配置保持一致！】

    <property>
        <name>hadoop.tmp.dir</name>
        <value>/usr/local/hadoop/tmp</value>
    </property>
    【这里的路径默认是NameNode、DataNode、JournalNode等存放数据的公共目录。用户也可以自己单独指定这三类节点的目录。】

    <property>
        <name>ha.zookeeper.quorum</name>
        <value>hadoop104:2181,hadoop105:2181,hadoop106:2181</value>
    </property>
    【这里是ZooKeeper集群的地址和端口。注意，数量一定是奇数，且不少于三个节点】

</configuration>
```

- #### hdfs-site.xml

```xml
<configuration>

    <property>
        <name>dfs.replication</name>
        <value>3</value>
    </property>
    【指定DataNode存储block的副本数量。默认值是3个，该值不大于节点数量即可。】

    <property>
        <name>dfs.nameservices</name>
        <value>cluster1</value>
    </property>
    【使用federation时，HDFS集群别名,该名字与core-site.xml保持一致】

    <property>
        <name>dfs.ha.namenodes.cluster1</name>
        <value>hadoop100,hadoop101</value>
    </property>
    【指定该集群的namenode的机器】

    <property>
        <name>dfs.namenode.rpc-address.cluster1.hadoop100</name>
        <value>hadoop100:9000</value>
    </property>
    【指定hadoop100的RPC地址】
    <property>
        <name>dfs.namenode.http-address.cluster1.hadoop100</name>
        <value>hadoop100:50070</value>
    </property>
    【指定hadoop100的http地址】

    <property>
        <name>dfs.namenode.rpc-address.cluster1.hadoop101</name>
        <value>hadoop101:9000</value>
    </property>
    【指定hadoop101的RPC地址】

    <property>
        <name>dfs.namenode.http-address.cluster1.hadoop101</name>
        <value>hadoop101:50070</value>
    </property>
    【指定hadoop101的http地址】

    <property>
        <name>dfs.namenode.shared.edits.dir</name>
        <value>qjournal://hadoop104:8485;hadoop105:8485;hadoop106:8485/cluster1
        </value>
    </property>
    【指定该集群的两个NameNode共享edits文件目录时，使用的JournalNode集群信息】

    <property>
        <name>dfs.ha.automatic-failover.enabled.cluster1</name>
        <value>true</value>
    </property>
    【指定该集群是否启动自动故障恢复，即当NameNode出故障时，是否自动切换到另一台NameNode】

    <property>
        <name>dfs.client.failover.proxy.provider.cluster1</name>
        <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider
        </value>
    </property>
    【指定该集群出故障时，哪个实现类负责执行故障切换】

    <property>
        <name>dfs.ha.fencing.methods</name>
        <value>sshfence</value>
    </property>
    【一旦需要NameNode切换，使用ssh方式进行操作】

    <property>
        <name>dfs.ha.fencing.ssh.private-key-files</name>
        <value>/hadoop/.ssh/id_rsa</value>
    </property>
    【如果使用ssh进行故障切换，使用ssh通信时用的密钥存储的位置】

    <property>
        <name>dfs.ha.fencing.ssh.connect-timeout</name>
        <value>30000</value>
    </property>
    【connect-timeout连接超时】

</configuration>
```

- #### mapred-site.xml

  ```xml
  <configuration>
      <property>
          <name>mapreduce.framework.name</name>
          <value>yarn</value>
      </property>
      【指定运行mapreduce的环境是yarn，与hadoop1截然不同的地方】
  </configuration>
  ```

- #### yarn-site.xml

  ```xml
  <configuration>
  
      <property>
          <name>yarn.resourcemanager.ha.enabled</name>
          <value>true</value>
      </property>
      【启动HA高可用性】
  
      <property>
          <name>yarn.resourcemanager.cluster-id</name>
          <value>yrc</value>
      </property>
      【指定resourcemanager的名字】
  
      <property>
          <name>yarn.resourcemanager.ha.rm-ids</name>
          <value>rm1,rm2</value>
      </property>
      【使用了2个resourcemanager,分别指定Resourcemanager的地址】
  
      <property>
          <name>yarn.resourcemanager.hostname.rm1</name>
          <value> hadoop102</value>
      </property>
      【自定ResourceManager1的地址】
  
      <property>
          <name>yarn.resourcemanager.hostname.rm2</name>
          <value> hadoop103</value>
      </property>
      【自定ResourceManager2的地址】
  
      <property>
          <name>yarn.resourcemanager.zk-address</name>
          <value>hadoop104:2181,hadoop105:2181,hadoop106:2181</value>
      </property>
      【制定Zookeeper机器】
      
      <property>
          <name>yarn.nodemanager.aux-services</name>
          <value>mapreduce_shuffle</value>
      </property>
      【默认】
      
  </configuration>
  ```

- #### slave

  ```xml
  hadoop104
  hadoop105
  hadoop106
  hadoop107
  ```



###启动过程（首次启动）

1.启动zookeeper集群 

```shell
/usr/local/zookeeper/bin/zkServer.sh start
/usr/local/zookeeper/bin./zkServer.sh status

#验证
jps
#显示QuorumpeerMain
```

2.启动journalnode (首次启动时，先要在journalnode机器上启动服务)

```shell
#启动
/usr/local/hadoop/sbin/hadoop-daemon.sh start journalnode

#验证
jps
#显示JouralNode
```

3.格式化namenode 

```shell
/usr/local/hadoop/binhdfs namenode -format
```

4.格式化ZKFC (ZookeeperFailoverController )

```shell
/usr/local/hadoop/bin/hdfs zkfc -formatZK
```

5.NameNode从namenode active同步到namenode standby

```shell
/usr/local/hadoop/bin/hdfs namenode -bootstrapstandby
```

6.启动NameNode和DataNode （在NameNode上）

```shell
#启动
/usr/local/hadoop/sbin/start-dfs.sh

#验证
jps
#NameNode主机上显示NameNode
```

7.启动yarn （在分配了ResourceManager角色的机器上）

```shell
#启动
/usr/local/hadoop/sbin/start-yarn.sh

#验证：
jps
#ResourceManager主机显示ResourceManager，其他机器显示NodeManager
```

8.启动ZookeeperFailoverController 

```shell
#启动
/usr/local/hadoop/sbin/hadoop-daemon.sh start zkfc

#验证
jps

#显示DFSZKFailoverController
```



###检验

数据节点检查：打开本机浏览器输入    本机ip:50070     即可查看namenode节点和datanode节点的状态

ResourceManager检查：在设置为ResourceManager的机器上打开浏览器输入    本机ip:8088     即可查看各个节点的状态





###参考资料

**单机版安装教程**

http://www.powerxing.com/install-hadoop/

**集群安装教程**

http://www.powerxing.com/install-hadoop-cluster/

**JAVA 8安装教程**

https://blog.csdn.net/williamyi96/article/details/78268595

**Hadoop HA + ZooKeeper**

https://blog.csdn.net/ltliyue/article/details/51144381

**Hadoop HA + NFS**

https://blog.csdn.net/lovebyz/article/details/51233972
