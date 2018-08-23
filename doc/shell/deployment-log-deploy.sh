# !/bin/bash
# 功能：包含编译、启动
#
. /etc/profile

# 1. checkout 代码
rm -rf /data/temp/ideploy
echo "开始checkout代码"
git clone https://github.com/oldDiverInGZ/ideploy.git /data/temp/ideploy/ > /dev/null

if [ $? -ne 0 ]; then
	echo "checkout代码失败"
	exit 1
fi
# 2.开始编译
cd /data/temp/ideploy/
echo "开始编译代码"
mvn -P=test -Dmaven.test.skip=true -U clean install > /dev/null
if [ $? -ne 0 ]; then
        echo "编译代码失败"
	exit 1
fi
echo "编译完成"

# 迁移jar包
cp /data/temp/ideploy/deployment-log/target/deployment-log.jar /data/project/ideploy/deployment-log

# 3.重启
echo "开始部署....."
killTimes=3
# 循环kill -15 3次，否则直接kill -9 
echo "开始停止tomcat....."
pId=$(ps -ef | grep deployment-log.jar | grep -v grep | awk '{print $2}')
while [ $killTimes -ge 0 ]; do
	ps -ax | awk '{ print $1 }' | grep -e "^$pId$"
        if [ $? -ne 0 ]; then
		break
	fi
	
	kill -15 $pId >/dev/null 2>&1
	if [ $killTimes -gt 0 ]; then
          sleep 10
        fi
	# 强kill
	ps -ax | awk '{ print $1 }' | grep -e "^$pId$"
	if [ $? -ne 0 ]; then
		    sleep 10
		else 
		    break	
	fi
	if [ $killTimes -eq 0 ]; then
		kill -9 $pId
	fi
	killTimes=`expr $killTimes - 1 `
done

export JAVA_OPTS="$JAVA_OPTS"
echo "开始启动...."

#启动
cd /data/project/ideploy/deployment-log/
java $JAVA_OPTS -jar /data/project/ideploy/deployment-log/deployment-log.jar>app.log 2>&1 &

#删除临时目录
rm -rf /data/temp/ideploy

echo "启动完成"
