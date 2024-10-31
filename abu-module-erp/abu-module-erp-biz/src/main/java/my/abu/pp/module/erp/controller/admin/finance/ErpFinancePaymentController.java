package my.abu.pp.module.erp.controller.admin.finance;

import cn.hutool.core.collection.CollUtil;
import my.abu.pp.framework.apilog.core.annotation.ApiAccessLog;
import my.abu.pp.framework.common.pojo.CommonResult;
import my.abu.pp.framework.common.pojo.PageParam;
import my.abu.pp.framework.common.pojo.PageResult;
import my.abu.pp.framework.common.util.collection.MapUtils;
import my.abu.pp.framework.common.util.number.NumberUtils;
import my.abu.pp.framework.common.util.object.BeanUtils;
import my.abu.pp.framework.excel.core.util.ExcelUtils;
import my.abu.pp.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentPageReqVO;
import my.abu.pp.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentRespVO;
import my.abu.pp.module.erp.controller.admin.finance.vo.payment.ErpFinancePaymentSaveReqVO;
import my.abu.pp.module.erp.dal.dataobject.finance.ErpAccountDO;
import my.abu.pp.module.erp.dal.dataobject.finance.ErpFinancePaymentDO;
import my.abu.pp.module.erp.dal.dataobject.finance.ErpFinancePaymentItemDO;
import my.abu.pp.module.erp.dal.dataobject.purchase.ErpSupplierDO;
import my.abu.pp.module.erp.service.finance.ErpAccountService;
import my.abu.pp.module.erp.service.finance.ErpFinancePaymentService;
import my.abu.pp.module.erp.service.purchase.ErpSupplierService;
import my.abu.pp.module.system.api.user.AdminUserApi;
import my.abu.pp.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static my.abu.pp.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static my.abu.pp.framework.common.pojo.CommonResult.success;
import static my.abu.pp.framework.common.util.collection.CollectionUtils.*;

@Tag(name = "管理后台 - ERP 付款单")
@RestController
@RequestMapping("/erp/finance-payment")
@Validated
public class ErpFinancePaymentController {

    @Resource
    private ErpFinancePaymentService financePaymentService;
    @Resource
    private ErpSupplierService supplierService;
    @Resource
    private ErpAccountService accountService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建付款单")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:create')")
    public CommonResult<Long> createFinancePayment(@Valid @RequestBody ErpFinancePaymentSaveReqVO createReqVO) {
        return success(financePaymentService.createFinancePayment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新付款单")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:update')")
    public CommonResult<Boolean> updateFinancePayment(@Valid @RequestBody ErpFinancePaymentSaveReqVO updateReqVO) {
        financePaymentService.updateFinancePayment(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新付款单的状态")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:update-status')")
    public CommonResult<Boolean> updateFinancePaymentStatus(@RequestParam("id") Long id,
                                                           @RequestParam("status") Integer status) {
        financePaymentService.updateFinancePaymentStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除付款单")
    @Parameter(name = "ids", description = "编号数组", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:delete')")
    public CommonResult<Boolean> deleteFinancePayment(@RequestParam("ids") List<Long> ids) {
        financePaymentService.deleteFinancePayment(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得付款单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:query')")
    public CommonResult<ErpFinancePaymentRespVO> getFinancePayment(@RequestParam("id") Long id) {
        ErpFinancePaymentDO payment = financePaymentService.getFinancePayment(id);
        if (payment == null) {
            return success(null);
        }
        List<ErpFinancePaymentItemDO> paymentItemList = financePaymentService.getFinancePaymentItemListByPaymentId(id);
        return success(BeanUtils.toBean(payment, ErpFinancePaymentRespVO.class, financePaymentVO ->
                financePaymentVO.setItems(BeanUtils.toBean(paymentItemList, ErpFinancePaymentRespVO.Item.class))));
    }

    @GetMapping("/page")
    @Operation(summary = "获得付款单分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:query')")
    public CommonResult<PageResult<ErpFinancePaymentRespVO>> getFinancePaymentPage(@Valid ErpFinancePaymentPageReqVO pageReqVO) {
        PageResult<ErpFinancePaymentDO> pageResult = financePaymentService.getFinancePaymentPage(pageReqVO);
        return success(buildFinancePaymentVOPageResult(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出付款单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-payment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinancePaymentExcel(@Valid ErpFinancePaymentPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinancePaymentRespVO> list = buildFinancePaymentVOPageResult(financePaymentService.getFinancePaymentPage(pageReqVO)).getList();
        // 导出 Excel
        ExcelUtils.write(response, "付款单.xls", "数据", ErpFinancePaymentRespVO.class, list);
    }

    private PageResult<ErpFinancePaymentRespVO> buildFinancePaymentVOPageResult(PageResult<ErpFinancePaymentDO> pageResult) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        // 1.1 付款项
        List<ErpFinancePaymentItemDO> paymentItemList = financePaymentService.getFinancePaymentItemListByPaymentIds(
                convertSet(pageResult.getList(), ErpFinancePaymentDO::getId));
        Map<Long, List<ErpFinancePaymentItemDO>> financePaymentItemMap = convertMultiMap(paymentItemList, ErpFinancePaymentItemDO::getPaymentId);
        // 1.2 供应商信息
        Map<Long, ErpSupplierDO> supplierMap = supplierService.getSupplierMap(
                convertSet(pageResult.getList(), ErpFinancePaymentDO::getSupplierId));
        // 1.3 结算账户信息
        Map<Long, ErpAccountDO> accountMap = accountService.getAccountMap(
                convertSet(pageResult.getList(), ErpFinancePaymentDO::getAccountId));
        // 1.4 管理员信息
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(pageResult.getList(),
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getFinanceUserId())));
        // 2. 开始拼接
        return BeanUtils.toBean(pageResult, ErpFinancePaymentRespVO.class, payment -> {
            payment.setItems(BeanUtils.toBean(financePaymentItemMap.get(payment.getId()), ErpFinancePaymentRespVO.Item.class));
            MapUtils.findAndThen(supplierMap, payment.getSupplierId(), supplier -> payment.setSupplierName(supplier.getName()));
            MapUtils.findAndThen(accountMap, payment.getAccountId(), account -> payment.setAccountName(account.getName()));
            MapUtils.findAndThen(userMap, Long.parseLong(payment.getCreator()), user -> payment.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, payment.getFinanceUserId(), user -> payment.setFinanceUserName(user.getNickname()));
        });
    }

}