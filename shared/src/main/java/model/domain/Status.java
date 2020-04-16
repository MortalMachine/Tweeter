package model.domain;

/*
import android.text.SpannableString;
import android.text.Spanned;
*/

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
import client.view.util.MentionClickableSpan;
*/

public class Status implements Serializable, Comparable<Status> {

    public String message;
/*
    private SpannableString spanMessage;
*/
    public long milliseconds;
    public List<StatusItem> mentions;
    //public List<StatusItem> urls;

    public Status() {
        this.setMilliseconds();
        mentions = new ArrayList<>();
        //urls = new ArrayList<>();
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setMilliseconds() {
        milliseconds = System.currentTimeMillis();
    }
    public void setMilliseconds(long m) {
        milliseconds = m;
    }
/*
    public void setRandomDate() {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(2015, 2019);
        milliseconds = Date.from(  LocalDate.of(year, month, day)
                                    .atStartOfDay(ZoneId.systemDefault())
                                    .toInstant());
    }
*/
    public void addMention(StatusItem item) {
        mentions.add(item);
    }
/*
    public void addURL(StatusItem item) {
        urls.add(item);
    }
*/
    public String getMessage() { return message; }
/*
    public SpannableString getSpanMessage() { return spanMessage; }
*/
    public long getMilliseconds() {
        return this.milliseconds;
    }
    public List<StatusItem> getMentions() {
        return this.mentions;
    }
/*
    public List<StatusItem> getURLs() {
        return this.urls;
    }
*/

/*
    private void makeMentionClickable(int startIndex, int endIndex) {
        if (spanMessage == null) {
            spanMessage = new SpannableString(message);
        }
        spanMessage.setSpan(new MentionClickableSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
*/

    public void findMentionsInStatus() throws Exception {
        if (message == null) return;

        int startIndex;
        int endIndex;
        int indexOf = 0;
        while ((indexOf = message.indexOf("@", indexOf)) > -1) {

            startIndex = indexOf;
            endIndex = message.indexOf(' ', startIndex + 1);
            String alias = message.substring(startIndex, endIndex);
            StatusItem mention = new Mention(alias);
            this.addMention(mention);
/*
            makeMentionClickable(startIndex, endIndex);
*/
        }
    }

    private static int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    @Override
    public int compareTo(Status status) {
        if (this.milliseconds > status.getMilliseconds()) return 1;
        else if (this.milliseconds == status.getMilliseconds()) return 0;
        else return -1;
    }
}
