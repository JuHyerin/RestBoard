package com.innilabs.restboard.util;

import lombok.Data;

@Data
public class PagingUtil {
    private int pageNo; //현재페이지
    private int pageSize; //페이지 당 행 갯수
    private int blockSize; //페이지블럭 당 페이지 갯수
    private int firstData; //페이지마다 첫번째로 출력할 데이터 
    private int startPageNo; //페이지블럭 시작 페이지
    private int endPageNo; //페이지블럭 끝 페이지
    private int totalPages; //총 페이지 수
    private int totalData; //총 데이터 수 
    private int startData; //페이지 시작 행
    
    public PagingUtil(int page, int pageSize, int blockSize) {
        this.pageNo = page;
        this.pageSize = pageSize;
        this.blockSize = blockSize;
    }
    
    public void setTotalData(int totalData) {
        this.totalData = totalData;
        this.makePaging();
    }

    public void makePaging() {
        //기본설정
        if(this.totalData == 0) {//게시글이 없는 경우
            return;
        }
        this.totalPages = (this.totalData + (this.pageSize-1)) / this.pageSize; //총페이지 
        this.setFirstData((this.pageNo - 1) * this.pageSize); //출력될 첫 데이터
        
        try {
            this.setStartPageNo(1 + ((this.pageNo - 1) / this.blockSize) * this.blockSize) ; 
        }catch (ArithmeticException e) {//ArithmeticException: / by zero 처리
            this.setStartPageNo(1);		
        }
        
        int endPage = this.startPageNo + (this.blockSize - 1); //블럭의 마지막페이지 구하기
        if(endPage > this.totalPages) { //마지막 블럭: 총페이지가 맨 마지막페이지에 도달하기 전에 끝남
            endPage = this.totalPages;
        }
        this.setEndPageNo(endPage);//최종 블럭끝페이지 설정
        
    }
    
}