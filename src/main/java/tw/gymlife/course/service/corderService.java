package tw.gymlife.course.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.AioCheckOutPeriod;
import ecpay.payment.integration.domain.CreateServerOrderObj;
import ecpay.payment.integration.domain.QueryCreditCardPeriodInfoObj;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import jakarta.transaction.Transactional;
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.course.model.CorderRepository;
import tw.gymlife.course.model.CourseBean;
import tw.gymlife.course.model.CourseRepostiory;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Service
public class corderService {
	@Autowired
	private CorderRepository oRepo;
	@Autowired
	private MemberRepository mRepo;
	@Autowired
	private courseService cservice;
//	 @Autowired
//	    private JavaMailSender javaMailSender;

	// 新增訂單
	public void insertCorder(CorderBean obean) {
		oRepo.save(obean);
	}
	//查詢訂單
	public Member selectMemberByuserId(Integer userId) {
		Optional<Member> optional = mRepo.findById(userId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	//查詢全部訂單
	public List<CorderBean> selectAllCorder(){
		return oRepo.findAll();
	}

	// 查詢訂單bycorderID
	public CorderBean selectCorderBycorderId(Integer corderId) {
		Optional<CorderBean> optional = oRepo.findById(corderId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	// 綠界金流+新增訂單
	public String ecpayCheckout(Integer userId, Integer courseId, String corderPayment, Integer corderQuantity,
			Integer corderCost, String courseName) {

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

		AllInOne all = new AllInOne("");
		// 建立 SimpleDateFormat 物件，指定日期時間的格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// 取得現在的日期時間
		String currentDate = sdf.format(new Date());
//		System.out.println(userId);
//		System.out.println(courseId);
//		System.out.println(corderPayment);
//		System.out.println(corderQuantity);
//		System.out.println(corderCost);
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate(currentDate);
		obj.setTotalAmount(corderCost.toString());
		obj.setTradeDesc("test Description");
		obj.setItemName(courseName + " * " + corderQuantity + "堂");
		// 結束後跳轉
		obj.setReturnURL("http://localhost:8080/gymlife/front/coursesingle");
//		obj.setClientRedirectURL("http://localhost:8080/gymlife/front/course");
//		System.out.println(uuId);
//		obj.setClientBackURL("http://localhost:8080/gymlife/front/coursesingle/corderenter?MerchantTradeNo="+uuId);
		obj.setClientBackURL("http://localhost:8080/gymlife/front/coursecoder/complete?MerchantTradeNo="+uuId+"&userId="+userId+"&courseId="+courseId+"&corderPayment="+corderPayment+"&corderQuantity="+corderQuantity+"&corderCost="+corderCost);
//		obj.setOrderResultURL("http://localhost:8080/gymlife/course/order/insert?userId="+userId+"&courseId="+courseId+"&corderPayment="+corderPayment+"&corderQuantity="+corderQuantity+"&corderCost="+corderCost);
		obj.setNeedExtraPaidInfo("N");
		String form = all.aioCheckOut(obj, null);
		
		
		return form;
	}
	
	//更新訂單狀態
	@Transactional
	public void updateCorderState(Integer corderId,Integer corderState) {
		Optional<CorderBean> optional = oRepo.findById(corderId);
		if(optional.isPresent()) {
			CorderBean obean = optional.get();
			obean.setCorderState(corderState);
		}
	}
	//更新訂單數量
	@Transactional
	public void updateCorder(Integer corderId,String corderUpdateTime,Integer corderQuantity,Integer corderCost) {
		Optional<CorderBean> optional = oRepo.findById(corderId);
		if(optional.isPresent()) {
			CorderBean obean = optional.get();
			obean.setCorderUpdateTime(corderUpdateTime);
			obean.setCorderQuantity(corderQuantity);
			obean.setCorderCost(corderCost);
			obean.setCorderState(0);
		}
	}
	//刪除訂單
	public void deleteCorder(Integer corderId) {
		oRepo.deleteById(corderId);
	}

}
