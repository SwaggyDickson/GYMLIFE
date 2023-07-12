package tw.gymlife.course.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tw.gymlife.course.model.MailBean;
import tw.gymlife.course.model.MailRepository;

@Service
public class mailBeanService {
	@Autowired
	private MailRepository mailRepo;
	//查詢所有mail
	public List<MailBean> selectAllMail(){
		return mailRepo.findAll(Sort.by(Sort.Direction.DESC,"mailId"));
	}
	//新增mail
	public void insertMail(MailBean mailbean) {
		mailRepo.save(mailbean);
	}
	//已讀訊息
	@Transactional
	public void updateMailNotRead(Integer mailId) {
		Optional<MailBean> optional = mailRepo.findById(mailId);
		System.out.println("666:"+mailId);
		if(optional.isPresent()) {
			MailBean mailbean = optional.get();
			mailbean.setMailNotRead(0);
		}
	}

}
