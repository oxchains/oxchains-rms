package com.oxchains.rmsuser.dao;

import com.oxchains.rmsuser.entity.RolePermission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2017-12-12 17:10
 * @name UserRepo
 * @desc:
 */
@Repository
public interface RolePermissionRepo extends CrudRepository<RolePermission,Long> {

    List<RolePermission> findByRoleId(Long roleId);
    RolePermission findByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
