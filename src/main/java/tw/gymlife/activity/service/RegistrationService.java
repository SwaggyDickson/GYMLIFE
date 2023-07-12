package tw.gymlife.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.activity.dao.RegistrationRepository;
import tw.gymlife.activity.model.Registration;

@Service
public class RegistrationService {

    @Autowired
    public RegistrationRepository rRepo;
    
    // 活動報名

    // 新增
    public Registration insertRegistration(Registration registration) {
        Date currentDate = new Date();
        registration.setRegistrationDate(currentDate);
        registration.setCreateTime(currentDate);
        return rRepo.save(registration);
    }

    // 查詢根據報名ID
    public Registration getRegistrationById(int registrationId) {
        Optional<Registration> optional = rRepo.findById(registrationId);
        if(optional.isPresent()) {
        	return optional.get();
        }
        return null;
    }

    // 查询根據活動ID
    public List<Registration> getRegistrationsByActivityId(int activityId) {
        return rRepo.findByActivityId(activityId);
    }

    // 查询根據會員ID
    public List<Registration> getRegistrationsByMemberId(int userId) {
        return rRepo.findByuserId(userId);
    }

    // 更新
    @Transactional
    public Registration updateRegistrationById(Integer registrationId,String registrationStatus,Date registrationDate,String emergencyContact,Integer emergencyContactPhone, String relationship) {
    	Optional<Registration> optional = rRepo.findById(registrationId);
    	if(optional.isPresent()) {
    		Registration registration = optional.get();
    		registration.setRegistrationStatus(registrationStatus);
    		registration.setRegistrationDate(registrationDate);
    		registration.setEmergencyContact(emergencyContact);
    		registration.setEmergencyContactPhone(emergencyContactPhone);
    		registration.setRelationship(relationship);
    		
	        Date currentDate = new Date();
	        registration.setUpdateTime(currentDate);
	        return rRepo.save(registration);
    	} else {
	        System.out.println("No update data");
	        return null;
	    }
    }

    // 刪除
    public void deleteRegistration(Integer registrationId) {
        rRepo.deleteById(registrationId);
    }
    
    // 判斷是不是該報名過該活動
    public boolean isMemberRegisteredForActivity(int userId, int activityId) {
        List<Registration> registrations = rRepo.findByuserIdAndActivityId(userId, activityId);
        return !registrations.isEmpty();
    }
    

}
