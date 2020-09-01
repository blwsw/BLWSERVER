package com.hopedove.commons.vo;

import java.io.Serializable;

/**
 * 基础分页vo
 */
public class BasicPageVO implements Serializable {

    private Integer page_currentPage = 1;

    private Integer page_pageSize = 10;

    private Integer page_total = 0;

    private Integer page_pages = 1;

    private Integer page_startIndex = 0;

    private Integer page_pre;

    private Integer page_next;

    public BasicPageVO() {
    }

    public BasicPageVO(Integer page_currentPage) {
        this.setPage_currentPage(page_currentPage);
    }

    public BasicPageVO(Integer page_currentPage, Integer page_pageSize) {
        this.setPage_pageSize(page_pageSize);
        this.setPage_currentPage(page_currentPage);
    }

    public Integer getPage_currentPage() {
        return page_currentPage;
    }

    public void setPage_currentPage(Integer page_currentPage) {
        this.page_currentPage = page_currentPage;
        this.page_startIndex = (this.page_currentPage - 1) * this.page_pageSize;
    }

    public Integer getPage_pageSize() {
        return page_pageSize;
    }

    public void setPage_pageSize(Integer page_pageSize) {
        if (page_pageSize != null) {
            this.page_pageSize = page_pageSize;
        } else {
            this.page_pageSize = 10;
        }
    }

    public Integer getPage_total() {
        return page_total;
    }

    public void setPage_total(Integer page_total) {
        this.page_total = page_total;
        int n = page_total % this.page_pageSize;
        if (n > 0) {
            this.page_pages = page_total / this.page_pageSize + 1;
        } else {
            this.page_pages = page_total / this.page_pageSize;
        }

        if (this.page_currentPage < 1) {
            this.page_currentPage = 1;
        }

        if (this.page_pages > 0 && this.page_currentPage > this.page_pages) {
            this.page_currentPage = this.page_pages;
        }
    }

    public Integer getPage_pages() {
        return page_pages;
    }

    public void setPage_pages(Integer page_pages) {
        this.page_pages = page_pages;
    }

    public Integer getPage_startIndex() {
        return page_startIndex;
    }

    public void setPage_startIndex(Integer page_startIndex) {
        this.page_startIndex = page_startIndex;
    }

    public Integer getPage_pre() {
        if (page_currentPage != null && page_currentPage > 1) {
            page_pre = page_currentPage - 1;
        } else {
            page_pre = null;
        }
        return page_pre;
    }

    public void setPage_pre(Integer page_pre) {
        this.page_pre = page_pre;
    }

    public Integer getPage_next() {
        if (page_pages != null) {
            if (page_currentPage == page_pages || page_pages == 0) {
                page_next = null;
            } else {
                page_next = page_currentPage + 1;
            }
        }
        return page_next;
    }

    public void setPage_next(Integer page_next) {
        this.page_next = page_next;
    }
}
