package cn.itcast.mybatis;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cn.itcast.mybatis.pojo.User;

public class Mybatis2 {

    public static void main(String[] args) throws Exception {
        // 定义配置文件名称
        String resource = "mybatis-config.xml";
        // 通过Resources工具类读取配置文件
        InputStream inputStream = Resources.getResourceAsStream(resource);
        // 通过SqlSessionFactoryBuilder构造SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 或得到SqlSession
        SqlSession session = sqlSessionFactory.openSession();

        System.out.println(sqlSessionFactory);
        System.out.println(session);

        User user = session.selectOne("abc.queryUserById", 1);
        System.out.println(user);
        
        session.close();
    }

}
