package io.ideploy.deployment.admin.service.account;

import io.ideploy.deployment.admin.vo.account.AdminOperationLog;

/**
 * 管理员操作日志 <br>
 * 的操作接口
 * 
 * @author ten 2015-08-05
 */
public interface AdminOperationLogService {

	/**
	 * 保存数据
	 * 
	 * @param dto 对象
	 */
	public void save(AdminOperationLog dto);

}
