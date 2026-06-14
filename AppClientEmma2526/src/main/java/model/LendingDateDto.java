package model;

import java.time.LocalDate;

public class LendingDateDto {
    private String isbnDto;
    private String titleDto;
    private String userCodeDto;
    private String userNameDto;
    private LocalDate lendingDateDto;
    private LocalDate dueDateDto;
    private boolean delayedDto;


    public void setTitleDto(String titleDto) {
        this.titleDto = titleDto;
    }

    public void setUserNameDto(String userNameDto) {
        this.userNameDto = userNameDto;
    }

    public void setDelayedDto(boolean delayedDto) {
        this.delayedDto = delayedDto;
    }

    public String getTitleDto() {
        return titleDto;
    }

    public String getUserNameDto() {
        return userNameDto;
    }

    public boolean isDelayedDto() {
        return delayedDto;
    }

    public void setIsbnDto(String isbnDto) {
        this.isbnDto = isbnDto;
    }

    public void setLendingDateDto(LocalDate lendingDateDto) {
        this.lendingDateDto = lendingDateDto;
    }

    public void setUserCodeDto(String userCodeDto) {
        this.userCodeDto = userCodeDto;
    }

    public void setDueDateDto(LocalDate dueDateDto) {
        this.dueDateDto = dueDateDto;
    }

    public void setDelayedDto(Boolean delayedDto) {
        this.delayedDto = delayedDto;
    }

    public String getIsbnDto() {
        return isbnDto;
    }

    public String getUserCodeDto() {
        return userCodeDto;
    }

    public LocalDate getLendingDateDto() {
        return lendingDateDto;
    }

    public LocalDate getDueDateDto() {
        return dueDateDto;
    }

    public Boolean getDelayedDto() {
        return delayedDto;
    }
}
