package my.abu.pp.module.statistics.dal.mysql.member;

import my.abu.pp.framework.mybatis.core.mapper.BaseMapperX;
import my.abu.pp.module.statistics.controller.admin.member.vo.MemberRegisterCountRespVO;
import my.abu.pp.module.statistics.controller.admin.member.vo.MemberSexStatisticsRespVO;
import my.abu.pp.module.statistics.controller.admin.member.vo.MemberTerminalStatisticsRespVO;
import my.abu.pp.module.statistics.service.member.bo.MemberAreaStatisticsRespBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员信息的统计 Mapper
 *
 * @author owen
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface MemberStatisticsMapper extends BaseMapperX {

    List<MemberAreaStatisticsRespBO> selectSummaryListByAreaId();

    List<MemberSexStatisticsRespVO> selectSummaryListBySex();

    List<MemberTerminalStatisticsRespVO> selectSummaryListByRegisterTerminal();

    Integer selectUserCount(@Param("beginTime") LocalDateTime beginTime,
                            @Param("endTime") LocalDateTime endTime);

    /**
     * 获得用户的每天注册数量列表
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 每天注册数量列表
     */
    List<MemberRegisterCountRespVO> selectListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                                  @Param("endTime") LocalDateTime endTime);

}