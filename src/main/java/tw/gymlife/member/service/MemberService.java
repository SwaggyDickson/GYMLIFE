package tw.gymlife.member.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	  public Member memberUpdate(int userId, String userAccount, String userName, String userGender,
	            String userAddress, String userTel, String userEmail, Date userBirthDay){
	        Member member = memberRepo.findById(userId).orElse(null);
	        if(member != null){
	            member.setUserAccount(userAccount);
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
			String userAddress,Date userBirthDay, String userTel, String userEmail, String userNickName, byte[] userPhoto ) {
		Optional<Member> memberOptional = memberRepo.findById(userId);

        if (!memberOptional.isPresent()) {
            // Handle the case where the Member with the given userId does not exist
        }

        Member member = memberOptional.get();
        member.setUserAccount(userAccount);
        member.setUserName(userName);
        member.setUserGender(userGender);
        member.setUserAddress(userAddress);
        member.setUserBirthDay(userBirthDay);
        member.setUserTel(userTel);
        member.setUserEmail(userEmail);
        member.setUserNickName(userNickName);
        member.setUserPhoto(userPhoto);
        
        
        return memberRepo.save(member);
    }
	
	
	public Member updateUserStatus(int userId, int userStatus) {
		Optional<Member> memberOptional = memberRepo.findById(userId);
		  if (!memberOptional.isPresent()) {
			  return null;
	        }
	
		  Member member = memberOptional.get();
		    if (member.getUserStatus() == 1) {
		        member.setUserStatus(0);
		    } else {
		        member.setUserStatus(1);
		    }
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
	 //--------忘記密碼-----
	 //透過帳號信箱比對資料庫
	 public Optional<Member> findUserByUserAccountAndUserEmail(String userAccount,String userEmail) {
			return memberRepo.findByUserAccountAndUserEmail(userAccount, userEmail);
	 }

	 //使用帳號搜尋該筆資料
	 public Optional<Member> findUserByUserAccount(String userAccount) {
		
		return memberRepo.findUserByUserAccount(userAccount);
	}
	
	 public Member seveUserPassword(Member member) {
		 return memberRepo.save(member);
	 }
	 
	 
	  public Optional<Member> findById(int userId) {
	        return memberRepo.findById(userId);
	    }
	 
	  public Map<String, Integer> getGenderCount(){
		  List<Member> members = memberRepo.findAll();
		  
		  
		  int male = 0;
		  int female =0;
		  
		  for(Member member: members) {
			  String gender = member.getUserGender().trim();
			  
			  System.out.println("Member gender: " + member.getUserGender());
			  if ("male".equalsIgnoreCase(gender)) {
				  male++;
				  System.out.println("Incrementing male count");
			  } else if ("female".equalsIgnoreCase(gender)) {
				  female++;
				  System.out.println("Incrementing female count");
			  }
			  System.out.println("Original gender value: '" + member.getUserGender() + "'");
		  }
		  
		  
		  Map<String, Integer> genderCount = new HashMap<>();
		  genderCount.put("女", female);
		  genderCount.put("男", male);
		  System.out.println("Male: " + male);  // 打印男性数量
		  System.out.println("Female: " + female);  // 打印女性数量
		  
		  
		  return genderCount;
	  }
	  
	  public long countMember() {
		  return memberRepo.count();
	  }
	  
	  public List<Map<String, Object>> countMembersByMonth() {
	        // Get the current year
	        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	        return memberRepo.countMembersByMonth(currentYear);
	    }
	  
	  
		// 查單筆(id)
		public Member getMemberById(Integer userId) {
			Optional<Member> memberOptional = memberRepo.findById(userId);

			if (memberOptional.isPresent()) {
				return memberOptional.get();
			}
			return null;
		}

		 public Member checkUserByEmail(String userEmail) {
				
				return memberRepo.findByUserEmail(userEmail);
			}
	  
}
