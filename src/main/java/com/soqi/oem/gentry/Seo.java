package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Seo implements Serializable {
    private Long taskid;

    private Integer userid;

    private String keyword;

    private String url;

    private String seourl;

    private Integer searchtype;

    private Integer rankfirst;

    private Integer ranklast;

    private Integer ranklastchange;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rankupdatetime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;

    private Integer fromtype;

    private Long fromid;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buytime;

    private BigDecimal freezeamount;

    private Integer costcount;

    private BigDecimal costamount;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date costtime;

    private Integer settlehour;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date settletime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date settlestart;

    private Long apipricetaskid;

    private Long apiranktaskid;

    private Long apiwatchtaskid;

    private String errormsg;

    private Integer status;

    private BigDecimal priceoem;
    
    private BigDecimal priceori;
    
    private BigDecimal price;
    
    private static final long serialVersionUID = 1L;

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getSeourl() {
        return seourl;
    }

    public void setSeourl(String seourl) {
        this.seourl = seourl == null ? null : seourl.trim();
    }

    public Integer getSearchtype() {
        return searchtype;
    }

    public void setSearchtype(Integer searchtype) {
        this.searchtype = searchtype;
    }

    public Integer getRankfirst() {
        return rankfirst;
    }

    public void setRankfirst(Integer rankfirst) {
        this.rankfirst = rankfirst;
    }

    public Integer getRanklast() {
        return ranklast;
    }

    public void setRanklast(Integer ranklast) {
        this.ranklast = ranklast;
    }

    public Integer getRanklastchange() {
        return ranklastchange;
    }

    public void setRanklastchange(Integer ranklastchange) {
        this.ranklastchange = ranklastchange;
    }

    public Date getRankupdatetime() {
        return rankupdatetime;
    }

    public void setRankupdatetime(Date rankupdatetime) {
        this.rankupdatetime = rankupdatetime;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getFromtype() {
        return fromtype;
    }

    public void setFromtype(Integer fromtype) {
        this.fromtype = fromtype;
    }

    public Long getFromid() {
        return fromid;
    }

    public void setFromid(Long fromid) {
        this.fromid = fromid;
    }

    public Date getBuytime() {
        return buytime;
    }

    public void setBuytime(Date buytime) {
        this.buytime = buytime;
    }

    public BigDecimal getFreezeamount() {
        return freezeamount;
    }

    public void setFreezeamount(BigDecimal freezeamount) {
        this.freezeamount = freezeamount;
    }

    public Integer getCostcount() {
        return costcount;
    }

    public void setCostcount(Integer costcount) {
        this.costcount = costcount;
    }

    public BigDecimal getCostamount() {
        return costamount;
    }

    public void setCostamount(BigDecimal costamount) {
        this.costamount = costamount;
    }

    public Date getCosttime() {
        return costtime;
    }

    public void setCosttime(Date costtime) {
        this.costtime = costtime;
    }

    public Integer getSettlehour() {
        return settlehour;
    }

    public void setSettlehour(Integer settlehour) {
        this.settlehour = settlehour;
    }

    public Date getSettletime() {
        return settletime;
    }

    public void setSettletime(Date settletime) {
        this.settletime = settletime;
    }

    public Date getSettlestart() {
        return settlestart;
    }

    public void setSettlestart(Date settlestart) {
        this.settlestart = settlestart;
    }

    public Long getApipricetaskid() {
        return apipricetaskid;
    }

    public void setApipricetaskid(Long apipricetaskid) {
        this.apipricetaskid = apipricetaskid;
    }

    public Long getApiranktaskid() {
        return apiranktaskid;
    }

    public void setApiranktaskid(Long apiranktaskid) {
        this.apiranktaskid = apiranktaskid;
    }

    public Long getApiwatchtaskid() {
        return apiwatchtaskid;
    }

    public void setApiwatchtaskid(Long apiwatchtaskid) {
        this.apiwatchtaskid = apiwatchtaskid;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg == null ? null : errormsg.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public BigDecimal getPriceoem() {
		return priceoem;
	}

	public void setPriceoem(BigDecimal priceoem) {
		this.priceoem = priceoem;
	}

	public BigDecimal getPriceori() {
		return priceori;
	}

	public void setPriceori(BigDecimal priceori) {
		this.priceori = priceori;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", taskid=").append(taskid);
        sb.append(", userid=").append(userid);
        sb.append(", keyword=").append(keyword);
        sb.append(", url=").append(url);
        sb.append(", seourl=").append(seourl);
        sb.append(", searchtype=").append(searchtype);
        sb.append(", rankfirst=").append(rankfirst);
        sb.append(", ranklast=").append(ranklast);
        sb.append(", ranklastchange=").append(ranklastchange);
        sb.append(", rankupdatetime=").append(rankupdatetime);
        sb.append(", addtime=").append(addtime);
        sb.append(", fromtype=").append(fromtype);
        sb.append(", fromid=").append(fromid);
        sb.append(", buytime=").append(buytime);
        sb.append(", freezeamount=").append(freezeamount);
        sb.append(", costcount=").append(costcount);
        sb.append(", costamount=").append(costamount);
        sb.append(", costtime=").append(costtime);
        sb.append(", settlehour=").append(settlehour);
        sb.append(", settletime=").append(settletime);
        sb.append(", settlestart=").append(settlestart);
        sb.append(", apipricetaskid=").append(apipricetaskid);
        sb.append(", apiranktaskid=").append(apiranktaskid);
        sb.append(", apiwatchtaskid=").append(apiwatchtaskid);
        sb.append(", errormsg=").append(errormsg);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}