package bean;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private String send_name;
	private String receiver_name;
	private Date date;
	private String message_text;
	
	
	public Message(String send_name, String receiver_name, Date date, String message_text) {
		super();
		this.send_name = send_name;
		this.receiver_name = receiver_name;
		this.date = date;
		this.message_text = message_text;
	}
	public String getSend_name() {
		return send_name;
	}
	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessageText() {
		return message_text;
	}
	public void setMessageText(String message_text) {
		this.message_text = message_text;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	@Override
	public String toString() {
		return "Message [Sender = " + send_name + ", Receiver = " + receiver_name
				+ ", Date = " + date + ",\n         Message Text = " + message_text + "]";
	}
	
	
	
}
