package cn.itcast.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.itcast.mybatis.pojo.User;

public interface UserMapper {
    
    /**
     * 根据id查询用户数据
     * 
     * @param id
     * @return
     */
    public User queryUserById(Long id);
    
    /**
     * 新增user数据
     * 
     * @param user
     */
    public void saveUser(User user);
    
    /**
     * 更新user数据
     * 
     * @param user
     */
    public void updateUser(User user);
    
    /**
     * 根据id删除用户数据
     * 
     * @param id
     */
    public void deleteUserById(Long id);
    
    public List<User> queryUserListLikeName(@Param("name")String name);

}
