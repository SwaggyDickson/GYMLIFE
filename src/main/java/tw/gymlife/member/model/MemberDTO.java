package tw.gymlife.member.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MemberDTO {
	private int userId;
    private String userAccount;
    private String userPassword;
    private String userName;
    private String userNickName;
    private String userGender;
    private String userAddress;
    private String userTel;
    private String userEmail;
    private Date userBirthDay; 
    private Date userRegisterDate;
    private int userTotalSpend;
    private BigDecimal userRewardPoint;
    private String userPermission;
	
    
    
	
	@Override
	public String toString() {
		return "MemberDTO [userId=" + userId + ", userAccount=" + userAccount + ", userPassword=" + userPassword
				+ ", userName=" + userName + ", userNickName=" + userNickName + ", userGender=" + userGender
				+ ", userAddress=" + userAddress + ", userTel=" + userTel + ", userEmail=" + userEmail
				+ ", userBirthDay=" + userBirthDay + ", userRegisterDate=" + userRegisterDate + ", userTotalSpend="
				+ userTotalSpend + ", userRewardPoint=" + userRewardPoint + ", userPermission=" + userPermission + "]";
	}


    
    
}
