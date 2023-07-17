package tw.gymlife.course.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.course.model.CourseBean;
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.course.model.CourseDTO;
import tw.gymlife.course.model.ImageBean;
import tw.gymlife.course.model.convertDTO;
import tw.gymlife.course.service.coachService;
import tw.gymlife.course.service.corderService;
import tw.gymlife.course.service.courseService;
import tw.gymlife.course.service.imageService;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Controller
public class CourseController_Front {

	@Autowired
	private courseService cservice;

	@Autowired
	private imageService iservice;
	@Autowired
	private coachService chservice;
	@Autowired
	private corderService oservice;

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private convertDTO converDTO;
	

//	//前台首頁
//	@GetMapping("/front")
//	public String home() {
//		return"frontgymlife/course/FrontGYMLIFE";
//	}
	// 課程首頁
	@GetMapping("/front/course")
	public String courseHome() {
		AioCheckOutALL obj = new AioCheckOutALL();
		String ccc = obj.getReturnURL();
		System.out.println(ccc);
		return "frontgymlife/course/Course";
	}

	// 購買課程
	@GetMapping("/front/coursesingle/coursebuy")
	public String coursesinglebuy() {
		return "frontgymlife/course/coursebuy";
	}

	// 綠界付款+新增訂單
	@ResponseBody
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout(@RequestParam("userId") Integer userId, @RequestParam("courseId") Integer courseId,
			@RequestParam("corderPayment") String corderPayment, @RequestParam("corderQuantity") Integer corderQuantity,
			@RequestParam("corderCost") Integer corderCost, @RequestParam("courseName") String courseName) {
		String aioCheckOutALLForm = oservice.ecpayCheckout(userId, courseId, corderPayment, corderQuantity, corderCost,
				courseName);

		return aioCheckOutALLForm;
	}
	

	// 查詢訂單結果
	@GetMapping("/front/coursecoder/complete")
	public String findCorderEnter(@RequestParam("MerchantTradeNo") String MerchantTradeNo ,HttpSession session,@RequestParam("userId") Integer userId, @RequestParam("courseId") Integer courseId,
			@RequestParam("corderPayment") String corderPayment, @RequestParam("corderQuantity") Integer corderQuantity,
			@RequestParam("corderCost") Integer corderCost) throws MessagingException {
		AllInOne all = new AllInOne("");
		QueryTradeInfoObj obj = new QueryTradeInfoObj();
		obj.setMerchantTradeNo(MerchantTradeNo);
		String data = all.queryTradeInfo(obj);
		
		System.out.println(data);
		//擷取字串
		String tradeStatusValue = "";
		String[] params = data.split("&");
		for (String param : params) {
		    String[] keyValue = param.split("=");
		    //抓取TradeStatus
		    if (keyValue.length == 2 && keyValue[0].equals("TradeStatus")) {
		        tradeStatusValue = keyValue[1];
		        break;
		    }
		}
			//TradeStatus=0
		if (tradeStatusValue.equals("0")) {
		    System.out.println("購買失敗!");
		} else if (tradeStatusValue.equals("1")) {
			//TradeStatus=1
			System.out.println("購買成功!");
			// 新增到訂單
			Member member = oservice.selectMemberByuserId(userId);
			CourseBean cbean = cservice.selectCourseById(courseId);
			CorderBean obean = new CorderBean();
			obean.setMember(member);
			obean.setCourse(cbean);
			obean.setCorderPayment(corderPayment);
			obean.setCorderQuantity(corderQuantity);
			obean.setCorderCost(corderCost);
			obean.setCorderState(0);
			oservice.insertCorder(obean);
			cservice.insertCourseBuyers(courseId);
			//成功寄送e-mail
			String userEmail = session.getAttribute("userEmail").toString();
		    String userName = session.getAttribute("userName").toString();
		    String userTel = session.getAttribute("userTel").toString();
		    String userAddress = session.getAttribute("userAddress").toString();
		    MimeMessage message = javaMailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    helper.setTo(userEmail);
		    helper.setSubject("GYMLIFE");
		    helper.setText("<html>\r\n"
		    		+ "<head>\r\n"
		    		+ "  <meta charset=\"UTF-8\">\r\n"
		    		+ "  <title>健身課程訂單確認</title>\r\n"
		    		+ "</head>\r\n"
		    		+ "<body>\r\n"
		    		+ "<img src=\"https://gymlife.es/wp-content/uploads/2022/01/GYMLIFELOGO-BLACK-WEB.png\" style=\"width: 200px;\">\r\n"
		    		+ "  <h2>健身課程訂單確認</h2>\r\n"
		    		+ "\r\n"
		    		+ "  <p>親愛的 "+userName+"，</p>\r\n"
		    		+ "  <p>感謝您選擇我們的健身課程！我們非常期待能與您一同開展這個健康旅程。以下是您的訂單詳細資訊：</p>\r\n"
		    		+ "\r\n"
		    		+"<table border=\"1\">\r\n"
		    		+ "    <thead>\r\n"
		    		+ "      <tr>\r\n"
		    		+ "        <th>課程名稱</th>\r\n"
		    		+ "        <th>數量</th>\r\n"
		    		+ "        <th>價格</th>\r\n"
		    		+ "      </tr>\r\n"
		    		+ "    </thead>\r\n"
		    		+ "    <tbody>\r\n"
		    		+ "      <tr>\r\n"
		    		+ "        <td>"+cbean.getCourseName()+"</td>\r\n"
		    		+ "        <td>"+corderQuantity+"</td>\r\n"
		    		+ "        <td>"+cbean.getCourseCost()+"</td>\r\n"
		    		+ "      </tr>\r\n"
		    		+ "    </tbody>\r\n"
		    		+ "    <tfoot>\r\n"
		    		+ "      <tr>\r\n"
		    		+ "        <td colspan=\"2\">總金額：</td>\r\n"
		    		+ "        <td>"+corderCost+"</td>\r\n"
		    		+ "      </tr>\r\n"
		    		+ "    </tfoot>\r\n"
		    		+ "  </table>\r\n"
		    		+ "\r\n"
		    		+ "  <p>付款方式： 信用卡付款</p>\r\n"
		    		+ "  <p>收件人姓名： "+userName+"</p>\r\n"
		    		+ "  <p>聯絡電話： "+userTel+"</p>\r\n"
		    		+ "  <p>郵寄地址： "+userAddress+"</p>\r\n"
		    		+ "\r\n"
//		    		+ "  <p>付款資訊：</p>\r\n"
//		    		+ "  <p>[付款資訊詳情]</p>\r\n"
		    		+ "\r\n"
		    		+ "  <p>如有任何問題或需要進一步的協助，請隨時與我們聯繫。我們的客戶服務團隊將竭誠為您服務。</p>\r\n"
		    		+ "\r\n"
		    		+ "  <p>再次感謝您的選擇和支持！</p>\r\n"
		    		+ "\r\n"
		    		+ "  <p>祝您健康愉快！</p>\r\n"
		    		+ "\r\n"
		    		+ "  <p>GYMLIFE</p>\r\n"
		    		+ "</body>\r\n"
		    		+ "</html>", true);

		    javaMailSender.send(message);
		} 	
		return "redirect:/front/coursesingle";
	}

	// 教練課程(查詢所有課程)
	@GetMapping("/front/coursesingle")
	public String findAllsingle( Model m) throws ParseException {
		List<CourseBean> cbeans = cservice.selectAllCourse();
		List<ImageBean> ibeans = iservice.selectAllCourseImage();

		m.addAttribute("cbeans", cbeans);
		m.addAttribute("ibeans", ibeans);
		return "frontgymlife/course/Course";

	}

	// 查詢單筆課程
	@GetMapping("/front/coursesingle/{courseId}")
	public String findCourseById(@PathVariable Integer courseId, Model m) {
		CourseBean cbean = cservice.selectCourseById(courseId);
		cservice.insertCourseViewers(courseId);
		List<CourseBean> cbeans = cservice.selectAllCourse();
		CourseDTO cdto = converDTO.convertCourseDTO(cbean);
		System.out.println(cdto);
		Collections.sort(cbeans, new CourseBuyersComparator());
		List<CourseBean> topThreeCourses = cbeans.subList(0, Math.min(cbeans.size(), 3));
		for (CourseBean ccbean : topThreeCourses) {
			int ccourseId = ccbean.getCourseId();
			System.out.println(ccourseId);
			String courseName = ccbean.getCourseName();
			System.out.println(courseName);
		}
		m.addAttribute("topThreeBuyers", topThreeCourses);
		m.addAttribute("cbeans", cbeans);
		m.addAttribute("cbean", cdto);
		return "frontgymlife/course/coursesingle";
	}

	// 依照購買課程人數排序
	public class CourseBuyersComparator implements Comparator<CourseBean> {
		@Override
		public int compare(CourseBean bean1, CourseBean bean2) {
			return Integer.compare(bean2.getCourseBuyers(), bean1.getCourseBuyers());
		}
	}

	// 查詢單筆會員 ajax
	@ResponseBody
	@GetMapping("/course/cordermember")
	public Member findmemberById(@RequestParam(name = "userId") Integer userId) throws ParseException {
		Member member = oservice.selectMemberByuserId(userId);
		return member;
	}

	// 查詢課程ByIdAjax
	@ResponseBody
	@GetMapping("/front/coursesingle/select")
	public CourseDTO cousesingle(@RequestParam(name = "courseId") Integer courseId) {
		CourseBean cbean = cservice.selectCourseById(courseId);
		cservice.insertCourseViewers(courseId);
		CourseDTO cdto = converDTO.convertCourseDTO(cbean);
		System.out.println(cdto);
		return cdto;
	}

	// 查詢課程ById(跳轉到購買課程)
	@GetMapping("/course/buy")
	public String buyCourseById(@RequestParam("courseId") Integer courseId, HttpSession session, Model model) {
		if (session.getAttribute("userId") != null) {
			int userId = Integer.parseInt(session.getAttribute("userId").toString());
			CourseBean cbean = cservice.selectCourseById(courseId);
			String userName = session.getAttribute("userName").toString();
			System.out.println("userId:" + userId);
			Member member = oservice.selectMemberByuserId(userId);
			model.addAttribute("userId", userId);
			model.addAttribute("userName", userName);
			model.addAttribute("cbean", cbean);
			model.addAttribute("member", member);
			return "frontgymlife/course/coursebuy";
		} else {
			return "redirect:/Login";
		}
	}

	// 會員訂單查詢
	@GetMapping("/course/membercorder")
	public String selectMemberCorder(HttpSession session,Model model) {
		if (session.getAttribute("userId") != null) {
			int userId = Integer.parseInt(session.getAttribute("userId").toString());
			Member member = oservice.selectMemberByuserId(userId);
			model.addAttribute("member", member);
			return "frontgymlife/course/membercorder";
		} else {
			return "redirect:/Login";
		}
	}
	// 會員訂單查詢Ajax
	@ResponseBody
	@GetMapping("/course/membercorderajax")
	public CourseDTO selectMemberCorderAjax(HttpSession session) {
			int userId = Integer.parseInt(session.getAttribute("userId").toString());
			Member member = oservice.selectMemberByuserId(userId);
			return converDTO.convertMemberDTO(member);
	}
	//修改訂單狀態
			@ResponseBody
			@PutMapping("/course/corder/state")
			public CourseDTO updateCorder(@RequestParam(name="corderId")Integer corderId,@RequestParam(name="corderState")Integer corderState,HttpSession session) {
				oservice.updateCorderState(corderId, corderState);
				return selectMemberCorderAjax(session);
			}

}
