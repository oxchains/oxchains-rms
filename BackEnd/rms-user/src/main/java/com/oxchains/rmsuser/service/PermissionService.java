package com.oxchains.rmsuser.service;

import com.oxchains.rmsuser.common.RestResp;
import com.oxchains.rmsuser.dao.PermissionRepo;
import com.oxchains.rmsuser.dao.RolePermissionRepo;
import com.oxchains.rmsuser.dao.UserRoleRepo;
import com.oxchains.rmsuser.entity.Permission;
import com.oxchains.rmsuser.entity.RolePermission;
import com.oxchains.rmsuser.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author luoxuri
 * @create 2018-01-10 11:08
 **/
@Service
public class PermissionService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Resource private PermissionRepo permissionRepo;
    @Resource private UserRoleRepo userRoleRepo;
    @Resource private RolePermissionRepo rolePermissionRepo;

    public RestResp addURI(Permission permission){
        try {
            String name = permission.getName();
            String uri = permission.getUrl();
            Permission permission1 = permissionRepo.findByUrl(uri);
            if (permission1 != null){
                return RestResp.fail("URI已存在");
            }
            Permission permission2 = new Permission(name, null, uri);
            Permission p = permissionRepo.save(permission2);
            return RestResp.success("操作成功", p);
        } catch (Exception e){
            LOG.error("add url failed: {}", e.getMessage(), e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp deleteURI(Long permissionId){
        try {
            permissionRepo.delete(permissionId);
            return RestResp.success("操作成功", permissionId);
        } catch (Exception e){
            LOG.error("delete URI failed: {}", e.getMessage(), e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp updateURI(Long permissionId, Permission permission){
        try {
            String name = permission.getName();
            String uri = permission.getUrl();
            Permission permission1 = permissionRepo.findOne(permissionId);
            permission1.setName(name);
            permission1.setUrl(uri);
            Permission permission2 = permissionRepo.save(permission1);
            return RestResp.success("操作成功", permission2);
        } catch (Exception e){
            LOG.error("update URI failed: {}", e.getMessage(), e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryAllURI(){
        try {
            Iterator<Permission >it = permissionRepo.findAll().iterator();
            if (it.hasNext()){
                return RestResp.success("操作成功", it);
            }
            return RestResp.success("操作成功", new ArrayList<>());
        } catch (Exception e){
            LOG.error("query all URI failed: {}", e.getMessage(), e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp authURI(Long permissionId, Long toId){
        try {
            List<UserRole> userRoleList = userRoleRepo.findByUserId(toId);
            Set<Long> roleSet = new HashSet<>();
            userRoleList.stream().forEach(ur -> {
                Long roleId = ur.getRoleId();
                roleSet.add(roleId);
            });

            Iterator<Long> iterator = roleSet.iterator();
            RolePermission rolePermission1 = new RolePermission();
            while (iterator.hasNext()){
                Long roleId = iterator.next();
                RolePermission rolePermission = rolePermissionRepo.findByRoleIdAndPermissionId(roleId, permissionId);
                if (rolePermission == null){
                    rolePermission1.setRoleId(roleId);
                    rolePermission1.setPermissionId(permissionId);
                    rolePermissionRepo.save(rolePermission1);
                }
            }
            return RestResp.success("操作成功", rolePermission1);
        } catch (Exception e){
            LOG.error("auth URI failed: {}", e.getMessage(), e);
        }
        return RestResp.fail("操作失败");
    }


}
