package ssd8.rmi.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * meeting对象
 * @author Patchouli
 * @version 1.1
 */
public class Meeting implements Serializable{

	private int meetingID;
	private User host;
    private ArrayList<User> participant = new ArrayList<User>();
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d-k:m");
    private Date startTime;
    private Date endTime;
    private String title;
    /**
     * 构造函数
     * @param meetingID 会议ID
     * @param host 举办会议者
     * @param participant 会议参与人员
     * @param startTime 会议开始时间
     * @param endTime 会议结束时间
     * @param title 会议主题
     */
    public Meeting(int meetingID, User host, ArrayList<User> participant, Date startTime, Date endTime, String title) {
        this.meetingID = meetingID;
        this.host = host;
        this.participant = participant;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    /**
     * 
     * @return 返回会议ID
     */
    public int getMeetingID() {
        return meetingID;
    }

    /**
     * 设置会议ID
     * @param meetingID 会议ID
     */
    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }

    /**
     * 
     * @return 返回会议举办者
     */
    public User getHost() {
        return host;
    }

    /**
     * 
     * 设置会议举办者
     * @param host 会议举办者
     */
    public void setHost(User host) {
        this.host = host;
    }

    /**
     * 
     * @return 返回会议参与人员列表
     */
    public ArrayList<User> getParticipant() {
        return participant;
    }

    /**
     * 设置会议参与人员列表
     * @param participant 会议参与人员列表
     */
    public void setParticipant(ArrayList<User> participant) {
        this.participant = participant;
    }

    /**
     * 
     * @return 返回会议开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置会议开始时间
     * @param startTime 会议开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return 返回会议结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置会议结束时间
     * @param endTime 会议结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     * @return 返回会议主题
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * 设置会议主题
     * @param title 会议主题
     */
    public void setTitle(String title) {
        this.title = title;
    }

	@Override
	public String toString() {
		return "Meeting [meetingID=" + meetingID + ",\n host=" + host.toString() + ",\n participant=" + participant.toString() + ",\n startTime="
				+ startTime.toString() + ",\n endTime=" + endTime.toString() + ",\n title=" + title + "]";
	}

}
