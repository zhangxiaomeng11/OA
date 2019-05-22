package com.tz.oa.sysmanage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tz.oa.framework.dto.PageParam;
import com.tz.oa.framework.util.UserUtils;
import com.tz.oa.sysmanage.entity.Dict;
import com.tz.oa.sysmanage.mapper.DictMapper;
import com.tz.oa.sysmanage.service.IDictService;

@Service
public class DictService implements IDictService{

	@Autowired
	private DictMapper dictMapper;
	
	public List<String> getAllDictType() {
		return dictMapper.getAllDictType();
	}

	public List<Dict> getDictList(Dict dict) {
		return dictMapper.getDictList(dict);
	}
	
	public PageInfo<Dict> getDictListPage(Dict dict,PageParam pageParam){
		PageHelper.startPage(pageParam.getPageNo(), pageParam.getPageSize());
 		List<Dict> dictList = this.dictMapper.getDictList(dict);
		PageInfo<Dict> pageInfo = new PageInfo<Dict>(dictList);
		
		return pageInfo;
	}
	
	
	public Dict getDictById(Long dictId) {
		return dictMapper.getDictById(dictId);
	}

	public boolean updateDict(Dict dict) {
		//修改的时候注意填充修改人和修改时间
		dict.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			dict.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}	
		return dictMapper.updateDict(dict);
	}

	public boolean addDict(Dict dict) {
		dict.setUpdateDate(new Date());
		if(UserUtils.getCurrrentUserId()!=null){
			dict.setUpdateBy(UserUtils.getCurrrentUserId().toString());
		}	
		return dictMapper.addDict(dict);
	}

	public boolean delDict(Long dictId) {
		return dictMapper.delDict(dictId);
	}

	



}
