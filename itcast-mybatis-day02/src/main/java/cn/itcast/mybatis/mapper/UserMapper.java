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
     * 更新user数据
     * 
     * @param user
     */
    public void updateUser2(User user);

    /**
     * 根据id删除用户数据
     * 
     * @param id
     */
    public void deleteUserById(Long id);

    public List<User> queryUserListLikeName(@Param("name") String name);

    /**
     * 登陆，测试多个参数传入
     * 
     * @param userName
     * @param passwd
     * @return
     */
    public User login(@Param("userName") String userName, @Param("passwd") String passwd);

    /**
     * 测试动态sql
     * 
     * @param name
     * @return
     */
    public List<User> queryUserLikeName(@Param("name") String name);

    /**
     * 查询男性用户，如果输入了姓名则按照姓名模糊查找，否则如果输入了年龄则按照年龄查找。
     * 
     * @param name
     * @param age
     * @return
     */
    public List<User> queryUserLikeNameAndAge(@Param("name") String name, @Param("age") Integer age);

    /**
     * 查询所有用户，如果输入了姓名，进行模糊查找，如果输入了年龄则按照年龄查找。
     * 
     * @param name
     * @return
     */
    public List<User> queryUserLikeName2(@Param("name") String name, @Param("age") Integer age);

    /**
     * 按照多个ID查询用户信息。
     * 
     * @param name
     * @return
     */
    public List<User> queryUserByIds(@Param("ids") List<Long> ids);

}
