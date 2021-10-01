package com.procoin.module.circle.entity;

/**
 * Created by zhengmj on 16-3-23.
 */
public enum CircleRoleEnum {
    illegal(-1, "游客"), member(0, "普通成员"), admin(10, "管理员"), root(20, "圈主");
    private final int role;
    private final String roleName;

    CircleRoleEnum(int role, String roleName) {
        this.role = role;
        this.roleName = roleName;
    }

    public int role() {
        return role;
    }

    public static boolean isRootOrAdmin(int role) {
        return root.role() == role || admin.role() == role;
    }

    /**
     * 是圈主
     *
     * @param role
     * @return
     */
    public static boolean isRoot(int role) {
        return root.role() == role;
    }

    /**
     * 是普通成员
     *
     * @param role
     * @return
     */
    public static boolean isMember(int role) {
        return illegal.role() == role;
    }

    /**
     * 是管理员
     *
     * @param role
     * @return
     */
    public static boolean isAdmin(int role) {
        return admin.role() == role;
    }

    /**
     * 是游客
     *
     * @param role
     * @return
     */
    public static boolean isllegal(int role) {
        return illegal.role() == role;
    }

    /**
     * 根据role获取对面的角色名字
     * @param role
     * @return
     */
    public static String getRoleName(int role) {
        for (CircleRoleEnum circleRoleEnum : values()) {
            if (circleRoleEnum.role() == role) {
                return circleRoleEnum.roleName;
            }
        }
        return "";
    }
}
