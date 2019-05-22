package com.tz.oa.sysmanage.mapper;

import java.util.List;

import com.tz.oa.sysmanage.dto.UserDto;
import com.tz.oa.sysmanage.entity.User;
import com.tz.oa.sysmanage.entity.UserToRole;

/**
 * 用户增删改查以及登陆验证的mapper代理接口
 * @author Administrator
 *
 */
public interface UserMapper {
	
	/**
	 * 根据条件查询用户列表
	 * @param user
	 * @return
	 */
	public List<UserDto> getUserDtoList(User user);
	
	/**
	 * 根据条件查询用户列表
	 * @param user
	 * @return
	 */
	public List<User> getUserList(User user);
	
	
	/**
	 * 通过用户ID获取用户信息
	 * @param userId
	 * @return
	 */
	public User getUserById(Long userId);
	
	
	/**
	 * 根据id获取用户明细 包含部门名称以及角色列表
	 * @param userId
	 * @return
	 */
	public UserDto getUserInfoById(Long userId);

	
	/**
	 * 修改用户密码
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean updateUserPassword(Long userId,String password);
	
	
	/**
	 * 更新用户个人信息 
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user);

	/**
	 * 增加用户
	 * @param user
	 * @return
	 */
	public boolean addUser(User user);

	/**
	 * 批量增加用户角色对应关系表
	 * @param userRoleList
	 * @return
	 */
	public boolean addUserRoleBatch(List<UserToRole> userRoleList);

	/**
	 * 删除用户角色对应关系表
	 * @param userId
	 * @return
	 */
	public boolean delUserRoleByUserId(Long userId);

	/**
	 * 删除用户信息
	 * @param userId
	 * @return
	 */
	public boolean delUser(Long userId);

	/**
	 * 查询某个用户拥有的角色信息
	 * @param userId
	 * @return
	 */
	public List<UserToRole> getUserRoleByUserId(Long userId);

}
