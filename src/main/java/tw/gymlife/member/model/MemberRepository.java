package tw.gymlife.member.model;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	public Member findByUserAccount(String userAccount);
	
	void deleteById(int userId);
	
	boolean existsByUserAccount(String userAccount);
	
	public Optional<Member> findByUserAccountAndUserEmail(String userAccount, String userEmail);
	
//	public Member updateUserDetails(int userId, String userAccount, String userName, String userGender, 
//				String userAddress, Date userBirthDay, String userTel, String userEmail, String userNickName);
	

}
