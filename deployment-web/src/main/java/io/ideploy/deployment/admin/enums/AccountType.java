package io.ideploy.deployment.admin.enums;

/**
 * @author: code4china
 * @description:
 * @date: Created in 00:32 2018/7/13
 */
public enum AccountType {

    SYSTEM_USER((short) 0, "系统内部用户"),
    LDAP_USER((short) 1, "ldap用户"),
    ;

    private short value;

    /**
     * 描述
     */
    private String name;

    AccountType(short value, String name) {
        this.name = name;
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AccountType from(short value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.getValue() == value) {
                return accountType;
            }
        }
        return null;
    }

}
