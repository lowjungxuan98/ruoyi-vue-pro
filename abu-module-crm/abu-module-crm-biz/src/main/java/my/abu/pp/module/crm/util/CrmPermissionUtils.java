package my.abu.pp.module.crm.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import my.abu.pp.module.crm.dal.dataobject.permission.CrmPermissionDO;
import my.abu.pp.module.crm.enums.common.CrmBizTypeEnum;
import my.abu.pp.module.crm.enums.common.CrmSceneTypeEnum;
import my.abu.pp.module.crm.enums.permission.CrmPermissionLevelEnum;
import my.abu.pp.module.system.api.permission.PermissionApi;
import my.abu.pp.module.system.api.user.AdminUserApi;
import my.abu.pp.module.system.api.user.dto.AdminUserRespDTO;
import my.abu.pp.module.system.enums.permission.RoleCodeEnum;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.autoconfigure.MybatisPlusJoinProperties;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.List;

import static my.abu.pp.framework.common.util.collection.CollectionUtils.convertSet;
import static my.abu.pp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * 数据权限工具类
 *
 * @author HUIHUI
 */
public class CrmPermissionUtils {

    /**
     * 校验用户是否是 CRM 管理员
     *
     * @return 是/否
     */
    public static boolean isCrmAdmin() {
        PermissionApi permissionApi = SpringUtil.getBean(PermissionApi.class);
        return permissionApi.hasAnyRoles(getLoginUserId(), RoleCodeEnum.CRM_ADMIN.getCode());
    }

    /**
     * 构造 CRM 数据类型数据【分页】查询条件
     *
     * @param query     连表查询对象
     * @param bizType   数据类型 {@link CrmBizTypeEnum}
     * @param bizId     数据编号
     * @param userId    用户编号
     * @param sceneType 场景类型
     */
    public static <T extends MPJLambdaWrapper<?>, S> void appendPermissionCondition(T query, Integer bizType, SFunction<S, ?> bizId,
                                                                                    Long userId, Integer sceneType) {
        MybatisPlusJoinProperties mybatisPlusJoinProperties = SpringUtil.getBean(MybatisPlusJoinProperties.class);
        final String ownerUserIdField = mybatisPlusJoinProperties.getTableAlias() + ".owner_user_id";
        // 场景一：我负责的数据
        if (CrmSceneTypeEnum.isOwner(sceneType)) {
            query.eq(ownerUserIdField, userId);
        }
        // 场景二：我参与的数据（我有读或写权限，并且不是负责人）
        if (CrmSceneTypeEnum.isInvolved(sceneType)) {
            if (CrmPermissionUtils.isCrmAdmin()) { // 特殊逻辑：如果是超管，直接查询所有，不过滤数据权限
                return;
            }
            query.innerJoin(CrmPermissionDO.class, on -> on.eq(CrmPermissionDO::getBizType, bizType)
                    .eq(CrmPermissionDO::getBizId, bizId)
                    .in(CrmPermissionDO::getLevel, CrmPermissionLevelEnum.READ.getLevel(), CrmPermissionLevelEnum.WRITE.getLevel()));
            query.ne(ownerUserIdField, userId);
        }
        // 场景三：下属负责的数据（下属是负责人）
        if (CrmSceneTypeEnum.isSubordinate(sceneType)) {
            AdminUserApi adminUserApi = SpringUtil.getBean(AdminUserApi.class);
            List<AdminUserRespDTO> subordinateUsers = adminUserApi.getUserListBySubordinate(userId);
            if (CollUtil.isEmpty(subordinateUsers)) {
                query.eq(ownerUserIdField, -1); // 不返回任何结果
            } else {
                query.in(ownerUserIdField, convertSet(subordinateUsers, AdminUserRespDTO::getId));
            }
        }
    }

}