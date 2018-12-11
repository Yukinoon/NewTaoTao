package cn.itcast.mybatis.mapper;

import cn.itcast.mybatis.pojo.Order;
import cn.itcast.mybatis.pojo.OrderUser;

/**
 * 测试高级查询
 */
public interface OrderMapper {
    
    /**
     * 查询订单，并且查询出下单人的信息。
     * @param orderNumber
     * @return
     */
    public OrderUser queryOrderAndUserByOrderNumber(String orderNumber);
    
    /**
     * 测试延迟加载
     * 查询订单，并且查询出下单人的信息。
     * @param orderNumber
     * @return
     */
    public Order lazyQueryOrderAndUserByOrderNumber(String orderNumber);
    
    /**
     * 查询订单，并且查询出下单人的信息。
     * @param orderNumber
     * @return
     */
    public Order queryOrderAndUserByOrderNumber2(String orderNumber);
    
    /**
     * 查询订单，查询出下单人信息并且查询出订单详情。
     * 
     * @param orderNumber
     * @return
     */
    public Order queryOrderAndUserAndOrderDetailByOrderNumber(String orderNumber);
    
    /**
     * 多对多查询
     * 查询订单，查询出下单人信息并且查询出订单详情中的商品数据。
     * @param orderNumber
     * @return
     */
    public Order queryOrderAndUserAndOrderDetailAndItemByOrderNumber(String orderNumber);

}
