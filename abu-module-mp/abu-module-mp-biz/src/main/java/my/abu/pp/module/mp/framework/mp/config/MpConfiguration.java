package my.abu.pp.module.mp.framework.mp.config;

import my.abu.pp.module.mp.framework.mp.core.DefaultMpServiceFactory;
import my.abu.pp.module.mp.framework.mp.core.MpServiceFactory;
import my.abu.pp.module.mp.service.handler.menu.MenuHandler;
import my.abu.pp.module.mp.service.handler.message.MessageReceiveHandler;
import my.abu.pp.module.mp.service.handler.message.MessageAutoReplyHandler;
import my.abu.pp.module.mp.service.handler.other.KfSessionHandler;
import my.abu.pp.module.mp.service.handler.other.NullHandler;
import my.abu.pp.module.mp.service.handler.other.ScanHandler;
import my.abu.pp.module.mp.service.handler.other.StoreCheckNotifyHandler;
import my.abu.pp.module.mp.service.handler.user.LocationHandler;
import my.abu.pp.module.mp.service.handler.user.SubscribeHandler;
import my.abu.pp.module.mp.service.handler.user.UnsubscribeHandler;
import com.binarywang.spring.starter.wxjava.mp.properties.WxMpProperties;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 微信公众号的配置类
 *
 * @author 阿布源码
 */
@Configuration
public class MpConfiguration {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RedisTemplateWxRedisOps redisTemplateWxRedisOps(StringRedisTemplate stringRedisTemplate) {
        return new RedisTemplateWxRedisOps(stringRedisTemplate);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MpServiceFactory mpServiceFactory(RedisTemplateWxRedisOps redisTemplateWxRedisOps,
                                             WxMpProperties wxMpProperties,
                                             MessageReceiveHandler messageReceiveHandler,
                                             KfSessionHandler kfSessionHandler,
                                             StoreCheckNotifyHandler storeCheckNotifyHandler,
                                             MenuHandler menuHandler,
                                             NullHandler nullHandler,
                                             SubscribeHandler subscribeHandler,
                                             UnsubscribeHandler unsubscribeHandler,
                                             LocationHandler locationHandler,
                                             ScanHandler scanHandler,
                                             MessageAutoReplyHandler messageAutoReplyHandler) {
        return new DefaultMpServiceFactory(redisTemplateWxRedisOps, wxMpProperties,
                messageReceiveHandler, kfSessionHandler, storeCheckNotifyHandler, menuHandler,
                nullHandler, subscribeHandler, unsubscribeHandler, locationHandler, scanHandler, messageAutoReplyHandler);
    }

}