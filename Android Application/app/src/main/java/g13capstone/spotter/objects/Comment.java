package g13capstone.spotter.objects;

/**
 * Created by Vital on 2018-01-16.
 */

public class Comment {
    private String userName;
    private String logTime;
    private String comment;

    public Comment(){
        userName="";
        logTime="";
        comment="";
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getLogTime() { return logTime; }

    public void setLogTime(String logTime) { this.logTime = logTime; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }
}
