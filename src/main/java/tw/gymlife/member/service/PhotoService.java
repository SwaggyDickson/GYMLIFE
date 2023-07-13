package tw.gymlife.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Path;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Service
public class PhotoService {

	 @Autowired
	    private MemberRepository memberRepo;

	    public void savePhoto(int userId, byte[] photoData) {
	        try {
	            // 找到對應的用戶
	            Member member = memberRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

	            // 將文件轉換為字節數組並存儲到用戶實體中
	            member.setUserPhoto(photoData);

	            // 保存用戶
	            memberRepo.save(member);
	        } catch (Exception e) {
	            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	        }
	    }
	}

