package cn.itcast.mybatis.mapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
    public void testUpdateUser2() {
        User user = this.userMapper.queryUserById(2L);
        user.setAge(35);
        user.setPassword(null);
        this.userMapper.updateUser2(user);
    }

    @Test
    public void testDeleteUserById() {
        this.userMapper.deleteUserById(7L);
    }

    @Test
    public void testQueryUserListLikeName() {
        //设置分页条件，参数分别是：页数、页数大小数据条数、是否查询数据总条数
        PageHelper.startPage(1, 3, true);
        List<User> users = this.userMapper.queryUserListLikeName(null);
        
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        
        //打印分页信息
        System.out.println("数据总数：" + pageInfo.getTotal());
        System.out.println("数据总页数：" + pageInfo.getPages());
        System.out.println("最后一页：" + pageInfo.getLastPage());
        
        for (User user : pageInfo.getList()) {
            System.out.println(user);
        }
    }
    
    @Test
    public void testLogin() {
        User user = this.userMapper.login("zhangsan", "123456");
        System.out.println(user);
    }
    
    @Test
    public void testQueryUserLikeName(){
        List<User> users = this.userMapper.queryUserLikeName("李");
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    @Test
    public void testQueryUserLikeNameAndAge(){
        List<User> users = this.userMapper.queryUserLikeNameAndAge("张",30);
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    @Test
    public void testQueryUserLikeName2(){
        List<User> users = this.userMapper.queryUserLikeName2(null,30);
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    @Test
    public void testQueryUserByIds(){
        List<Long> ids = new ArrayList<Long>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        List<User> users = this.userMapper.queryUserByIds(ids);
        for (User user : users) {
            System.out.println(user);
        }
    }
    
    

}
