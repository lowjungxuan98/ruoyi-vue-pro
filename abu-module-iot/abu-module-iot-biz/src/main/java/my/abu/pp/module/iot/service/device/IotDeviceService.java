package my.abu.pp.module.iot.service.device;

import jakarta.validation.*;
import my.abu.pp.module.iot.controller.admin.device.vo.*;
import my.abu.pp.module.iot.dal.dataobject.device.IotDeviceDO;
import my.abu.pp.framework.common.pojo.PageResult;

/**
 * IoT 设备 Service 接口
 *
 * @author 阿布源码
 */
public interface IotDeviceService {

    /**
     * 创建设备
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDevice(@Valid IotDeviceSaveReqVO createReqVO);

    /**
     * 更新设备
     *
     * @param updateReqVO 更新信息
     */
    void updateDevice(@Valid IotDeviceSaveReqVO updateReqVO);

    /**
     * 删除设备
     *
     * @param id 编号
     */
    void deleteDevice(Long id);

    /**
     * 获得设备
     *
     * @param id 编号
     * @return IoT 设备
     */
    IotDeviceDO getDevice(Long id);

    /**
     * 获得设备分页
     *
     * @param pageReqVO 分页查询
     * @return IoT 设备分页
     */
    PageResult<IotDeviceDO> getDevicePage(IotDevicePageReqVO pageReqVO);

    /**
     * 更新设备状态
     *
     * @param updateReqVO 更新信息
     */
    void updateDeviceStatus(IotDeviceStatusUpdateReqVO updateReqVO);

    /**
     * 获得设备数量
     *
     * @param productId 产品编号
     * @return 设备数量
     */
    Long getDeviceCountByProductId(Long productId);
}