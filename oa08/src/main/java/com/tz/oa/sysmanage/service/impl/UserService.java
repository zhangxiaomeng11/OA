package com.tz.oa.sysmanage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tz.oa.framework.dto.PageParam;
import com.tz.oa.framework.util.EncryptUtil;
import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.Vo.UserVo;
import com.tz.oa.sysmanage.dto.UserDto;
import com.tz.oa.sysmanage.entity.User;
import com.tz.oa.sysmanage.entity.UserToRole;
import com.tz.oa.sysmanage.mapper.UserMapper;
import com.tz.oa.sysmanage.service.IUserService;

@Service
public class UserService implements IUserService{

	public static final int SALT_SIZE = 8;
	public static final int HASH_ITERATIONS = 1024;
	@Autowired
	private UserMapper userMapper;
	
	public User loginUser(String loginName, String password) {
		//1 :先根据loginName查出用户对象
		//2 :将界面传过来的明文密码 根据既定的规则(密文截取盐和迭代次数,然后加上明文,进行加密)进行加密
		//3: 将第二步得到的密文和数据库的密文进行比对 ,如果成功则验证成功
		User user = new User();user.setLoginName(loginName);
		List<User> userList = userMapper.getUserList(user);
		if(userList.isEmpty()){
			return null;
		}else{//进行密码比对
			String ecnryptPassword = userList.get(0).getPassword();
			if(vaildatePassword(password,ecnryptPassword))
				return userList.get(0);		
			else
				return null;
		}
	}
	
	/**
	 * 校验密码是否有效
	 * @param plainPsd
	 * @param encryptPsd
	 * @return
	 */
	public boolean vaildatePassword(String plainPsd,String encryptPsd){
		//将密文逆转,截取salt
		byte[] salt = EncryptUtil.decodeHex(encryptPsd.substring(0,SALT_SIZE*2));
		//重新平凑 盐+密码  进行sha1的加密
		byte[] hashPassword = EncryptUtil.sha1(plainPsd.getBytes(), salt, HASH_ITERATIONS);
		String newEncrypPsd = EncryptUtil.encodeHex(salt) + EncryptUtil.encodeHex(hashPassword);
		boolean flag = false; 
		flag = newEncrypPsd.equals(encryptPsd);
		return flag;
		 
	}
	
 	public User getUserById(Long userId) {
		return userMapper.getUserById(userId);
	}

	public boolean updateUserPassword(Long userId, String password) {
		 String encryptPsd = this.encyptPassword(password);
		 return userMapper.updateUserPassword(userId, encryptPsd);
	}
	
	
	/**
     * 对密码进行加密 SHA-1
     * @param plainPassword 明文密码
     * @return
     */
    public String encyptPassword (String plainPassword) {
    	//生成一个随机数 ，所谓的salt 盐
        byte[] salt = EncryptUtil.generateSalt(SALT_SIZE);
        //盐+密码   进行sha1的加密
        byte[] hashPass = EncryptUtil.sha1(plainPassword.getBytes(), salt, HASH_ITERATIONS);
        //盐可逆加密+(盐+密码 sha1加密后)可逆加密
        return EncryptUtil.encodeHex(salt) + EncryptUtil.encodeHex(hashPass);
    }

	public UserDto getUserInfoById(Long userId) {
		return userMapper.getUserInfoById(userId);
	}

	public boolean updateUser(User user) {
		return userMapper.updateUser(user);
	}

	public PageInfo<UserDto> getUserDtoList(User user, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		List<UserDto> userList = this.userMapper.getUserDtoList(user);
		PageInfo<UserDto> pageInfo = new PageInfo<UserDto>(userList);
		
		return pageInfo;
	}
	
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean addUser(UserVo userVo) {
		boolean flag = false;
		//增加的时候 ,注意填充当前操作的用户和实践
		User user = userVo.getUser();
		user.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		user.setUpdateDate(new Date());
		//每次新增用户,其实是有一个默认密码
		String password = "123";
		String encryptPassword = this.encyptPassword(password);
		user.setPassword(encryptPassword);
		flag = this.userMapper.addUser(user);
		Long userId = user.getUserId();
	
		//用户增加成功以后,需要讲用户和角色的对应关系放入到pm_sys_user_role
		//批量插入用户角色信息对应表
 		List<UserToRole> userRoleList = new ArrayList<UserToRole>();
  		UserToRole userRole;
 		for (Long roleId : userVo.getRoleIds().values()) {  		  
        	userRole = new UserToRole();
        	userRole.setUserId(userId);
        	userRole.setRoleId(roleId);
        	userRoleList.add(userRole);
         }
        flag = this.userMapper.addUserRoleBatch(userRoleList);		
		return flag;
	} 
	

	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public boolean delUser(Long userId) {
		boolean flag = false;
		flag = this.userMapper.delUserRoleByUserId(userId);
		flag = this.userMapper.delUser(userId);		
		return flag;		
	}

	public List<UserToRole> getUserRoleByUserId(Long userId) {
		return this.userMapper.getUserRoleByUserId(userId);
		
	}

	public boolean updateUserVo(UserVo userVo) {
		boolean flag = false;
		//修改的时候注意填充修改人和修改时间	
		User user = userVo.getUser();
		user.setUpdateDate(new Date());
		user.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		
		Long userId = user.getUserId();		 
		flag = userMapper.delUserRoleByUserId(userId);
		//批量插入用户角色信息对应表
 		List<UserToRole> userRoleList = new ArrayList<UserToRole>();
  		UserToRole userRole;
 		for (Long roleId : userVo.getRoleIds().values()) {  		  
        	userRole = new UserToRole();
        	userRole.setUserId(userId);
        	userRole.setRoleId(roleId);
        	userRoleList.add(userRole);
         }
        flag = this.userMapper.addUserRoleBatch(userRoleList);
        
		flag =  userMapper.updateUser(user);
				
		return flag;
	}
	
	

}
