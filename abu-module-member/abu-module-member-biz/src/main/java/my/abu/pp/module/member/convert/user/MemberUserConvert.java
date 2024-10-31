package my.abu.pp.module.member.convert.user;

import my.abu.pp.framework.common.pojo.PageResult;
import my.abu.pp.module.member.api.user.dto.MemberUserRespDTO;
import my.abu.pp.module.member.controller.admin.user.vo.MemberUserRespVO;
import my.abu.pp.module.member.controller.admin.user.vo.MemberUserUpdateReqVO;
import my.abu.pp.module.member.controller.app.user.vo.AppMemberUserInfoRespVO;
import my.abu.pp.module.member.convert.address.AddressConvert;
import my.abu.pp.module.member.dal.dataobject.group.MemberGroupDO;
import my.abu.pp.module.member.dal.dataobject.level.MemberLevelDO;
import my.abu.pp.module.member.dal.dataobject.tag.MemberTagDO;
import my.abu.pp.module.member.dal.dataobject.user.MemberUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static my.abu.pp.framework.common.util.collection.CollectionUtils.convertList;
import static my.abu.pp.framework.common.util.collection.CollectionUtils.convertMap;

@Mapper(uses = {AddressConvert.class})
public interface MemberUserConvert {

    MemberUserConvert INSTANCE = Mappers.getMapper(MemberUserConvert.class);

    AppMemberUserInfoRespVO convert(MemberUserDO bean);


    @Mappings({
            @Mapping(source = "level", target = "level"),
            @Mapping(source = "bean.id", target = "id"),
            @Mapping(source = "bean.experience", target = "experience")
    })
    AppMemberUserInfoRespVO convert(MemberUserDO bean, MemberLevelDO level);

    MemberUserRespDTO convert2(MemberUserDO bean);

    List<MemberUserRespDTO> convertList2(List<MemberUserDO> list);

    MemberUserDO convert(MemberUserUpdateReqVO bean);

    PageResult<MemberUserRespVO> convertPage(PageResult<MemberUserDO> page);

    @Mapping(source = "areaId", target = "areaName", qualifiedByName = "convertAreaIdToAreaName")
    MemberUserRespVO convert03(MemberUserDO bean);

    default PageResult<MemberUserRespVO> convertPage(PageResult<MemberUserDO> pageResult,
                                                     List<MemberTagDO> tags,
                                                     List<MemberLevelDO> levels,
                                                     List<MemberGroupDO> groups) {
        PageResult<MemberUserRespVO> result = convertPage(pageResult);
        // 处理关联数据
        Map<Long, String> tagMap = convertMap(tags, MemberTagDO::getId, MemberTagDO::getName);
        Map<Long, String> levelMap = convertMap(levels, MemberLevelDO::getId, MemberLevelDO::getName);
        Map<Long, String> groupMap = convertMap(groups, MemberGroupDO::getId, MemberGroupDO::getName);
        // 填充关联数据
        result.getList().forEach(user -> {
            user.setTagNames(convertList(user.getTagIds(), tagMap::get));
            user.setLevelName(levelMap.get(user.getLevelId()));
            user.setGroupName(groupMap.get(user.getGroupId()));
        });
        return result;
    }

}