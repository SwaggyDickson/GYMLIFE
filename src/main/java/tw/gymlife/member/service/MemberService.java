package tw.gymlife.member.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;


@Service
public class MemberService {
	
	@Autowired
	private  PasswordEncoder pwdEncoder;
	
	@Autowired
	private MemberRepository memberRepo;
	 


	public Member register(Member member) {
		member.setUserPassword(pwdEncoder.encode(member.getUserPassword()));
		return memberRepo.save(member);
	}
	
	public boolean checkIfUserAccountExist(String userAccount) {
		Member member = memberRepo.findByUserAccount(userAccount);
		
		if (member !=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public Member checkLogin(String userAccount, String userPassword) {
		Member member = memberRepo.findByUserAccount(userAccount);
		if (member!=null) {
			if (pwdEncoder.matches(userPassword, member.getUserPassword())) {
				return member;
			}
		}else {
			return null;
		}
		return null;
	}
	
	public List<Member> selectAllMembers(){
		return memberRepo.findAll();
	}
	 
	public void deleteMember(int userId) {
		memberRepo.deleteById(userId);
    }
	
	  public Member memberUpdate(int userId, String userAccount, String userPassword, String userName, String userGender,
	            String userAddress, String userTel, String userEmail, Date userBirthDay){
	        Member member = memberRepo.findById(userId).orElse(null);
	        if(member != null){
	            member.setUserAccount(userAccount);
	            member.setUserPassword(userPassword);
	            member.setUserName(userName);
	            member.setUserGender(userGender);
	            member.setUserAddress(userAddress);
	            member.setUserTel(userTel);
	            member.setUserEmail(userEmail);
	            member.setUserBirthDay(userBirthDay);
	            member = memberRepo.save(member);
	        }
	        return member;
	    }
	  
	  public boolean checkIfUserAccountExistAjax(String userAccount) {
	        return memberRepo.existsByUserAccount(userAccount);
	    }


	
	public Member updateUserDetails(int userId, String userAccount, String userName, String userGender, 
			String userAddress, String userTel, String userEmail, String userNickName) {
		Optional<Member> memberOptional = memberRepo.findById(userId);

        if (!memberOptional.isPresent()) {
            // Handle the case where the Member with the given userId does not exist
        }

        Member member = memberOptional.get();
        member.setUserAccount(userAccount);
        member.setUserName(userName);
        member.setUserGender(userGender);
        member.setUserAddress(userAddress);
//        member.setUserBirthDay(userBirthDay);
        member.setUserTel(userTel);
        member.setUserEmail(userEmail);
        member.setUserNickName(userNickName);
        
        return memberRepo.save(member);
    }
	
	// 信箱驗證
	 public void verifyUser(int userId) {
	        // 根據userId從數據庫查找用戶
	        Member member = memberRepo.findById(userId).orElse(null);
	        
	        if (member != null) {
	            
	            member.setUserStatus(1);;
	            
	            memberRepo.save(member);
	        } else {
	            // 處理找不到用戶的情況，例如拋出異常或者返回錯誤信息
	            throw new RuntimeException("User not found");
	        }
	    }
	 
//	 public Member findUserByUserAccountAndUserEmail(String userAccount,String userEmail) {
//		 memberRepo.findUserByUserAccountAndUserEmail();
//	 }
//	
}
