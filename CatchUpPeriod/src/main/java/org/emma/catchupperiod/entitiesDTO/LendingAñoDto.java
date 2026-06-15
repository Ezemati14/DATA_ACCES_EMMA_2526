package org.emma.catchupperiod.entitiesDTO;

import java.time.LocalDate;

public class LendingAñoDto {
    private String isbnDto;
    private String userCodeDto;
    private LocalDate lendingDateDto;
    private LocalDate returningDateDto;

    public void setIsbn(String isbn) {
        this.isbnDto = isbn;
    }

    public void setUserCode(String userCode) {
        this.userCodeDto = userCode;
    }

    public void setLendingDate(LocalDate lendingDate) {
        this.lendingDateDto = lendingDate;
    }

    public void setReturningDate(LocalDate returningDate) {
        this.returningDateDto = returningDate;
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

    public LocalDate getReturningDate() {
        return returningDateDto;
    }
}
