package response;

import java.io.Serializable;

public class Response implements Serializable {

    public  boolean success;
    public String message;

    public Response() {}
    public Response(boolean success) {
        this(success, null);
    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }
}
