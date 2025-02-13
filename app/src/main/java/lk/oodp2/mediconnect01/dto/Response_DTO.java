package lk.oodp2.mediconnect01.dto;

public class Response_DTO<T> {
    private Boolean success;

    private T content;

    private String message;
    

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean status) {
        this.success = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
