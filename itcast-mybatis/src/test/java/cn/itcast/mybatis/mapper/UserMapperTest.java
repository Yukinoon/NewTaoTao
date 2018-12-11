package cn.itcast.mybatis.mapper;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.mybatis.pojo.User;

/**
 * 动态代理实现类测试用例
 * 
 */
public class UserMapperTest {

    private UserMapper userMapper;

    @Before
    public void setUp() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "test");
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 通过sqlSession获取到动态代理的实现类
        this.userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void testQueryUserById() {
        User user = this.userMapper.queryUserById(1L);
        System.out.println(user);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setAge(20);
        user.setBirthday(new Date());
        user.setName("test_3");
        user.setPassword("123456");
        user.setSex(1);
        user.setUserName("test_username_3");

        this.userMapper.saveUser(user);
    }

    @Test
    public void testUpdateUser() {
        User user = this.userMapper.queryUserById(2L);
        user.setAge(35);
        this.userMapper.updateUser(user);
    }

    @Test
    public void testDeleteUserById() {
        this.userMapper.deleteUserById(7L);
    }

    @Test
    public void testQueryUserListLikeName() {
        List<User> users = this.userMapper.queryUserListLikeName("张");
        for (User user : users) {
            System.out.println(user);
        }
    }

}
