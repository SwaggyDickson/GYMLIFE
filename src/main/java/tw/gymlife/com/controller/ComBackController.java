package tw.gymlife.com.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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


import tw.gymlife.com.dao.impl.ComUtilImpl;
import tw.gymlife.com.model.ComPic;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.service.ComService;
import tw.gymlife.com.service.ComUtilService;

@Controller
public class ComBackController {
	
	@Autowired
	private ComService comService; // 利用Spring建立service
	@Autowired
	private ComUtilService comUtilService; // 利用Spring建立service
	
	@Autowired
	private ComPic comPicBean; // 利用Spring建立Bean
	@Autowired
	private Commoditys comBean; // 利用spring建立Bean
	
	
	@GetMapping("/backHomePage")
	public String getBackHomePage() {
		
		return "backgymlife/com/backHomePage";
	}
	
	
	@GetMapping("/addItemPage")
	public String getAddItemPage() {
		
		return "backgymlife/com/addItemPage";
	}
	
	//新增
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

			Set<ComPic> pics = new LinkedHashSet<>();
			List<MultipartFile> comPicPart = request.getFiles("comPic"); // 取得圖片
			for (MultipartFile comPic : comPicPart) {
				if (!comPic.isEmpty()) {
					ComPic comPicBean = new ComPic();
					String loc = comUtilService.getFileName(comPic); //UUID商品圖片
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
	
	//查詢全部商品
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
				comService.updateComPic(comPicBean,imageId);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok().build();
		}
	
		//修改商品資訊
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

		//專門回傳圖片的controller
		@GetMapping("/download/{id}")
		public ResponseEntity<byte[]> downloadImg(@PathVariable Integer id){

			//springMVC調整HTTPresponse的細節，調整header之類的
			ComPic photo = comService.getPhotoById(id);
			byte[] photoFile = photo.getComPicFile();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); //讓程式預設圖片的header
											// 檔案      , headers,  狀態200
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
				comService.changeStatus(comBean,comId);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		//關鍵字查詢
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
		
		public int aaaaaaaaaaaaaaaa() {
			return  0 ;
		}
		
		public void bbbbbbbb() {
			
		}
}
