package pv260.solid.ocp.original;

import java.util.Date;

public class Comment {

    private String headline;

    private String text;

    private Date entered;

    private String author;

    public Comment(String headline,
                   String text,
                   Date entered,
                   String author) {
        this.headline = headline;
        this.text = text;
        this.entered = entered;
        this.author = author;
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public Date getEntered() {
        return entered;
    }

    public String getAuthor() {
        return author;
    }

}
