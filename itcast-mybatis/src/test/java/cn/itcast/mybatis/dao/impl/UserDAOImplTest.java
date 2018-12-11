package cn.itcast.mybatis.dao.impl;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Date;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.mybatis.dao.UserDAO;
import cn.itcast.mybatis.pojo.User;

public class UserDAOImplTest {

    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.userDAO = new UserDAOImpl(sqlSessionFactory);
    }

    @Test
    public void testQueryUserById() {
       User user = this.userDAO.queryUserById(1L);
       System.out.println(user);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setAge(20);
        user.setBirthday(new Date());
        user.setName("test_1");
        user.setPassword("123456");
        user.setSex(1);
        user.setUserName("test_username_1");
        
        this.userDAO.saveUser(user);
    }

    @Test
    public void testUpdateUser() {
        User user = this.userDAO.queryUserById(2L);
        user.setAge(30);
        this.userDAO.updateUser(user);
    }

    @Test
    public void testDeleteUserById() {
        this.userDAO.deleteUserById(6L);
    }

}
