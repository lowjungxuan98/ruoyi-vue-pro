package my.abu.pp.module.infra.job.logger;

import my.abu.pp.framework.quartz.core.handler.JobHandler;
import my.abu.pp.framework.tenant.core.aop.TenantIgnore;
import my.abu.pp.module.infra.service.logger.ApiAccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 物理删除 N 天前的访问日志的 Job
 *
 * @author j-sentinel
 */
@Component
@Slf4j
public class AccessLogCleanJob implements JobHandler {

    @Resource
    private ApiAccessLogService apiAccessLogService;

    /**
     * 清理超过（14）天的日志
     */
    private static final Integer JOB_CLEAN_RETAIN_DAY = 14;

    /**
     * 每次删除间隔的条数，如果值太高可能会造成数据库的压力过大
     */
    private static final Integer DELETE_LIMIT = 100;

    @Override
    @TenantIgnore
    public String execute(String param) {
        Integer count = apiAccessLogService.cleanAccessLog(JOB_CLEAN_RETAIN_DAY, DELETE_LIMIT);
        log.info("[execute][定时执行清理访问日志数量 ({}) 个]", count);
        return String.format("定时执行清理访问日志数量 %s 个", count);
    }

}