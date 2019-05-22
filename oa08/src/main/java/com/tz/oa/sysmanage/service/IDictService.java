package com.tz.oa.sysmanage.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.tz.oa.framework.dto.PageParam;
import com.tz.oa.sysmanage.entity.Dict;

/**
 * 字典相关业务层接口
 * @author Administrator
 *
 */
public interface IDictService {

	/**
	 * 获取所有字典类型
	 * @return
	 */
	public List<String> getAllDictType() ;
 
	/**
	 * 根据条件查询字典列表
	 * @param dict
	 * @return
	 */
	public List<Dict> getDictList(Dict dict);
	
	/**
	 * 分页查询字典列表
	 * @param dict
	 * @param pageParam
	 * @return
	 */
	public PageInfo<Dict> getDictListPage(Dict dict,PageParam pageParam);
	
	/**
	 * 通过字典id获取字典明细信息
	 * @param dictId
	 * @return
	 */
	public Dict getDictById(Long dictId);
	
	/**
	 * 修改字典
	 * @param dict
	 * @return
	 */
	public boolean updateDict(Dict dict);
	
	
	/**
	 * 增加字典
	 * @param dict
	 * @return
	 */	
	public boolean addDict(Dict dict);
	
	/**
	 * 删除字典
	 * @param dictId
	 * @return
	 */
	public boolean delDict(Long dictId);
}
