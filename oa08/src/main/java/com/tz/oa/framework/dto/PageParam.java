package com.tz.oa.framework.dto;

/**
 * 页面传输过来的分页对象
 * @author Administrator
 *
 */
public class PageParam {
	//第几页
	private Integer pageNo=1;
	//每页多少条
	private Integer pageSize=10;
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
	
}
