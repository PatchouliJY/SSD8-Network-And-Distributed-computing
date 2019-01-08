package server;

import java.util.Date;

public class Project {

	private String creator;
	private Date startTime;
	private Date endTime;
	private String title;
	private int projectID;

	/**
	 * 构造函数
	 */
	public Project() {
		super();
	}
	
	/**
	 * 带参构造函数
	 * @param creator 项目创建者
	 * @param startTime 项目开始时间
	 * @param endTime 项目结束时间
	 * @param title 项目标题
	 * @param projectID 项目ID
	 */
	public Project(String creator, Date startTime, Date endTime, String title, int projectID) {
		super();
		this.creator = creator;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.projectID = projectID;
	}

	/**
	 * 返回项目创建者
	 * @return 项目创建者
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * 设置创建者名字
	 * @param creator 创建者名字
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * 返回项目开始时间
	 * @return 项目开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 设置项目开始时间
	 * @param startTime 项目开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 返回项目结束时间
	 * @return 项目结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 设置项目结束时间
	 * @param endTime 项目结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 返回项目标题
	 * @return 项目标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置项目标题
	 * @param title 项目标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 返回项目ID
	 * @return 项目ID
	 */
	public int getProjectID() {
		return projectID;
	}

	/**
	 * 设置项目ID
	 * @param projectID 项目ID
	 */
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	/**
	 * toString函数
	 */
	@Override
	public String toString() {
		return "Project [creator=" + creator + ", startTime=" + startTime + ", endTime=" + endTime + ", title=" + title
				+ ", ID=" + projectID + "]";
	}

}
