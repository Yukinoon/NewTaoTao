package cn.itcast.mybatis.mapper;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.mybatis.pojo.Order;
import cn.itcast.mybatis.pojo.OrderUser;
import cn.itcast.mybatis.pojo.User;

public class OrderMapperTest {

    private OrderMapper orderMapper;

    @Before
    public void setUp() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 通过sqlSession获取到动态代理的实现类
        this.orderMapper = sqlSession.getMapper(OrderMapper.class);
    }

    @Test
    public void testQueryOrderAndUserByOrderNumber() {
        OrderUser orderUser = this.orderMapper.queryOrderAndUserByOrderNumber("20140921001");
        System.out.println(orderUser);
    }

    @Test
    public void testQueryOrderAndUserByOrderNumber2() {
        Order order = this.orderMapper.queryOrderAndUserByOrderNumber2("20140921001");
        System.out.println(order);
    }

    @Test
    public void testLazyQueryOrderAndUserByOrderNumber() {
        Order order = this.orderMapper.lazyQueryOrderAndUserByOrderNumber("20140921001");
        User user = order.getUser();//触发延迟加载
        System.out.println(user);
        System.out.println(order);
    }

    @Test
    public void testQueryOrderAndUserAndOrderDetailByOrderNumber() {
        Order order = this.orderMapper.queryOrderAndUserAndOrderDetailByOrderNumber("20140921001");
        System.out.println(order);
    }

    @Test
    public void testQueryOrderAndUserAndOrderDetailAndItemByOrderNumber() {
        Order order = this.orderMapper.queryOrderAndUserAndOrderDetailAndItemByOrderNumber("20140921001");
        System.out.println(order);
    }

}
