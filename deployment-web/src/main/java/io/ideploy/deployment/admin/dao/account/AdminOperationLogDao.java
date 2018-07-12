package io.ideploy.deployment.admin.dao.account;

import io.ideploy.deployment.admin.po.account.AdminOperationLogPO;
import io.ideploy.deployment.datasource.MyBatisDao;

/**
 * mz_admin_operation_log 表的操作接口
 * 
 * @author ten 2015-08-05
 */
@MyBatisDao
public interface AdminOperationLogDao {

	/**
	 * 保存数据
	 * 
	 * @param po 对象
	 */
	public void save(AdminOperationLogPO po);

}
