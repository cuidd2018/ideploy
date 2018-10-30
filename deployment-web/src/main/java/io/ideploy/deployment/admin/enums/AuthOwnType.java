package io.ideploy.deployment.admin.enums;

/**
 * @author: code4china
 * @description:
 * @date: Created in 16:12 2018/10/25
 */
public enum AuthOwnType {

    USER((short) 0, "私有"),
    COMMON((short) 1, "公共"),
    ;

    private short value;

    /**
     * 描述
     */
    private String name;

    AuthOwnType(short value, String name) {
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
