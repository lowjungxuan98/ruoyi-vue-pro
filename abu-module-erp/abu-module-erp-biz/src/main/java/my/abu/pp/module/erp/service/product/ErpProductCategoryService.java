package my.abu.pp.module.erp.service.product;

import my.abu.pp.module.erp.controller.admin.product.vo.category.ErpProductCategoryListReqVO;
import my.abu.pp.module.erp.controller.admin.product.vo.category.ErpProductCategorySaveReqVO;
import my.abu.pp.module.erp.dal.dataobject.product.ErpProductCategoryDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static my.abu.pp.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * ERP 产品分类 Service 接口
 *
 * @author 阿布源码
 */
public interface ErpProductCategoryService {

    /**
     * 创建产品分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductCategory(@Valid ErpProductCategorySaveReqVO createReqVO);

    /**
     * 更新产品分类
     *
     * @param updateReqVO 更新信息
     */
    void updateProductCategory(@Valid ErpProductCategorySaveReqVO updateReqVO);

    /**
     * 删除产品分类
     *
     * @param id 编号
     */
    void deleteProductCategory(Long id);

    /**
     * 获得产品分类
     *
     * @param id 编号
     * @return 产品分类
     */
    ErpProductCategoryDO getProductCategory(Long id);

    /**
     * 获得产品分类列表
     *
     * @param listReqVO 查询条件
     * @return 产品分类列表
     */
    List<ErpProductCategoryDO> getProductCategoryList(ErpProductCategoryListReqVO listReqVO);

    /**
     * 获得产品分类列表
     *
     * @param ids 编号数组
     * @return 产品分类列表
     */
    List<ErpProductCategoryDO> getProductCategoryList(Collection<Long> ids);

    /**
     * 获得产品分类 Map
     *
     * @param ids 编号数组
     * @return 产品分类 Map
     */
    default Map<Long, ErpProductCategoryDO> getProductCategoryMap(Collection<Long> ids) {
        return convertMap(getProductCategoryList(ids), ErpProductCategoryDO::getId);
    }

}