package tw.gymlife.com.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.gymlife.com.model.ComLog;
import tw.gymlife.com.model.ComPic;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.OrderDetails;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;
import tw.gymlife.com.service.ComFrontService;
import tw.gymlife.com.service.ComFrontUtilService;
import tw.gymlife.com.service.ComLogService;
import tw.gymlife.com.service.ComOrderService;
import tw.gymlife.com.service.ComService;
import tw.gymlife.com.service.ComUtilService;

@Controller
public class ComBackController {

	@Autowired
	private ComService comService; // 利用Spring建立service
	@Autowired
	private ComUtilService comUtilService; // 利用Spring建立service

	@Autowired
	private ComOrderService orderService;
	@Autowired
	private ComFrontService comFService;
	@Autowired
	private ComFrontUtilService comFUtilService;

	@Autowired
	private ComLogService logService;

	@GetMapping("/backHomePage")
	public String getBackHomePage() {

		return "backgymlife/com/backHomePage";
	}

	@GetMapping("/addItemPage")
	public String getAddItemPage() {

		return "backgymlife/com/addItemPage";
	}

	// 新增
	@PostMapping("/addItem.func")
	public String getAddItemFunc(@RequestParam("comName") String comName, @RequestParam("comNumber") int comNumber,
			@RequestParam("comPrice") int comPrice, @RequestParam("comType") String comType,
			@RequestParam("comStatus") String comStatus, @RequestParam("comContent") String comContent, Model model,
			MultipartHttpServletRequest request) {
//		String virLoc = request.getServletContext().getRealPath("/WEB-INF/resource/IMG");

		try {
			// 將請求參數資料設定到 comBean 物件中
			Commoditys comBean = new Commoditys();
			comBean.setComName(comName);
			comBean.setComNumber(comNumber);
			comBean.setComPrice(comPrice);
			comBean.setComType(comType);
			comBean.setComStatus(comStatus);
			comBean.setComContent(comContent);
			comBean.setComBuyNumber(0);
			comBean.setClickTime(0);

			Set<ComPic> pics = new LinkedHashSet<>();
			List<MultipartFile> comPicPart = request.getFiles("comPic"); // 取得圖片
			for (MultipartFile comPic : comPicPart) {
				if (!comPic.isEmpty()) {
					ComPic comPicBean = new ComPic();
					String loc = comUtilService.getFileName(comPic); // UUID商品圖片
//					comPic.transferTo(new File(virLoc + "/" + loc)); //儲存圖片

					comPicBean.setComPicFile(comPic.getBytes());
					comPicBean.setComPicName(loc);
					comPicBean.setCommoditys(comBean);
					pics.add(comPicBean);
				}
			}
			comBean.setComPics(pics);

			Commoditys returnList = comService.addCom(comBean);

			// 將資料轉發到商品頁面
			if (returnList != null) {
				model.addAttribute("msg", "成功新增商品");
			} else {
				model.addAttribute("msg", "發生錯誤，新增失敗");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "發生錯誤，新增失敗");
		}

		return "backgymlife/com/addItemPage";
	}

	// 查詢全部商品
	@RequestMapping("/itemListPage.func")
	public String getItemListFunc(Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
//		String virLoc = request.getServletContext().getRealPath("/WEB-INF/resource/IMG");

		try {
			List<Commoditys> returnList = comService.checkAllCommoditys();
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			m.addAttribute("comList", comDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backgymlife/com/itemList";
	}

	// 修改單張圖片
	@Transactional
	@PutMapping("/editOnePic.func")
	public ResponseEntity<Object> getEditItemPicFunc(@RequestParam("imageId") int imageId,
			@RequestParam("comId") int comId, MultipartHttpServletRequest request) {

		try {
			MultipartFile comPicPart = request.getFile("imageFile");
			String loc = comUtilService.getFileName(comPicPart); // 商品名稱
			ComPic comPicBean = new ComPic();
			comPicBean.setComPicFile(comPicPart.getBytes());
			comPicBean.setComId(comId);
			comPicBean.setComPicId(imageId);
			comPicBean.setComPicName(loc);
			comService.updateComPic(comPicBean, imageId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}

	// 修改商品資訊
	@Transactional
	@PutMapping("/editItemInfo.func")
	public ResponseEntity<Object> getEditItemInfoFunc(@RequestParam("comId") int id,
			@RequestParam("comNameAjax") String comName, @RequestParam("comNumberAjax") int comNumber,
			@RequestParam("comPriceAjax") int comPrice, @RequestParam("comContentAjax") String comContent,
			@RequestParam("comStatusAjax") String comStatus, @RequestParam("comTypeAjax") String comType) {

		try {
			// 將前台輸入的資料傳送到Goods(Java Bean)裡面
			Commoditys comBean = new Commoditys();
			comBean.setComId(id); // id
			comBean.setComName(comName); // 商品名稱
			comBean.setComNumber(comNumber); // 商品數量
			comBean.setComPrice(comPrice); // 商品價格
			comBean.setComContent(comContent); // 商品描述
			comBean.setComStatus(comStatus); // 上下架狀態
			comBean.setComType(comType); // 商品類別

			comService.updateCom(comBean, id);

			return ResponseEntity.ok().build();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// 專門回傳圖片的controller
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadImg(@PathVariable Integer id) {

		// springMVC調整HTTPresponse的細節，調整header之類的
		ComPic photo = comService.getPhotoById(id);
		byte[] photoFile = photo.getComPicFile();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG); // 讓程式預設圖片的header
		// 檔案 , headers, 狀態200
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}

	// 一鍵上下架
	@Transactional
	@PutMapping("/changeStatus.func")
	public ResponseEntity<String> getChangeStatus(@RequestBody Map<String, Object> request) {

		try {
			int comId = Integer.parseInt((String) request.get("comId"));
			boolean isChecked = (boolean) request.get("isChecked"); // 更改後的狀態
			String status = isChecked ? "上架" : "下架";
			Commoditys comBean = new Commoditys();
			comBean.setComId(comId);
			comBean.setComStatus(status);
			comService.changeStatus(comBean, comId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// 關鍵字查詢
	@PostMapping("/checkByKeyword.func")
	public String getCheckByKeyword(@RequestParam("keywords") String keywords, Model model) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comService.seachByKeyword(keywords);

			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			model.addAttribute("comList", comDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backgymlife/com/itemList";
	}

	// 價格排序
	@PostMapping("/sortByPrice.func")
	public String getSortByPrice(@RequestParam("sortPrice") String sortByPrice, Model model) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		List<CommodityDTO> sortList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comService.searchByPrice();
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
				if (sortByPrice.equals("0")) {
					sortList = comUtilService.sortByHighPrice(comDTOList);
				} else {
					sortList = comUtilService.sortByLowPrice(comDTOList);
				}
			}
			// 將資料轉發到商品頁面
			model.addAttribute("comList", sortList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "backgymlife/com/itemList";
	}

	// 類別排序
	@PostMapping("/sortByType.func")
	public String getSortByType(@RequestParam("sortType") String sortByType, Model model) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
//			String virLoc = request.getServletContext().getRealPath("/WEB-INF/resource/IMG");
		try {
			List<Commoditys> returnList = comService.searchByType(sortByType);
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			model.addAttribute("comList", comDTOList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backgymlife/com/itemList";
	}

	// 上下架查詢
	@PostMapping("/sortByStatus.func")
	public String getSortByStatus(@RequestParam("sortSattus") String SortByStatus, Model model) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comService.searchByStatus(SortByStatus);
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			model.addAttribute("comList", comDTOList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "backgymlife/com/itemList";
	}

	/* -------------- 分析列表------------ */
	@GetMapping("/analyze.func")
	public String getanalyPage(Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comService.checkAllCommoditys();
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			m.addAttribute("comList", comDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "backgymlife/com/analyzeList";
	}

	// 購買人數分析圖表
	@GetMapping("/getChatByComBuyNumber.func")
	public ResponseEntity<Object> getChatByComBuyNumberJson() {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comService.sortByComBuyNumber();
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉為JSON轉發到商品頁面
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDTOList = objectMapper.writeValueAsString(comDTOList);

			return ResponseEntity.ok(jsonDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	// 瀏覽人數分析圖表
	@GetMapping("/getChatByClickTime.func")
	public ResponseEntity<Object> getChatByClickTimeJson() {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comService.sortByClickTime();
			if (returnList != null) {
				comDTOList = comUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉為JSON轉發到商品頁面
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDTOList = objectMapper.writeValueAsString(comDTOList);

			return ResponseEntity.ok(jsonDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	// 將圖表輸出成圖檔
	@PostMapping("/importImg.func")
	public ResponseEntity<Object> getrImportImg(@RequestParam("image") String image) {

		System.out.println("image: " + image);
		String base64Data = image.substring(image.indexOf(',') + 1);
		byte[] imageData = Base64.getDecoder().decode(base64Data);

		String desktopPath = System.getProperty("user.home") + "/Desktop";
		String filePath = desktopPath + "/chart.png";

		try (OutputStream outputStream = new FileOutputStream(filePath)) {

			outputStream.write(imageData);
			System.out.println("图像已成功保存到桌面。");
			return ResponseEntity.ok().build();
		} catch (IOException e) {

			System.out.println("保存图像时发生错误。");
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	// 通知
	@PostMapping("/orderAnnotation")
	public ResponseEntity<Object> getOrderAnnotation() {

		// 找出所以未付款的商品
		List<Orders> returnOrderList = orderService.getAllPayedOrders();

		List<Commoditys> returnComList = new ArrayList<>();

		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : returnOrderList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue; // 跳出迴圈
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(returnOrderList, commodityDTOList);

		// 將資料轉為JSON轉發到商品頁面
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonDTOList = objectMapper.writeValueAsString(ordersDTOList);
			return ResponseEntity.ok(jsonDTOList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.notFound().build();
	}

	// 跳轉訂單頁面
	@GetMapping("/getOrder.func")
	public String getOrderListPage(Model model) {

		List<Orders> allOrdersList = orderService.getAllOrders();

		List<Commoditys> returnComList = new ArrayList<>();

		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : allOrdersList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue; // 跳出迴圈
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(allOrdersList, commodityDTOList);
		model.addAttribute("orderList", ordersDTOList);

		return "backgymlife/com/comOrderList";
	}

	// 取得訂單LIST
	@GetMapping("getAllOrders")
	public ResponseEntity<Object> getAllOrders() {

		List<Orders> allOrdersList = orderService.getAllOrders();

		List<Commoditys> returnComList = new ArrayList<>();

		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : allOrdersList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue; // 跳出迴圈
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(allOrdersList, commodityDTOList);
		System.out.println("DTOLIST: " + ordersDTOList);
		// 將資料轉為JSON轉發到商品頁面
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonDTOList = objectMapper.writeValueAsString(ordersDTOList);
			return ResponseEntity.ok(jsonDTOList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return ResponseEntity.notFound().build();
	}

	// JS 出貨按鈕
	@Transactional
	@PutMapping("/sendComStatus.func")
	public ResponseEntity<Object> getSendComSataus(@RequestParam("orderId") String orderUuid) {

		System.out.println("uuid: " + orderUuid);

		try {
			Orders ordersByUuid = orderService.getOrdersByUuid(orderUuid);
			ordersByUuid.setOrderPayment(4);
			orderService.setOrderStatusByAdmin(ordersByUuid);
			System.out.println("更新成功");
			return ResponseEntity.ok().build();

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}

	}

	// LOG
	@GetMapping("/getLog.func")
	public String getAllLog(Model model) {

		List<ComLog> allLogList = logService.getAllLog();
		// 將資料轉為JSON轉發到商品頁面
			model.addAttribute("logList",allLogList);
			
			return "backgymlife/com/comLog";

	}

}
