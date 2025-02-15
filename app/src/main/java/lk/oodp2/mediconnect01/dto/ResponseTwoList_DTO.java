package lk.oodp2.mediconnect01.dto;

import java.util.List;

public class ResponseTwoList_DTO <T,Y>{

    private Boolean success;
    private List<T> content;
    private List<Y> content2;
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public List<Y> getContent2() {
        return content2;
    }

    public void setContent2(List<Y> content2) {
        this.content2 = content2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
