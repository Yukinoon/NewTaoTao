package cn.itcast.mybatis.pojo;

import java.util.List;



/**
 * 订单表
 * 
 */
public class Order {

    private Integer id;

    private Long userId;

    private String orderNumber;
    
    private User user;
    
    //映射一对多查询的订单详情
    private List<Orderdetail> orderdetails;
    
    
    public List<Orderdetail> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<Orderdetail> orderdetails) {
        this.orderdetails = orderdetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

}
