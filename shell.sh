#!/bin/sh
#先什么都不判断了
#1.创建hadoop用户 
	echo ">>>1.创建hadoop用户"     
	
	#hadoop帐号	
	user="1hadoop"
	#默认密码
	pwd="hadoop"	
	
	#create group if not exists  
	egrep "^$user" /etc/passwd >& /dev/null  
	if [ $? -ne 0 ]; then  
		echo ">>>新建hadoop用户并给予管理员权限"     	
		sudo  useradd -m $user -s /bin/bash	
		sudo  adduser $user sudo
					
		#询问用户是否使用默认密码
		echo ">>>修改hadoop用户密码"		
		echo ">>>请问是否对hadoop用户采用默认密码(Y/N)?"
		read flag_pwd_moren
		while [ "$flag_pwd_moren" != "Y" ] && [ "$flag_pwd_moren" != "N" ]; do
			echo ">>>请问是否对hadoop用户采用默认密码(Y/N)?"
			read flag_pwd_moren
		done
		
		
		#设置密码		
		if [ "$flag_pwd_moren" == "Y" ];then
			echo ">>>为hadoop用户设置默认密码"
			(echo $pwd;sleep 1;echo $pwd) | sudo passwd $user > /dev/null
		else
			sudo passwd $user
		
		fi
		
		echo ">>>1.2 完成"
	
	else
		echo ">>>你已经创建hadoop用户"

	fi  
	
	echo ">>>1.创建hadoop用户"
#1.创建hadoop用户 


#2.安装java环境完成
	echo "2.安装java环境"		
	JAVA_VERSION=`java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`
	if [ -z $JAVA_VERSION ]; then
		echo ">>>添加ppa源"
		sudo add-apt-repository ppa:webupd8team/java
		sudo apt-get update
		echo ">>>安装oracle-java-installer"
		sudo apt-get install oracle-java8-installer
		echo ">>>设置系统默认jdk"
		sudo update-java-alternatives -s java-8-oracle
		echo ">>>添加系统变量JAVA_HOME"
		echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle" >>  ~/.bashrc
		source ~/.bashrc
	
	else
		if [ "$JAVA_VERSION" != "1.8.0_171" ]; then
			#询问用户是否安装推荐版本的java		
			echo ">>>你电脑上的java版本并不是建议版本，请问是否安装指定版本[Y/N]？"	
			read flag_java
			while [ $flag_java != "Y" ] && [ $flag_java != "N" ]; do
				echo ">>>你电脑上的java版本并不是建议版本，请问是否安装指定版本[Y/N]？"				
				read flag_java
			done
		
			#安装java8
			if [ flag_java == "Y" ]; then
				echo ">>>添加ppa源"
				sudo add-apt-repository ppa:webupd8team/java
				sudo apt-get update
				echo ">>>安装oracle-java-installer"
				sudo apt-get install oracle-java8-installer
				echo ">>>设置系统默认jdk"
				sudo update-java-alternatives -s java-8-oracle
				echo ">>>添加系统变量JAVA_HOME"
				echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle" >>  ~/.bashrc
				source ~/.bashrc
			fi
		fi
	fi	
	echo "2.安装java环境完成"
#2.安装java环境完成


#3.安装hadoop
	echo "3.安装hadoop"	
	#判断hadoop是否存在
	flag_h="N"	
	folder="/usr/local/hadoop/"
	if [ -d $folder ]; then
		flag_h="Y"
	fi
	
	#如果不存在直接下载吧，否则询问
	if [ $flag_h == "N" ]; then
		
		username=$(echo "$USER")
		f_h="/home/"$username"/下载/hadoop-2.6.0-cdh5.13.3.tar.gz"
		if [ ! -f "$f_h" ]; then
			echo ">>>正在下载hadoop"
			sudo wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.13.3.tar.gz 
			sudo mv hadoop-2.6.0-cdh5.13.3.tar.gz /home/"$username"/下载
		fi		
		
		echo ">>>正在安装hadoop"
		sudo tar -zxf ~/下载/hadoop-2.6.0-cdh5.13.3.tar.gz -C /usr/local
		cd /usr/local/
		sudo mv ./hadoop-2.6.0-cdh5.13.3/ ./hadoop
		sudo chown -R hadoop ./hadoop
	
	else
		#获取Hadoop版本并判断
		cd /usr/local/hadoop
		v=$( ./bin/hadoop version | sed -n '1p;1q')	
		if [ "$v" != "Hadoop 2.6.0-cdh5.13.3" ]; then
			#询问用户是否安装推荐版本的hadoop	
			echo ">>>你电脑上的java版本并不是建议版本，请问是否安装指定版本[Y/N]？"	
			read flag_h2
			while [ $flag_h2= "Y" ] && [ $flag_h2= "N" ]; do
				echo ">>>你电脑上的java版本并不是建议版本，请问是否安装指定版本[Y/N]？"	
				read flag_h2
			done
		
			#安装hadoop
			if [ $flag_h2 == "Y" ]; then
				
				#删除原本的hadoop				
				echo ">>>删除原本的hadoop"
				sudo rm -r /usr/local/hadoop
				
				echo ">>>安装新的hadoop"				
				username=$(echo "$USER")
				f_h="/home/"$username"/下载/hadoop-2.6.0-cdh5.13.3.tar.gz"
				if [ ! -f "$f_h" ]; then
					echo ">>>正在下载hadoop"
					sudo wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.13.3.tar.gz 
					sudo mv hadoop-2.6.0-cdh5.13.3.tar.gz /home/"$username"/下载
				fi		
		
				echo ">>>正在安装hadoop"
				sudo tar -zxf ~/下载/hadoop-2.6.0-cdh5.13.3.tar.gz -C /usr/local
				cd /usr/local/
				sudo mv ./hadoop-2.6.0-cdh5.13.3/ ./hadoop
				sudo chown -R hadoop ./hadoop
			
			fi
		fi
	fi	
	echo "3.安装hadoop完成"
#3.安装hadoop


#4.ssh
	echo "4.安装ssh"		
	sudo apt-get install openssh-server
	cd ~/.ssh/ # 若没有该目录,请先执行一次ssh localhost
	ssh-keygen -t rsa # 会有提示,都按回车就可以
	cat ./id_rsa.pub >> ./authorized_keys
	echo "4.安装ssh完成"	
#4.ssh
