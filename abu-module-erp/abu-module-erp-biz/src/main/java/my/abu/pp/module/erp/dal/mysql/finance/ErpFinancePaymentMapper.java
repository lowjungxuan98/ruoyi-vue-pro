package my.abu.pp.module.erp.dal.mysql.finance;

import my.abu.pp.framework.common.pojo.PageResult;
import my.abu.pp.framework.mybatis.core.mapper.BaseMapperX;
import my.abu.pp.framework.mybatis.core.query.MPJLambdaWrapperX;
import my.abu.pp.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentPageReqVO;
import my.abu.pp.module.erp.dal.dataobject.finance.ErpFinancePaymentDO;
import my.abu.pp.module.erp.dal.dataobject.finance.ErpFinancePaymentItemDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 付款单 Mapper
 *
 * @author 阿布源码
 */
@Mapper
public interface ErpFinancePaymentMapper extends BaseMapperX<ErpFinancePaymentDO> {

    default PageResult<ErpFinancePaymentDO> selectPage(ErpFinancePaymentPageReqVO reqVO) {
        MPJLambdaWrapperX<ErpFinancePaymentDO> query = new MPJLambdaWrapperX<ErpFinancePaymentDO>()
                .likeIfPresent(ErpFinancePaymentDO::getNo, reqVO.getNo())
                .betweenIfPresent(ErpFinancePaymentDO::getPaymentTime, reqVO.getPaymentTime())
                .eqIfPresent(ErpFinancePaymentDO::getSupplierId, reqVO.getSupplierId())
                .eqIfPresent(ErpFinancePaymentDO::getCreator, reqVO.getCreator())
                .eqIfPresent(ErpFinancePaymentDO::getFinanceUserId, reqVO.getFinanceUserId())
                .eqIfPresent(ErpFinancePaymentDO::getAccountId, reqVO.getAccountId())
                .eqIfPresent(ErpFinancePaymentDO::getStatus, reqVO.getStatus())
                .likeIfPresent(ErpFinancePaymentDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinancePaymentDO::getId);
        if (reqVO.getBizNo() != null) {
            query.leftJoin(ErpFinancePaymentItemDO.class, ErpFinancePaymentItemDO::getPaymentId, ErpFinancePaymentDO::getId)
                    .eq(reqVO.getBizNo() != null, ErpFinancePaymentItemDO::getBizNo, reqVO.getBizNo())
                    .groupBy(ErpFinancePaymentDO::getId); // 避免 1 对多查询，产生相同的 1
        }
        return selectJoinPage(reqVO, ErpFinancePaymentDO.class, query);
    }

    default int updateByIdAndStatus(Long id, Integer status, ErpFinancePaymentDO updateObj) {
        return update(updateObj, new LambdaUpdateWrapper<ErpFinancePaymentDO>()
                .eq(ErpFinancePaymentDO::getId, id).eq(ErpFinancePaymentDO::getStatus, status));
    }

    default ErpFinancePaymentDO selectByNo(String no) {
        return selectOne(ErpFinancePaymentDO::getNo, no);
    }

}