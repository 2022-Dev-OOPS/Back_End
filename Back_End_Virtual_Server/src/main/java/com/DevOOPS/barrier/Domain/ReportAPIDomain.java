package com.DevOOPS.barrier.Domain;

public class ReportAPIDomain {

        private int idx;
        private int stnId;
        private String title;
        private String tmFc;
        private int tmSeq;
        private int pageNo;
        private int numOfRows;
        private int totalCount;

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public int getStnId() {
            return stnId;
        }

        public void setStnId(int stnId) {
            this.stnId = stnId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTmFc() {
            return tmFc;
        }

        public void setTmFc(String tmFc) {
            this.tmFc = tmFc;
        }

        public int getTmSeq() {
            return tmSeq;
        }

        public void setTmSeq(int tmSeq) {
            this.tmSeq = tmSeq;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }
