package com.hints.authserver.model;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.springframework.security.core.GrantedAuthority;

@Data
@Table("sys_role")
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Id
    private Long role_id;
    /** 角色名称 */
    @Name
    private String role_name;
    /** 角色权限 */
    private String role_key;
    /** 角色排序 */
    private String role_sort;
    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
    private String data_scope;
    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menu_check_strictly;
    /** 角色状态（0正常 1停用） */
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String del_flag;
    /** 用户是否存在此角色标识 默认不存在 */
    private boolean flag = false;
    /** 菜单组 */
    private Long[] menu_ids;
    /** 部门组（数据权限） */
    private Long[] dept_ids;

    @Override
    public String getAuthority() {
        return role_id.toString();
    }

    @Override
    public String toString() {
        return role_id.toString();
    }
}