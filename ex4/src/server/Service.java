package server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(name = "myServer", portName = "myServerPort", targetNamespace = "http://www.myServer.com")
public class Service {

	private List<User> userList;
	private List<Project> projectList;

	/**
	 * 构造函数，进行初始化
	 */
	public Service() {
		userList = new ArrayList<User>();
		projectList = new ArrayList<Project>();
	}


	public static void main(String[] args) {
		Endpoint.publish("http://127.0.0.1:8001/service", new Service());
		// wsimport -encoding utf-8 -keep http://127.0.0.1:8001/service?wsdl
		System.out.println("服务器启动成功 ！");
	}
	
	/**
	 * 添加用户
	 * @param user 用户
	 * @return 是否添加成功
	 */
	@WebMethod
	public boolean addUser(User user) {
		boolean isSuccess = true;
		
		for (User temp : userList) {// 判断用户名是否存在
			if (temp.getUsername().equals(user.getUsername())) {
				isSuccess = false;
				return isSuccess;
			}
		}
		isSuccess = userList.add(user);
		return isSuccess;
	}

	/**
	 * 查询用户
	 * @param username 用户名
	 * @return 查询到的用户
	 */
	@WebMethod
	public User queryUser(String username) {
		User user = null;
		
		for (User temp : userList) {// 查询用户
			if (temp.getUsername().equals(username)) {
				user = temp;
			}
		}
		return user;
	}

	/**
	 * 添加项目
	 * @param project 待添加项目
	 * @return 项目是否添加成功
	 */
	@WebMethod
	public int addProject(Project project) {
		int isSuccess = 1;
		
		for (Project temp : projectList) {// 判断时间是否冲突，时间冲突，返回2
			if (!(project.getEndTime().before(temp.getStartTime()) || project.getStartTime().after(temp.getEndTime()))) {
				isSuccess = 2;
				break;
			}
		}
		if (isSuccess == 1) {// 添加成功，返回1
			if(projectList.add(project) == true) {
				isSuccess = 1;
			}else {// 添加失败，返回0
				isSuccess = 0;
			}
		}
		return isSuccess;
	}

	/**
	 * 查询时间范围内的项目
	 * @param startTime 项目开始时间
	 * @param endTime 项目结束时间
	 * @return 查询到的在时间范围内的项目
	 */
	@WebMethod
	public List<Project> queryProject(Date startTime, Date endTime) {
		List<Project> queryList = new ArrayList<Project>();
		
		for (Project temp : projectList) {
			if ((temp.getStartTime().after(startTime) || temp.getStartTime().equals(startTime))
					&& (temp.getEndTime().before(endTime) || temp.getEndTime().equals(endTime))) {
				queryList.add(temp);
			}
		}
		if (queryList.size() != 0) {
			return queryList;
		} else {
			return null;
		}
	}

	/**
	 * 删除项目
	 * @param projectID 项目ID
	 * @param username 用户名
	 * @return 是否删除成功
	 */
	@WebMethod
	public boolean deleteProject(int projectID, String username) {
		boolean isExist = false;
		Project project = null;
		
		for (Project tmp : projectList) {
			if (tmp.getProjectID() == projectID && tmp.getCreator().equals(username)) {
				project = tmp;
				isExist = true;
				break;
			}
		}
		
		if (isExist) {// 若存在，进行删除
			isExist = projectList.remove(project);
		} 			
		return isExist;
	}

	/**
	 *  清除指定用户所有项目
	 * @param username 用户名
	 * @return 是否清除成功
	 */
	@WebMethod
	public boolean clearProject(String username) {
		List<Project> clearList = new ArrayList<Project>();

		for (Project temp : projectList) {
			if (temp.getCreator().equals(username) == false) {
				clearList.add(temp);
			}
		}
		projectList = clearList;
		return true;
	}

}
