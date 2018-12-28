package ssd8.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ssd8.rmi.bean.Meeting;
import ssd8.rmi.bean.User;
import ssd8.rmi.rface.MeetingInterface;

public class MeetingInterfaceImplement extends UnicastRemoteObject implements MeetingInterface {

	protected MeetingInterfaceImplement() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private ArrayList<Meeting> meetingList = new ArrayList<Meeting>();// 会议列表

	@Override
	public ArrayList<Meeting> getMeetingList() throws RemoteException {
		return this.meetingList;
	}

	@Override
	public boolean addMeeting(Meeting meeting) throws RemoteException {
		//检测时间是否与已存在会议重叠
		if (meetingList.isEmpty() == false) {// 如果当前会议列表为空，直接添加
			for (Meeting temp : meetingList) {
				if((meeting.getStartTime().before(temp.getStartTime()) && meeting.getEndTime().before(temp.getStartTime()))
						|| (meeting.getStartTime().after(temp.getEndTime()) && meeting.getEndTime().after(temp.getEndTime())))
					continue;
				else return false;
			}
		}
		this.meetingList.add(meeting);// 添加会议
		return true;// 返回结果，添加成功
	}
	
	@Override
	public ArrayList<Meeting> queryMeetingByTime(Date startTime, Date endTime)
			throws RemoteException {

		if (meetingList.isEmpty() == true)
			return null;// 当前会议列表为空，直接退出方法
		
		ArrayList<Meeting> query = new ArrayList<Meeting>();// 结果列表
		//查找[startTime,endTime]闭区间内的所有会议
		for (Meeting meeting : meetingList) {
			if (!meeting.getStartTime().before(startTime)
					&& !meeting.getEndTime().after(endTime)) {//闭区间表示，不能使用meeting.getStartTime().after(endTime)&& eeting.getEndTime().after(endTime)
				query.add(meeting);
			}
		}
		return query;// 返回结果
	}

	@Override
	public void clearMeeting(User user) throws RemoteException {
		
		String host = user.getName();
		ArrayList<Meeting> newMeetingList = new ArrayList<Meeting>();
		
		for(Meeting temp : this.meetingList) {
			if(!temp.getHost().getName().equals(host)) {
				newMeetingList.add(temp);
			}
		}
		this.meetingList = newMeetingList;
	}

	@Override
	public boolean deleteMeetingById(int meetingID, User user) throws RemoteException {
		for (Meeting temp : meetingList) {
			if (temp.getMeetingID() == meetingID) {// ID符合
				User check = temp.getHost();
				if(check.getName().equals(user.getName())){// 执行操作的人是创建会议的人
					meetingList.remove(temp);// 移除会议
					return true;// 返回结果，删除成功
				}
			}
		}
		return false;// 返回结果，删除失败
	}
}
