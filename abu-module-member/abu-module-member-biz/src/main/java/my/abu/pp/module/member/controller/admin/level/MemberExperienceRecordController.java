package my.abu.pp.module.member.controller.admin.level;

import my.abu.pp.framework.common.pojo.CommonResult;
import my.abu.pp.framework.common.pojo.PageResult;
import my.abu.pp.module.member.controller.admin.level.vo.experience.MemberExperienceRecordPageReqVO;
import my.abu.pp.module.member.controller.admin.level.vo.experience.MemberExperienceRecordRespVO;
import my.abu.pp.module.member.convert.level.MemberExperienceRecordConvert;
import my.abu.pp.module.member.dal.dataobject.level.MemberExperienceRecordDO;
import my.abu.pp.module.member.service.level.MemberExperienceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static my.abu.pp.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 会员经验记录")
@RestController
@RequestMapping("/member/experience-record")
@Validated
public class MemberExperienceRecordController {

    @Resource
    private MemberExperienceRecordService experienceLogService;

    @GetMapping("/get")
    @Operation(summary = "获得会员经验记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:experience-record:query')")
    public CommonResult<MemberExperienceRecordRespVO> getExperienceRecord(@RequestParam("id") Long id) {
        MemberExperienceRecordDO experienceLog = experienceLogService.getExperienceRecord(id);
        return success(MemberExperienceRecordConvert.INSTANCE.convert(experienceLog));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员经验记录分页")
    @PreAuthorize("@ss.hasPermission('member:experience-record:query')")
    public CommonResult<PageResult<MemberExperienceRecordRespVO>> getExperienceRecordPage(
            @Valid MemberExperienceRecordPageReqVO pageVO) {
        PageResult<MemberExperienceRecordDO> pageResult = experienceLogService.getExperienceRecordPage(pageVO);
        return success(MemberExperienceRecordConvert.INSTANCE.convertPage(pageResult));
    }

}