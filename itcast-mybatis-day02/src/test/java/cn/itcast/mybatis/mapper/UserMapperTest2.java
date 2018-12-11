package cn.itcast.mybatis.mapper;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.mybatis.pojo.User;

/**
 * 测试缓存
 * 
 */
public class UserMapperTest2 {

    private UserMapper userMapper;
    private SqlSessionFactory sqlSessionFactory  = null;

    @Before
    public void setUp() throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, "test");
        //SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 通过sqlSession获取到动态代理的实现类
        //this.userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void testQueryUserById() {
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        this.userMapper = sqlSession.getMapper(UserMapper.class);
        //第一次查询
        User user = this.userMapper.queryUserById(1L);
        
        //清空缓存
//        sqlSession.clearCache();
        
        //在执行insert、update、delete时会刷新缓存。
//        user.setAge(50);
//        this.userMapper.updateUser(user);
//       sqlSession.close();
        
        //第二次查询
        user = this.userMapper.queryUserById(1L);
        System.out.println(user);
    }
    
    /**
     * 测试二级缓存
     */
    @Test
    public void testQueryUserById2() {
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        this.userMapper = sqlSession.getMapper(UserMapper.class);
        //第一次查询
        User user = this.userMapper.queryUserById(1L);
        
        //关闭session
        sqlSession.close();
        
        //再次打开session
        sqlSession = this.sqlSessionFactory.openSession();
        this.userMapper = sqlSession.getMapper(UserMapper.class);
        
        //第二次查询
        user = this.userMapper.queryUserById(1L);
        System.out.println(user);
    }
    

}
