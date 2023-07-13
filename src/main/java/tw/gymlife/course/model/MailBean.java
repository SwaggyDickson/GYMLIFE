package tw.gymlife.course.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="mail")
public class MailBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mailId")
	private Integer mailId;
	@Column(name = "mailType")
	private String mailType;
	@Column(name = "mailNotRead")
	private Integer mailNotRead = 1;
	@Column(name = "mail")
	private String mail;
}
