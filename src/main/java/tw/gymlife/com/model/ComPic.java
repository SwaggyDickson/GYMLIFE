package tw.gymlife.com.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "compic")
@Component("comPic")
public class ComPic {

	@Id @Column(name = "COMPICID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int comPicId;
	
	@Column(name = "COMPICNAME")
	private String comPicName;
	
	@Column(name = "COMID", insertable = false, updatable = false)
	private int comId;
	
	@Column(name = "COMPICFILE")
	private byte[] comPicFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMID")
	private Commoditys commoditys;

	public ComPic() {
		super();
	}
	
	
	public ComPic(int comPicId, String comPicName, int comId, byte[] comPicFile, Commoditys commoditys) {
		super();
		this.comPicId = comPicId;
		this.comPicName = comPicName;
		this.comId = comId;
		this.comPicFile= comPicFile;
		this.commoditys = commoditys;
		
	}
	

	


	@Override
	public String toString() {
		return "ComPic [comPicId=" + comPicId + ", comPicName=" + comPicName + ", comId=" + comId + "comPicFile"+comPicFile+"]";
		
	}


	public int getComPicId() {
		return comPicId;
	}

	public void setComPicId(int comPicId) {
		this.comPicId = comPicId;
	}

	public String getComPicName() {
		return comPicName;
	}

	public void setComPicName(String comPicName) {
		this.comPicName = comPicName;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public Commoditys getCommoditys() {
		return commoditys;
	}

	public void setCommoditys(Commoditys commoditys) {
		this.commoditys = commoditys;
	}
	
	public byte[] getComPicFile() {
		return comPicFile;
	}
	public void setComPicFile(byte[] comPicFile) {
		this.comPicFile = comPicFile;
	}
	
}
