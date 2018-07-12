package io.ideploy.deployment.admin.dao.global;

import io.ideploy.deployment.admin.po.global.GlobalSettingPO;
import io.ideploy.deployment.datasource.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * t_global_setting 表的操作接口
 *
 * @author liuhaihui 2017-01-12
 */
@MyBatisDao
public interface GlobalSettingDao {

	/**
	 * 保存数据
	 *
	 * @param po 要保存的对象
	 */
	void insert(GlobalSettingPO po);


	/**
	 * 修改数据，以主键更新
	 *
	 * @param po - 要更新的数据
	 * @return 更新的行数
	 */
	int update(GlobalSettingPO po);

	/**
	 * 根据主键读取记录
	 */
	GlobalSettingPO get(@Param("settingId") int settingId);

	/**
	 * 查找配置
     */
	List<GlobalSettingPO> findAll();
}
