package tw.gymlife.course.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.course.model.CourseBean;
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.course.model.CourseDTO;
import tw.gymlife.course.model.ImageBean;
import tw.gymlife.course.model.convertDTO;
import tw.gymlife.course.service.CourseOrderService;
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
	private convertDTO converDTO;

	@Autowired
	CourseOrderService orderService;

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
			@RequestParam("corderCost") Integer corderCost,@RequestParam("courseName") String courseName) {
		String aioCheckOutALLForm = oservice.ecpayCheckout(userId,courseId,corderPayment,corderQuantity,corderCost,courseName);

		return aioCheckOutALLForm;
	}

	// 教練課程(查詢所有課程)
	@GetMapping("/front/coursesingle")
	public String findAllsingle(Model m) throws ParseException {
		List<CourseBean> cbeans = cservice.selectAllCourse();
		List<ImageBean> ibeans = iservice.selectAllCourseImage();

		m.addAttribute("cbeans", cbeans);
		m.addAttribute("ibeans", ibeans);
		return "frontgymlife/course/coursesingle";
	}
	// 查詢單筆會員 ajax
	@ResponseBody
	@GetMapping("/course/cordermember")
	public Member findmemberById(@RequestParam(name="userId") Integer userId) throws ParseException {
		Member member = oservice.selectMemberByuserId(userId);
		return member;
	}

	// 查詢課程ByIdAjax
	@ResponseBody
	@GetMapping("/front/coursesingle/select")
	public CourseDTO cousesingle(@RequestParam(name = "courseId") Integer courseId) {
		CourseBean cbean = cservice.selectCourseById(courseId);
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
			model.addAttribute("userId", userId);
			model.addAttribute("userName", userName);
			model.addAttribute("cbean", cbean);
			return "frontgymlife/course/coursebuy";
		} else {
			return "redirect:/Login";
		}
	}

	// 新增訂單
	/*@PostMapping("/course/order/insert")
//	@GetMapping("/course/order/insert")
	public String insertCorder(@RequestParam("userId") Integer userId, @RequestParam("courseId") Integer courseId,
			@RequestParam("corderPayment") String corderPayment, @RequestParam("corderQuantity") Integer corderQuantity,
			@RequestParam("corderCost") Integer corderCost, Model model) throws ParseException {

		Member member = oservice.selectMemberByuserId(userId);
		CourseBean cbean = cservice.selectCourseById(courseId);
		CorderBean obean = new CorderBean();
		obean.setMember(member);
		obean.setCourse(cbean);
		obean.setCorderPayment(corderPayment);
		obean.setCorderQuantity(corderQuantity);
		obean.setCorderCost(corderCost);
		oservice.insertCorder(obean);
		return "redirect:/front/coursesingle";
	}*/

}
