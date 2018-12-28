package ssd8.rmi.rface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import ssd8.rmi.bean.Meeting;
import ssd8.rmi.bean.User;

public interface MeetingInterface extends Remote {

	/**
	 * 添加用户，并返回是否添加成功
	 * @param meeting 待添加会议
	 * @return 返回是或否添加成功
	 * @throws RemoteException
	 */
	boolean addMeeting(Meeting meeting) throws RemoteException;

	/**
	 * 返回会议查询结果
	 * @param startTime 查询开始时间
	 * @param endTime 查询结束时间
	 * @return 返回会议查询结果
	 * @throws RemoteException
	 */
	ArrayList<Meeting> queryMeetingByTime(Date startTime, Date endTime)throws RemoteException;

	/**
	 * 根据ID删除会议，并返回是否删除成功
	 * @param meetingID 待会议ID
	 * @param user 会议主持者
	 * @return 返回是否删除成功
	 * @throws RemoteException
	 */
	boolean deleteMeetingById(int meetingID, User user) throws RemoteException;

	/**
	 * 清除当前用户所建立的会议
	 * @throws RemoteException
	 */
	void clearMeeting(User user) throws RemoteException;

	/**
	 * 返回会议列表
	 * @return 返回会议列表
	 * @throws RemoteException
	 */
	ArrayList<Meeting> getMeetingList() throws RemoteException;

}
