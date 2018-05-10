package com.zgiot.app.server.module.auth.controller.authority;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.auth.constants.AuthConstants;
import com.zgiot.app.server.module.auth.mapper.AuthorityMapper;
import com.zgiot.app.server.module.auth.mapper.PlatformClientMapper;
import com.zgiot.app.server.module.auth.mapper.RoleMapper;
import com.zgiot.app.server.module.auth.pojo.Authority;
import com.zgiot.app.server.module.auth.pojo.PlatformClient;
import com.zgiot.app.server.module.auth.pojo.Role;
import com.zgiot.app.server.module.auth.service.AuthorityService;
import com.zgiot.app.server.module.auth.service.RelRoleAuthorityGroupService;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/authorities")
public class AuthorityController {

    private static final int PLATFORM_CLIENT_WEB = 1;
    private static final int PLATFORM_CLIENT_PAD = 2;
    private static final int PLATFORM_CLIENT_MOBILE = 3;
    private static final int PLATFORM_CLIENT_HAND_RING = 4;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PlatformClientMapper platformClientMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RelRoleAuthorityGroupService relRoleAuthorityGroupService;

    @RequestMapping(
            value = "/by-user/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> findAuthoritiesByUser(@PathVariable long userId) {
        List<Authority> list = authorityMapper.findAuthorityViaRoleOrAuthGroupByUser(userId);
        List<String> defaultAuthorityCode = new ArrayList<>();
        defaultAuthorityCode.add(AuthConstants.DEFAULT_MODULE_CODE);
        List<Authority> defaultModules = authorityMapper.getAuthorityByAuthorityCode(defaultAuthorityCode);
        boolean isExist = false;
        if (defaultModules != null && defaultModules.size() > 0) {
            for (Authority authority : defaultModules) {
                for (Authority auth : list) {
                    if (auth.getCode().equals(authority.getCode())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    list.add(authority);
                }
            }
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(list)
                , HttpStatus.OK);
    }

    @GetMapping("/authorityType/{authorityTypeId}")
    public ResponseEntity<String> getAuthorities(@PathVariable int authorityTypeId) {
        List<Authority> authorities = authorityMapper.getAuthorityByAuthorityTypeId(authorityTypeId);
        if (authorities == null || authorities.size() == 0) {
            ServerResponse res = new ServerResponse<>(
                    "authority data is empty", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(authorities)
                , HttpStatus.OK);
    }

    @GetMapping("/{authority-id}")
    public ResponseEntity<String> getAuthority(@PathVariable("authority-id") int id) {
        Authority authority = authorityMapper.getAuthority(id);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(authority)
                , HttpStatus.OK);
    }

    @GetMapping("/authorityTree")
    public ResponseEntity<String> getAuthorityTree() {
        List<List<AuthorityResponse>> children = getAllAuthorityTree(null);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(children)
                , HttpStatus.OK);
    }

    @GetMapping("/authorityTree/role/{roleId}")
    public ResponseEntity<String> getAuthorityTree(@PathVariable int roleId) {
        List<List<AuthorityResponse>> children = getAllAuthorityTree(roleId);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(children)
                , HttpStatus.OK);
    }

    @RequestMapping(value = "/authorityTree", method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditAuthorityTree(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        AuthorityRequest authorityRequest = JSON.parseObject(bodyStr, AuthorityRequest.class);
        int roleId = authorityRequest.getRoleId();
        String roleName = authorityRequest.getRoleName();
        Role role = roleMapper.getRoleByRoleName(roleName);
        boolean isExist = false;
        boolean isAdd = false;
        List<Integer> authorityIdList = authorityRequest.getAuthorityList();
        if (roleId == 0) {//add
            isAdd = true;
            if (role != null) {
                isExist = true;
            } else {
                saveRoleInfo(authorityIdList, roleName, roleId, isAdd);
            }
        } else {//edit
            if (role != null && role.getId() != roleId) {
                isExist = true;
            } else {
                relRoleAuthorityGroupService.deleteRelRoleAuthority(roleId);
                roleMapper.deleteRoleAuthority(roleId);
                saveRoleInfo(authorityIdList, roleName, roleId, isAdd);
            }
        }

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(isExist)
                , HttpStatus.OK);
    }

    private List<List<AuthorityResponse>> getAllAuthorityTree(Integer roleId) {
        List<AuthorityResponse> authorities = null;
        List<List<AuthorityResponse>> children = new ArrayList<>();
        List<PlatformClient> platformClients = platformClientMapper.findAll();
        for (PlatformClient platformClient : platformClients) {
            List<Authority> authorityList = authorityMapper.getAuthorityList(platformClient.getId());
            authorities = getAuthority(authorityList, roleId);
            children.add(authorities);
        }
        return children;
    }

    @Transactional
    public void saveRoleInfo(List<Integer> authorityIdList, String roleName, int roleId, boolean isAdd) {
        Role roleTemp = new Role();
        roleTemp.setName(roleName);
        roleTemp.setUpdateDate(new Date());
        if (isAdd) {
            roleMapper.insertRole(roleTemp);
            roleId = roleTemp.getId();
        } else {
            roleTemp.setId(roleId);
            roleMapper.updateRole(roleTemp);
        }

        //保存树节点时，如果这个节点是叶子节点，则同样需要保存除最顶层节点外的所有父节点，
        // 因为前端传过来的节点id列表中只包含叶子节点，不包含父节点
        //当再次点击编辑按钮时，查询到的树节点的选中节点只有叶子节点状态是checked,父节点不能是checked,因为父节点是checked的话，前端tree组件默认会选中此节点的所有子节点
        List<Authority> authorityList = authorityMapper.getAllAuthority();
        Set<Integer> parentNodes = new HashSet<>();
        for (int id : authorityIdList) {
            getAllParentNodeExcludeTopNode(id, parentNodes, authorityList);
        }
        authorityIdList.addAll(new ArrayList<>(parentNodes));
        for (int id : authorityIdList) {
            roleMapper.addRole(id, roleId);
        }
    }

    public void getAllParentNodeExcludeTopNode(int id, Set<Integer> parentNodes, List<Authority> authorityList) {
        if (id == 0) {//top node
            return;
        }
        Authority auth = null;
        for (Authority authority : authorityList) {
            if (authority.getId() == id) {
                auth = authority;
                break;
            }
        }
        if (auth != null) {
            if (auth.getParentId() == 0) {
                return;
            } else {
                parentNodes.add(auth.getParentId());
            }


            getAllParentNodeExcludeTopNode(auth.getParentId(), parentNodes, authorityList);
        }
    }

    private List<AuthorityResponse> getAuthority(List<Authority> authorityList, Integer roleId) {
        List<AuthorityResponse> children = new ArrayList<>();
        for (Authority authority : authorityList) {
            if (authority.getParentId() == 0) {
                AuthorityResponse authorityResponse = new AuthorityResponse();
                authorityResponse.setId(authority.getId());
                authorityResponse.setTitle(authority.getName());
                authorityResponse.setExpand(true);
                List<AuthorityResponse> authorities = getAuthorityTreeByParentAuthority(authorityResponse, authorityList, roleId);
                authorityResponse.setChildren(authorities);
                children.add(authorityResponse);
            }
        }

        return children;
    }

    private List<AuthorityResponse> getAuthorityTreeByParentAuthority(AuthorityResponse authorityTree, List<Authority> authorities, Integer roleId) {
        boolean isLeaf = true;
        List<AuthorityResponse> children = new ArrayList<>();
        for (Authority authority : authorities) {
            if (authority.getParentId() == authorityTree.getId()) {
                isLeaf = false;
                AuthorityResponse authorityResponse = new AuthorityResponse();
                authorityResponse.setId(authority.getId());
                authorityResponse.setTitle(authority.getName());
                List<AuthorityResponse> authList = getAuthorityTreeByParentAuthority(authorityResponse, authorities, roleId);
                authorityResponse.setChildren(authList);
                children.add(authorityResponse);
            }
        }

        if (isLeaf) {
            setChecked(authorityTree, roleId);
            return new ArrayList<>();
        } else {
            authorityTree.setExpand(true);
            return children;
        }
    }

    private void setChecked(AuthorityResponse authorityResponse, Integer roleId) {
        List<Authority> authorities = null;
        if (roleId != null) {
            authorities = authorityMapper.getRoleAuthority(roleId);
        }
        if (authorities != null && authorities.size() > 0) {
            for (Authority auth : authorities) {
                if (auth.getId() == authorityResponse.getId()) {
                    authorityResponse.setChecked(true);
                }
            }
        }
    }
}
                                                  