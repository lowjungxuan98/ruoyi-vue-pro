package my.abu.pp.module.infra.api.logger;

import my.abu.pp.module.infra.api.logger.dto.ApiErrorLogCreateReqDTO;

import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;

/**
 * API 错误日志的 API 接口
 *
 * @author 阿布源码
 */
public interface ApiErrorLogApi {

    /**
     * 创建 API 错误日志
     *
     * @param createDTO 创建信息
     */
    void createApiErrorLog(@Valid ApiErrorLogCreateReqDTO createDTO);

    /**
     * 【异步】创建 API 异常日志
     *
     * @param createDTO 异常日志 DTO
     */
    @Async
    default void createApiErrorLogAsync(ApiErrorLogCreateReqDTO createDTO) {
        createApiErrorLog(createDTO);
    }

}