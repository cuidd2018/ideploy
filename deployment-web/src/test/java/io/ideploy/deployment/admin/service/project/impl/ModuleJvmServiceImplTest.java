package io.ideploy.deployment.admin.service.project.impl;

import com.google.common.collect.Lists;
import io.ideploy.deployment.admin.dao.project.ProjectModuleDao;
import io.ideploy.deployment.admin.po.project.ProjectModulePO;
import io.ideploy.deployment.admin.service.project.ModuleJvmService;
import io.ideploy.deployment.admin.vo.project.ModuleJvm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * 详情 :
 * <p>
 * 详细 :
 *
 * @author K-Priest 17/2/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
@Profile("dev")
public class ModuleJvmServiceImplTest {

    @Autowired
    private ModuleJvmService moduleJvmService;

    @Autowired
    private ProjectModuleDao projectModuleDao;

    @Test
    public void testBatchInsert() {
        ModuleJvm moduleJvm01 = new ModuleJvm();
        moduleJvm01.setJvmArgs("-Xms180m -Xmx180m");
        moduleJvm01.setEnvId(1);
        moduleJvm01.setEnvName("dev");
        moduleJvm01.setModuleId(1);

        ModuleJvm moduleJvm02 = new ModuleJvm();
        moduleJvm02.setJvmArgs("-Xms190m -Xmx190m");
        moduleJvm02.setEnvId(2);
        moduleJvm02.setEnvName("test");
        moduleJvm02.setModuleId(2);

        ArrayList<ModuleJvm> moduleJvmArgses = Lists.newArrayList(moduleJvm01, moduleJvm02);
        moduleJvmService.saveModuleJvm(moduleJvmArgses);
    }

    @Test
    public void test() {
        ProjectModulePO projectModulePO = projectModuleDao.get(14);
        projectModulePO.setModuleNameZh("测试");
        projectModuleDao.update(projectModulePO);

    }
}
