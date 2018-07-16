package io.ideploy.deployment.admin.configure.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author: code4china
 * @description: ldap配置链接信息
 * @date: Created in 23:24 2018/7/12
 */
@Configuration
@PropertySource("app.properties")
@ConfigurationProperties(prefix = "ldap")
public class LdapConfigVO {

    private String url;

    private String baseDN;

    private String admin;

    private String password;

    private String factory;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBaseDN() {
        return baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }
}
