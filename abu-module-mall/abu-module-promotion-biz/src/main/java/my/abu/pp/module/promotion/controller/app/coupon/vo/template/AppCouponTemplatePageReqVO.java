package my.abu.pp.module.promotion.controller.app.coupon.vo.template;

import my.abu.pp.framework.common.pojo.PageParam;
import my.abu.pp.framework.common.validation.InEnum;
import my.abu.pp.module.promotion.enums.common.PromotionProductScopeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "用户 App - 优惠劵模板分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCouponTemplatePageReqVO extends PageParam {

    @Schema(description = "商品范围", example = "1")
    @InEnum(value = PromotionProductScopeEnum.class, message = "商品范围，必须是 {value}")
    private Integer productScope;

    @Schema(description = "商品标号", example = "1")
    private Long spuId;

}