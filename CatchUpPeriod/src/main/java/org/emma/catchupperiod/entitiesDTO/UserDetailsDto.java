package org.emma.catchupperiod.entitiesDTO;

import java.time.LocalDate;

public class UserDetailsDto {
    private String userCodeDto;
    private String userNameDto;
    private String userEmailDto;

    private String isbnDto;
    private String bookTitleDto;

    private LocalDate retturningDateDto;

    public String getUserCodeDto() {
        return userCodeDto;
    }

    public void setUserCodeDto(String userCodeDto) {
        this.userCodeDto = userCodeDto;
    }

    public String getUserNameDto() {
        return userNameDto;
    }

    public String getUserEmailDto() {
        return userEmailDto;
    }

    public String getIsbnDto() {
        return isbnDto;
    }

    public String getBookTitleDto() {
        return bookTitleDto;
    }

    public LocalDate getRetturningDateDto() {
        return retturningDateDto;
    }

    public void setUserNameDto(String userNameDto) {
        this.userNameDto = userNameDto;
    }

    public void setUserEmailDto(String userEmailDto) {
        this.userEmailDto = userEmailDto;
    }

    public void setIsbnDto(String isbnDto) {
        this.isbnDto = isbnDto;
    }

    public void setBookTitleDto(String bookTitleDto) {
        this.bookTitleDto = bookTitleDto;
    }

    public void setRetturningDateDto(LocalDate retturningDateDto) {
        this.retturningDateDto = retturningDateDto;
    }
}
