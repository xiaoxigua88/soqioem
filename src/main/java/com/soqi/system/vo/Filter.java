package com.soqi.system.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Filter {
	private String orderName;
	private String orderType;
	private Integer searchType;
	private Long taskId;
	private String likeField;
	//1：包含，2：等于，3：不包含，4：以开头，5以结尾
	private Integer keywordLike;
	private String keyword;
	private String url;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date minBuyTime;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date maxBuyTime;
	private Integer settleStatus;
	private Integer status;
	
	public Filter(){
		
	}
	
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Integer getSearchType() {
		return searchType;
	}
	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getLikeField() {
		return likeField;
	}

	public void setLikeField(String likeField) {
		this.likeField = likeField == null? null: likeField.trim();
	}

	public Integer getKeywordLike() {
		return keywordLike;
	}

	public void setKeywordLike(Integer keywordLike) {
		this.keywordLike = keywordLike;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getMinBuyTime() {
		return minBuyTime;
	}

	public void setMinBuyTime(Date minBuyTime) {
		this.minBuyTime = minBuyTime;
	}

	public Date getMaxBuyTime() {
		return maxBuyTime;
	}

	public void setMaxBuyTime(Date maxBuyTime) {
		this.maxBuyTime = maxBuyTime;
	}

	public Integer getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(Integer settleStatus) {
		this.settleStatus = settleStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
