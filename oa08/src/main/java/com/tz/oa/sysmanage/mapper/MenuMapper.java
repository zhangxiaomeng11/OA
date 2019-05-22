package com.tz.oa.sysmanage.mapper;

import java.util.List;
import com.tz.oa.sysmanage.entity.Menu;

/**
 * 菜单的增删改查的mapper代理接口
 * @author Administrator
 *
 */
public interface MenuMapper {
	
	/**
	 * 查询用户权限控制内的所有菜单
	 * @param userId
	 * @return
	 */
	public List<Menu> getMenuListByUserId(Long userId);
	
	/**
	 * 查询所有菜单列表
	 * @return
	 */
	public List<Menu> getAllMenuList();
	
	/**
	 * 获取某个节点的子节点数目,用于删除的特殊判断
	 * @param menuId
	 * @return
	 */
	public Integer getChildCount(Long menuId);
	
	/**
	 * 查询菜单明细
	 * @return
	 */
	public Menu getMenuById(Long menuId);
	/**
	 * 增加菜单
	 * @param menu
	 * @return
	 */
	public boolean addMenu(Menu menu);
	/**
	 * 删除菜单
	 * @param menuId
	 * @return
	 */
	public boolean delMenu(Long menuId);
	
	/**
	 * 修改菜单
	 * @param menu
	 * @return
	 */
	public boolean updateMenu(Menu menu);
	
	
}
