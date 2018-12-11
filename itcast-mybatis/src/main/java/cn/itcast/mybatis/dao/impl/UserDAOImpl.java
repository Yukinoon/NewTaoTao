package cn.itcast.mybatis.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.itcast.mybatis.dao.UserDAO;
import cn.itcast.mybatis.pojo.User;

public class UserDAOImpl implements UserDAO{
    
    private SqlSessionFactory sqlSessionFactory;
    
    public UserDAOImpl(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public User queryUserById(Long id) {
        SqlSession session = this.sqlSessionFactory.openSession();
        User user = session.selectOne("userDAO.queryUserById", id);
        session.close();
        return user;
    }

    @Override
    public void saveUser(User user) {
        SqlSession session = this.sqlSessionFactory.openSession();
        session.insert("userDAO.saveUser", user);
        //提交事物
        session.commit();
        session.close();
    }

    @Override
    public void updateUser(User user) {
        SqlSession session = this.sqlSessionFactory.openSession();
        session.update("userDAO.updateUser", user);
        //提交事物
        session.commit();
        session.close();
    }

    @Override
    public void deleteUserById(Long id) {
        SqlSession session = this.sqlSessionFactory.openSession();
        session.delete("userDAO.deleteUserById", id);
        //提交事物
        session.commit();
        session.close();
    }

}
