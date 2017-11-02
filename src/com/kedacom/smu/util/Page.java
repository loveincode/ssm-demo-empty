package com.kedacom.smu.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 当前页数
	 **/
	private int pageNo;
	/**
	 * 每页显示的数量
	 **/
	private int pageSize;

	private int offsets;
	/**
	 * 总条数
	 **/
	private int total;

	/**
	 * 存放集合
	 **/
	private List<T> rows = new ArrayList<T>();

	public Page() {

	}

	public Page(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.offsets = (pageNo - 1) * pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public void setOffsets(int offsets) {
		this.offsets = offsets;
	}

	// 计算开始位置
	public int getOffsets() {
		return (pageNo - 1) * pageSize;
	}

	// 计算总页数
	public int getTotalPages() {
		int totalPages;
		if (total % pageSize == 0) {
			totalPages = total / pageSize;
		} else {
			totalPages = (total / pageSize) + 1;
		}
		return totalPages;
	}

	// 计算最后一页
	public int getEndIndex() {
		if (getOffsets() + pageSize > total) {
			return total;
		} else {
			return getOffsets() + pageSize;
		}
	}
	
	@Override
	public String toString() {
		return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", offsets=" + offsets + ", total=" + total
				+ ", rows=" + rows + "]";
	}

}