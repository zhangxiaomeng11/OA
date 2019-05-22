package com.tz.oa.sysmanage.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.tz.oa.framework.dto.PageParam;
import com.tz.oa.sysmanage.Vo.UserVo;
import com.tz.oa.sysmanage.dto.UserDto;
import com.tz.oa.sysmanage.entity.User;
import com.tz.oa.sysmanage.entity.UserToRole;

/**
 * 用户业务处理接口
 * @author Administrator
 *
 */
public interface IUserService {

	/**
	 * 按条件分页查询用户列表
	 * @param user
	 * @param pageParam
	 * @return
	 */
	public PageInfo<UserDto> getUserDtoList(User user,PageParam pageParam);
	
	/**
	 * 登陆验证
	 * @param loginName
	 * @param password
	 * @return
	 */
	public User loginUser(String loginName,String password);
	
	/**
	 * 获取用户明细信息
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
	 * 校验密码
	 * @param plainPsd
	 * @param encryptPsd
	 * @return
	 */
	public boolean vaildatePassword(String plainPsd,String encryptPsd);
	
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
	 * 修改用户以及角色对应关系
	 * @param userVo
	 * @return
	 */
	public boolean updateUserVo(UserVo userVo);
	
	/**
	 * 增加用户信息包含用户角色对应信息
	 * @param userVo
	 */
	public boolean addUser(UserVo userVo);

	/**
	 * 删除用户和用户对应关系表
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
