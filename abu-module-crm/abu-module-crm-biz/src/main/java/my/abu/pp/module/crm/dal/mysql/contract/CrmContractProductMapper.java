package my.abu.pp.module.crm.dal.mysql.contract;


import my.abu.pp.framework.mybatis.core.mapper.BaseMapperX;
import my.abu.pp.framework.mybatis.core.query.LambdaQueryWrapperX;
import my.abu.pp.module.crm.dal.dataobject.contract.CrmContractProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 合同产品 Mapper
 *
 * @author HUIHUI
 */
@Mapper
public interface CrmContractProductMapper extends BaseMapperX<CrmContractProductDO> {

    default List<CrmContractProductDO> selectListByContractId(Long contractId) {
        return selectList(new LambdaQueryWrapperX<CrmContractProductDO>().eq(CrmContractProductDO::getContractId, contractId));
    }

}