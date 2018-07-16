package io.ideploy.deployment.admin.service.server.impl;

import io.ideploy.deployment.admin.DeployWebApplication;
import io.ideploy.deployment.admin.service.account.LdapAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: code4china
 * @description:
 * @date: Created in 12:54 2018/7/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeployWebApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class LdapAccountServiceImplTest {

    @Autowired
    private LdapAccountService ldapAccountService;

    @Test
    public void testLogin(){
        ldapAccountService.login("lidongyao", "123321456");
    }

}
