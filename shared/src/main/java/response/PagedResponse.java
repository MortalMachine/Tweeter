package response;

import java.io.Serializable;

public class PagedResponse extends Response implements Serializable {

    public boolean hasMorePages;

    public PagedResponse(boolean success, boolean hasMorePages) {
        super(success);
        this.hasMorePages = hasMorePages;
    }

    public PagedResponse(boolean success, String message, boolean hasMorePages) {
        super(success, message);
        this.hasMorePages = hasMorePages;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public PagedResponse() {}

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }
}
