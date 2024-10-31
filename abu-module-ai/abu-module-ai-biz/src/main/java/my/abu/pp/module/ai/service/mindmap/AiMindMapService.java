package my.abu.pp.module.ai.service.mindmap;

import my.abu.pp.framework.common.pojo.CommonResult;
import my.abu.pp.framework.common.pojo.PageResult;
import my.abu.pp.module.ai.controller.admin.mindmap.vo.AiMindMapGenerateReqVO;
import my.abu.pp.module.ai.controller.admin.mindmap.vo.AiMindMapPageReqVO;
import my.abu.pp.module.ai.dal.dataobject.mindmap.AiMindMapDO;
import reactor.core.publisher.Flux;

/**
 * AI 思维导图 Service 接口
 *
 * @author xiaoxin
 */
public interface AiMindMapService {

    /**
     * 生成思维导图内容
     *
     * @param generateReqVO 请求参数
     * @param userId        用户编号
     * @return 生成结果
     */
    Flux<CommonResult<String>> generateMindMap(AiMindMapGenerateReqVO generateReqVO, Long userId);

    /**
     * 删除思维导图
     *
     * @param id 编号
     */
    void deleteMindMap(Long id);

    /**
     * 获得思维导图分页
     *
     * @param pageReqVO 分页查询
     * @return 思维导图分页
     */
    PageResult<AiMindMapDO> getMindMapPage(AiMindMapPageReqVO pageReqVO);

}