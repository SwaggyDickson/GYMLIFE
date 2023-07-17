package tw.gymlife.member.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	public Member findByUserAccount(String userAccount);
	
	void deleteById(int userId);
	
	boolean existsByUserAccount(String userAccount);
	
	public Optional<Member> findByUserAccountAndUserEmail(String userAccount, String userEmail);
	
	public Optional<Member> findUserByUserAccount(String userAccount);
		
	@Query(value = "SELECT new map(MONTH(m.userRegisterDate) as month, COUNT(m.userId) as count) FROM Member m WHERE YEAR(m.userRegisterDate) = :year GROUP BY MONTH(m.userRegisterDate)")
    List<Map<String, Object>> countMembersByMonth(@Param("year") int year);

	Member findByUserEmail(String userEmail);

  


	


	
	
	
//	public Member updateUserDetails(int userId, String userAccount, String userName, String userGender, 
//				String userAddress, Date userBirthDay, String userTel, String userEmail, String userNickName);
}
