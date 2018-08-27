# !/bin/bash
# 功能：包含编译、启动
#
. /etc/profile

TEMP_DIR=/data0/temp/ideploy
DEPLOY_DIR=/data0/project/ideploy/deployment-web

#JAVA_OPTS="-Dlog4j.configuration=file:/data0/log4j.properties"
#JAVA_OPTS="${JAVA_OPTS} -Dspring.config.location=/data0/application.properties"

# 1. checkout 代码
rm -rf $TEMP_DIR
echo "开始checkout代码"
git clone https://github.com/oldDiverInGZ/ideploy.git $TEMP_DIR > /dev/null

if [ $? -ne 0 ]; then
	echo "checkout代码失败"
	exit 1
fi
# 2.开始编译
cd $TEMP_DIR
echo "开始编译代码"
mvn -P=dev -Dmaven.test.skip=true -U clean install > complie.log 2>&1
if [ $? -ne 0 ]; then
        echo "编译代码失败"
	exit 1
fi
echo "编译完成"

# 迁移jar包
if [ -f "${DEPLOY_DIR}" ];then
  rm -f ${DEPLOY_DIR}
fi
mkdir -p ${DEPLOY_DIR}
cp ${TEMP_DIR}/deployment-web/target/deployment-web.war ${DEPLOY_DIR}

# 3.重启
echo "开始部署....."
killTimes=3
# 循环kill -15 3次，否则直接kill -9 
echo "开始停止进程....."
pId=$(ps -ef | grep deployment-web.war | grep -v grep | awk '{print $2}')
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
cd ${DEPLOY_DIR}
java $JAVA_OPTS -jar ${DEPLOY_DIR}/deployment-web.war>app.log 2>&1 &

#删除临时目录
rm -rf ${TEMP_DIR}

echo "启动完成"
