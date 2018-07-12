package io.ideploy.deployment.admin.dao.project;

import org.apache.ibatis.annotations.Param;

import io.ideploy.deployment.admin.po.project.ProjectModulePO;
import io.ideploy.deployment.datasource.MyBatisDao;

import java.util.List;

/**
 * t_project_module 表的操作接口
 * 
 * @author liuhaihui 2017-01-12
 */
@MyBatisDao
public interface ProjectModuleDao {

	/**
	 * 保存数据
	 * 
	 * @param po 要保存的对象
	 */
	void insert(ProjectModulePO po);


	/**
	 * 修改数据，以主键更新
	 * 
	 * @param po - 要更新的数据
	 * @return 更新的行数
	 */
	int update(ProjectModulePO po);

	/**
	 * 根据主键读取记录
	 */
	ProjectModulePO get(@Param("moduleId") int moduleId);

	/**
	 * 根据项目名查找模块
     */
	List<ProjectModulePO> getByProjectId(@Param("projectId")int projectId);

	/**
	 * 根据项目ids查找模块
     */
	List<ProjectModulePO> getByProjectIds(@Param("projectIds")List<Integer> projectIds);

	/**
	 *  根据moduleId 删除
     */
	int deleteModule(@Param("moduleId")int moduleId);

	/**
	 * 根据主键读取记录
	 */
	List<ProjectModulePO> getByModuleIds(@Param("moduleIds") List<Integer>  moduleIds);
}
