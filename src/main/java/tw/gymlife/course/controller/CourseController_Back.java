package tw.gymlife.course.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.annotation.MultipartConfig;
import tw.gymlife.course.model.CoachBean;
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.course.model.CourseBean;
import tw.gymlife.course.model.CourseDTO;
import tw.gymlife.course.model.ImageBean;
import tw.gymlife.course.model.convertDTO;
import tw.gymlife.course.service.coachService;
import tw.gymlife.course.service.corderService;
import tw.gymlife.course.service.courseService;
import tw.gymlife.course.service.imageService;

@Controller
@MultipartConfig
public class CourseController_Back {

	@Autowired
	private courseService cservice;

	@Autowired
	private imageService iservice;
	@Autowired
	private coachService chservice;
	@Autowired
	private corderService oservice;
	@Autowired
	private convertDTO convertDTO;
	@Autowired
	private JavaMailSender javaMailSender;
	//新增教練
	@ResponseBody
	@PostMapping("/course/coach/insert")
	public List<CoachBean> insertCourseCoach(@RequestParam("coachName") String coachName,
			@RequestParam("coachNickName") String coachNickName,
			@RequestParam("coachPhoneNumber") String coachPhoneNumber,
			@RequestParam("coachEmail") String coachEmail,
			@RequestParam("coachBirthday") String coachBirthday,
			@RequestParam("coachHeight") Integer coachHeight,
			@RequestParam("coachWeight") Integer coachWeight,
			@RequestParam("coachIntroduce") String coachIntroduce,
			@RequestParam("coachImg") MultipartFile image,Model m)
			throws IOException {
		
		CoachBean chbean = new CoachBean();
		chbean.setCoachName(coachName);
		chbean.setCoachNickName(coachNickName);
		chbean.setCoachPhoneNumber(coachPhoneNumber);
		chbean.setCoachEmail(coachEmail);
		chbean.setCoachBirthday(coachBirthday);
		chbean.setCoachHeight(coachHeight);
		chbean.setCoachWeight(coachWeight);
		chbean.setCoachIntroduce(coachIntroduce);
		chbean.setCoachImage(image.getBytes());
		chservice.insertCourseCoach(chbean);
//		return selectAllCourse(m);
//		return "redirect:/course/coach";
		return selectAllCourseajax();
	}
	//查詢所有教練ajax
	@ResponseBody
	@GetMapping("/course/coachaj")
	public List<CoachBean> selectAllCourseajax() {
		List<CoachBean> chbeans = chservice.selectAllCoach();
		return chbeans;
	}
	//查詢所有教練
	@GetMapping("/course/coach")
	public String selectAllCourse(Model m) {
		List<CoachBean> chbeans = chservice.selectAllCoach();
		m.addAttribute("chbeans", chbeans);
		return "/backgymlife/course/selectAllCoach";
	}
	//查詢教練ById
	@ResponseBody
	@GetMapping("/course/coachbyid")
	public CoachBean selectCoachById(@RequestParam("coachId") Integer coachId) {
		CoachBean chbean = chservice.selectCoachById(coachId);
		return chbean;
	}
	//修改教練
	@ResponseBody
	@PutMapping("/course/coach/update")
	public List<CoachBean> updateCourse(@RequestParam("coachId") Integer coachId,@RequestParam("coachName") String coachName,
			@RequestParam("coachNickName") String coachNickName,
			@RequestParam("coachPhoneNumber") String coachPhoneNumber,
			@RequestParam("coachEmail") String coachEmail,
			@RequestParam("coachBirthday") String coachBirthday,
			@RequestParam("coachHeight") Integer coachHeight,
			@RequestParam("coachWeight") Integer coachWeight,
			@RequestParam("coachIntroduce") String coachIntroduce, Model m) {
		chservice.updateCourseCoach(coachId, coachName,coachNickName, coachPhoneNumber,coachEmail,coachBirthday, coachWeight, coachHeight, coachIntroduce);
		return selectAllCourseajax();
	}
	//刪除教練
	@ResponseBody
	@DeleteMapping("/course/coach/delete")
	public List<CoachBean> deleteCourse(@RequestParam("coachId") Integer coachId, Model m) {
		chservice.deleteCoach(coachId);
		return selectAllCourseajax();
	}
	//查詢教練圖片
		@GetMapping("/coachImage/{coachId}")
		public ResponseEntity<byte[]> coachImage(@PathVariable Integer coachId){
			CoachBean chbean = chservice.selectCoachById(coachId);
			byte[] photoFile = chbean.getCoachImage();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
											//檔案, header, HttpStatus
			return new ResponseEntity<byte[]>(photoFile,headers,HttpStatus.OK);
		}
		// 修改教練圖片
		@ResponseBody
		@PutMapping("/course/updatecoachImg")
		public List<CoachBean> updateCourseImg(@RequestParam("coachId") Integer coachId, @RequestParam("coachImg") MultipartFile image)
				throws IOException {
			chservice.updateCoachImg(coachId, image.getBytes());
			return selectAllCourseajax();
		}
	// 新增課程
		@ResponseBody
	@PostMapping("/course/insert")
	public List<CourseDTO> insertCourse(@RequestParam("coachId") Integer coachId,
			@RequestParam("courseName") String courseName,
			@RequestParam("courseCost") int courseCost,
			@RequestParam("courseIntroduce") String courseIntroduce, MultipartHttpServletRequest request, Model m)
			throws IOException {
		CourseBean cbean = new CourseBean();
		cbean.setCourseName(courseName);
		cbean.setCourseCost(courseCost);
		cbean.setCourseIntroduce(courseIntroduce);
		CoachBean chbean = chservice.selectCoachById(coachId);

		List<MultipartFile> images = request.getFiles("courseImg");
		List<ImageBean> imageBeans = new ArrayList<>();
		for (MultipartFile image : images) {
			ImageBean imageBean = new ImageBean();
			imageBean.setImageName(image.getOriginalFilename());
			imageBean.setImageData(image.getBytes());
			imageBean.setImgmimeType(image.getContentType());
			imageBean.setCourse(cbean);
			imageBeans.add(imageBean);
		}
		cbean.setCoach(chbean);
		cbean.setImages(imageBeans);
//		System.out.println("insert前:"+cbean);
		cservice.insertCourse(cbean);
		return findAllCourseajax();
	}

		// 查詢所有課程+課程圖片ajax
		@ResponseBody
		@GetMapping("/courseaj")
		public List<CourseDTO> findAllCourseajax() {
			List<CourseBean> cbeans = cservice.selectAllCourse();
//			System.out.println("insert後:"+cbeans);
			for(CourseBean cbean:cbeans) {
//				System.out.println(cbean.getCoachId());
//				System.out.println(cbean.getCoach().getCoachId());
			}
			return convertDTO.convertCourseDTOList(cbeans);
		}
	// 查詢所有課程+課程圖片
	@GetMapping("/course")
	public String findAllCourse(Model m) {
		List<CourseBean> cbeans = cservice.selectAllCourse();
		List<CoachBean> chbeans = chservice.selectAllCoach();
		m.addAttribute("chbeans", chbeans);
		m.addAttribute("cbeans", cbeans);
		return "/backgymlife/course/selectAllCourse";
	}
	// 查詢課程ById
	@ResponseBody
	@GetMapping("/course/find")
	public CourseDTO selectCourseById(@RequestParam("courseId") Integer courseId) {
		CourseBean cbean = cservice.selectCourseById(courseId);
		return convertDTO.convertCourseDTO(cbean);
	}
	// 修改課程
	@ResponseBody
	@PutMapping("/course/update")
	public List<CourseDTO> updateCourse(@RequestParam("coachId") Integer coachId,
			@RequestParam("courseId") Integer courseId,
			@RequestParam("courseName") String courseName,
			@RequestParam("courseCost") Integer courseCost,
			@RequestParam("courseIntroduce") String courseIntroduce, Model m) {
		CoachBean chbean = chservice.selectCoachById(coachId);
		cservice.updateCourse(chbean,courseId, courseName,  courseIntroduce, courseCost);
		return findAllCourseajax();
	}

	// 刪除課程
	@ResponseBody
	@DeleteMapping("/course/delete")
	public List<CourseDTO> deleteCourseById(@RequestParam("courseId") Integer courseId, Model m) {
		cservice.deleteCourse(courseId);
//		return selectAllCourse(m);
		return findAllCourseajax();
	}
	// 刪除課程圖片
	@ResponseBody
	@DeleteMapping("/course/deleteImg")
	public CourseDTO deleteCourseImg(@RequestParam("imageId") Integer imageId,@RequestParam("courseId") Integer courseId) {
		iservice.deleteCourseImg(imageId);
		return selectCourseById(courseId);
	}

	//查詢單張圖片
	@GetMapping("/courseImage/{imageId}")
	public ResponseEntity<byte[]> courseImage(@PathVariable Integer imageId){
		ImageBean ibean = iservice.findCourseImg(imageId);
		byte[] photoFile = ibean.getImageData();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		//檔案, header, HttpStatus
		return new ResponseEntity<byte[]>(photoFile,headers,HttpStatus.OK);
	}

	// 新增課程圖片
	@ResponseBody
	@PostMapping("/course/insertImg")
	public CourseDTO insertCourseImg(@RequestParam("courseId") Integer courseId,
			@RequestParam("courseImg") MultipartFile image) throws IOException {
		System.out.println(courseId);
		CourseBean cbean = cservice.selectCourseById(courseId);

		ImageBean imageBean = new ImageBean();
		imageBean.setImageName(image.getOriginalFilename());
		imageBean.setImageData(image.getBytes());
		imageBean.setImgmimeType(image.getContentType());
		imageBean.setCourse(cbean);
		iservice.insertCourseImg(imageBean);
		return selectCourseById(courseId);
	}
	//查詢所有訂單
		@GetMapping("/course/corder")
		public String selectAllCorder(Model m) {
			List<CorderBean> corders = oservice.selectAllCorder();
			m.addAttribute("corders",corders);
			return "/backgymlife/course/selectAllCorder";
		}
		//查詢所有訂單Ajax
		@ResponseBody
		@GetMapping("/course/corderajax")
		public List<CourseDTO> selectAllCorder() {
			List<CorderBean> corders = oservice.selectAllCorder();
			return convertDTO.convertCorderDTOList(corders);
		}
		
		//修改訂單數量
		@ResponseBody
		@PutMapping("/course/corder/update")
		public List<CourseDTO> updateCorder(@RequestParam(name="corderId")Integer corderId,@RequestParam(name="corderQuantity")Integer corderQuantity,@RequestParam(name="corderCost")Integer corderCost) throws MessagingException {
			System.out.println(corderId);
			System.out.println(corderQuantity);
			// 建立 SimpleDateFormat 物件，指定日期時間的格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// 取得現在的日期時間
			String currentDate = sdf.format(new Date());
			oservice.updateCorder(corderId, currentDate, corderQuantity,corderCost);
			CorderBean obean = oservice.selectCorderBycorderId(corderId);
			MimeMessage message = javaMailSender.createMimeMessage();
			    MimeMessageHelper helper = new MimeMessageHelper(message, true);
			    helper.setTo(obean.getMember().getUserEmail());
			    helper.setSubject("GYMLIFE");
			    helper.setText("<html>\r\n"
			    		+ "<head>\r\n"
			    		+ "  <meta charset=\"UTF-8\">\r\n"
			    		+ "  <title>健身課程修改訂單確認</title>\r\n"
			    		+ "</head>\r\n"
			    		+ "<body>\r\n"
			    		+ "<img src=\"https://gymlife.es/wp-content/uploads/2022/01/GYMLIFELOGO-BLACK-WEB.png\" style=\"width: 200px;\">\r\n"
			    		+ "  <h2>健身課程訂單確認</h2>\r\n"
			    		+ "\r\n"
			    		+ "  <p>親愛的 "+obean.getMember().getUserName()+"，</p>\r\n"
			    		+ "  <p>我們已審核完您提出的健身課程訂單修改!</p>\r\n"
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
			return selectAllCorder();
		}
		//刪除訂單
		@ResponseBody
		@DeleteMapping("/course/corder/delete")
		public List<CourseDTO> deleteCorder(@RequestParam("corderId")Integer corderId) {
			oservice.deleteCorder(corderId);
			return selectAllCorder();
		}
		// 訂單分析跳轉
		@GetMapping("/course/corderanalyze")
		public String corderAnalyzechange(Model m) {
			return "/backgymlife/course/corderAnalyze";
		}
		// 訂單分析圖
		@ResponseBody
		@GetMapping("/course/corder/analyze")
		public List<CourseDTO> corderAnalyze() {
			List<CourseBean> cbeans = cservice.selectAllCourse();
			return convertDTO.convertCourseDTOList(cbeans);
		}
	/*
	//查詢教練圖片
	@GetMapping("/coachImage/{courseId}")
	public ResponseEntity<byte[]> coachImage(@PathVariable Integer courseId){
		ImageBean ibean = iservice.findCoachImg(courseId);
		byte[] photoFile = ibean.getImageData();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
										//檔案, header, HttpStatus
		return new ResponseEntity<byte[]>(photoFile,headers,HttpStatus.OK);
	}
*/
}
