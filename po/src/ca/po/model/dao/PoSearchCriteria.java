package ca.po.model.dao;

import java.util.Date;

public class PoSearchCriteria {
    
    //these are immutable
    private final Long pageBegin;
    private final Integer pageSize;

    //these are mutable
    private Long userId;
    private Long labId;
    private Date fromDate;
    private Date toDate;
    
    public PoSearchCriteria(){
        this.pageBegin=null;
        this.pageSize=null;
    }
    
    public PoSearchCriteria(Long pageBegin, Integer pageSize){
        this.pageBegin=pageBegin;
        this.pageSize=pageSize;
    }

    
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Long getLabId() {
        return labId;
    }
    public void setLabId(Long labId) {
        this.labId = labId;
    }
    public Date getToDate() {
        return toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPageBegin() {
        return pageBegin;
    }
    public Integer getPageSize() {
        return pageSize;
    }
}
