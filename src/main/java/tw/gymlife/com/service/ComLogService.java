package tw.gymlife.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.com.dao.ComLogRepository;
import tw.gymlife.com.model.ComLog;

@Service
public class ComLogService {

	
	@Autowired ComLogRepository logRepo;
	
	//新增log
	public ComLog newLog(ComLog comLog) {
		
		return logRepo.save(comLog);
	}
	
	//查詢全部log
	public List<ComLog> getAllLog(){
		
		return logRepo.findAll();
	}
}
