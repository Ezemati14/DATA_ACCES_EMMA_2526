package org.emma.catchupperiod.entitiesDTO;

import java.time.LocalDate;

public class LendingDTO {
    private String isbnDto;
    private String titleDto;
    private String userCodeDto;
    private String userNameDto;
    private LocalDate lendingDateDto;
    private LocalDate dueDateDto;
    private boolean delayedDto;

    public String getIsbnDto() {
        return isbnDto;
    }

    public String getTitleDto() {
        return titleDto;
    }

    public String getUserCodeDto() {
        return userCodeDto;
    }

    public String getUserNameDto() {
        return userNameDto;
    }

    public LocalDate getLendingDateDto() {
        return lendingDateDto;
    }

    public LocalDate getDueDateDto() {
        return dueDateDto;
    }

    public boolean isDelayedDto() {
        return delayedDto;
    }

    public void setIsbnDto(String isbnDto) {
        this.isbnDto = isbnDto;
    }

    public void setTitleDto(String titleDto) {
        this.titleDto = titleDto;
    }

    public void setUserCodeDto(String userCodeDto) {
        this.userCodeDto = userCodeDto;
    }

    public void setUserNameDto(String userNameDto) {
        this.userNameDto = userNameDto;
    }

    public void setLendingDateDto(LocalDate lendingDateDto) {
        this.lendingDateDto = lendingDateDto;
    }

    public void setDueDateDto(LocalDate dueDateDto) {
        this.dueDateDto = dueDateDto;
    }

    public void setDelayedDto(boolean delayedDto) {
        this.delayedDto = delayedDto;
    }
}
