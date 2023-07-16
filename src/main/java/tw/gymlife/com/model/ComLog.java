package tw.gymlife.com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comlog")
public class ComLog {

	@Id @Column(name = "LOGID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int logId;
	
	@Column(name = "USERIP")
	private String userIp;
	
	@Column(name = "CONNECTTIME")
	private String connectTime;
	
	@Column(name = "USERACTION")
	private String userAction;

	public ComLog() {
	}

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(String connectTime) {
		this.connectTime = connectTime;
	}

	public String getUserAction() {
		return userAction;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}
	
	
	
}
