package page;

import java.util.List;

import planeBean.FlightModel;

public class PageBean {
    public static final int INIT_PAGE_NO = 1;
    public static final int INIT_PAGE_SIZE = 23;

    private int pageNo = 1;// 当前页码
    private int pageSize = 20;// 每页显示记录数
    private int pageCount;// 总页数
    private int totalCount;// 总记录数
    private int start;
    private int limit;
    private List<FlightModel> list;// 记录结果集


    public PageBean() {
    }

    public PageBean(int pageNo, int pageSize) {
        this.pageNo = pageNo > 1 ? pageNo : 1;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    public List<FlightModel> getList() {
        return list;
    }

    public void setList(List<FlightModel> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 设置
     *
     * @param totalCount
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.pageCount = totalCount % pageSize == 0 ? totalCount / pageSize
                : totalCount / pageSize + 1;
        if (pageNo > pageCount) {    //pageNo 当前页码
            pageNo = pageCount;
        }
        start = (pageNo - 1) * pageSize + 1;
        limit = start + pageSize - 1;
        limit = limit <= totalCount ? limit : totalCount;
    }

}