package org.emma.catchupperiod.entitiesDTO;

import java.time.LocalDate;

public class LendingInfoDTO {
    private Integer lendingIdDto;
    private String isbnDto;
    private String userCodeDto;
    private LocalDate lendingDateDto;

    public Integer getLendingId() {
        return lendingIdDto;
    }

    public String getIsbn() {
        return isbnDto;
    }

    public String getUserCode() {
        return userCodeDto;
    }

    public LocalDate getLendingDate() {
        return lendingDateDto;
    }

    public void setLendingId(Integer lendingId) {
        this.lendingIdDto = lendingId;
    }

    public void setIsbn(String isbn) {
        this.isbnDto = isbn;
    }

    public void setUserCode(String userCode) {
        this.userCodeDto = userCode;
    }

    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDateDto = lendingDate;
    }
}
