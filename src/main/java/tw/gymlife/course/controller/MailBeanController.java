package tw.gymlife.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.gymlife.course.model.MailBean;
import tw.gymlife.course.service.mailBeanService;

@Controller
public class MailBeanController {
	@Autowired
	private mailBeanService mservice;
	//查詢所有訊息
	@ResponseBody
	@GetMapping("/mail/select")
	public List<MailBean> selectAllMail(){
		return mservice.selectAllMail();
	}
	//新增未讀訊息
	@ResponseBody
	@PostMapping("/mail/insert")
	public void insertMail(@RequestParam("mailType")String mailType,@RequestParam("mail")String mail) {
		MailBean mailbean = new MailBean();
		mailbean.setMailType(mailType);
		mailbean.setMail(mail);
		mservice.insertMail(mailbean);
	}
	//已讀訊息
	@ResponseBody
	@PutMapping("/mail/read")
	public void updateMailNotRead(@RequestParam("mailId")Integer mailId) {
		System.out.println(mailId);
		mservice.updateMailNotRead(mailId);
	}
}
