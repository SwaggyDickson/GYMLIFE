package tw.gymlife.com.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.service.ComFrontService;
import tw.gymlife.com.service.ComFrontUtilService;

@Controller
public class ComFrontController {

	@Autowired
	private ComFrontService comFService;
	@Autowired
	private ComFrontUtilService comFUtilService;

	
	public String getHomePage() {

		return "frontgymlife/com/front_index";
	}

	@GetMapping("/shop")
	public String getShopPage() {

		return "frontgymlife/com/front_shop";
	}

	@GetMapping("/test")
	public String test() {
		return "test/test";
	}

	/*------------shop前導頁功能開始------------*/
	@GetMapping("/type/{comType}")
	public String getShopPageLink(@PathVariable String comType, Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getComTypeList(comType);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			m.addAttribute("comList", comDTOList);
			// 返回 JSON 数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return "frontgymlife/com/front_shopList";
	}
	/*------------shop前導頁功能結束------------*/

	/*------------shopList頁功能開始------------*/
	// 查詢所有商品
	@GetMapping("/shopList.func")
	public String getShopListPage(Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comFService.getAllComList();
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			m.addAttribute("comList", comDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "frontgymlife/com/front_shopList";
	}

	// 關鍵字查詢
	@GetMapping("/userCheckByKeyWord.func")
	public String getKeywordListPage(Model m, @RequestParam("keywords") String keywords) {

		System.out.println("Keyword: " + keywords);
		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getKeywordComList(keywords);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			m.addAttribute("comList", comDTOList);
			// 返回 JSON 数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return "frontgymlife/com/front_shopList";
	}

	// 價格排序
	@GetMapping("/userSortByPrice.func")
	public String getSortByPrice(@RequestParam("sortPrice") String sortByPrice, Model model) {
		List<CommodityDTO> comDTOList = new ArrayList<>();
		List<CommodityDTO> sortList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getSortByPrice();
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
				if (sortByPrice.equals("0")) {
					sortList = comFUtilService.sortByHighPrice(comDTOList);
				} else {
					sortList = comFUtilService.sortByLowPrice(comDTOList);
				}
			}
			// 將資料轉發到商品頁面
			model.addAttribute("comList", sortList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "frontgymlife/com/front_shopList";
	}

	// 類別排序
	@GetMapping("/userSortByType.func")
	public String getSortByType(@RequestParam("sortType") String sortByType, Model model) {

		System.out.println("sortBY:" + sortByType);
		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getComTypeList(sortByType);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			model.addAttribute("comList", comDTOList);
			// 返回 JSON 数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return "frontgymlife/com/front_shopList";
	}

	/*------------shop單筆商品頁功能開始------------*/
	// 單筆資訊
	@GetMapping("/com/{comId}")
	public String getComDetails(@PathVariable("comId") Integer comId, Model m) {

		System.out.println(comId);
		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comFService.getOneComList(comId);
			if (returnList != null) {
				comDTOList = comFUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			m.addAttribute("comList", comDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "frontgymlife/com/front_oneCom";
	}

	// 加入購物車
	// 假session
	@ModelAttribute("UserId")
	public String fakeSession() {

		return "abc1";
	}

	// 加入購物車
	@PostMapping("/userAddCart.func")
	public ResponseEntity<Object> getUserAddCart(@RequestParam("comId") int comId, @RequestParam("itemNumber") int itemNum,
			@ModelAttribute("UserId") String userId, HttpServletRequest request, Model model) {

		List<Cart> cartList = new ArrayList<>();
//			String virLoc = request.getServletContext().getRealPath("/WEB-INF/resource/IMG");
//		String path = request.getServletContext().getRealPath("/static/gym/com/cart");
		
		String path="C:\\gymLifeBoot\\workspace\\gymlifeproject\\src\\main\\resources\\static\\gym\\com\\cart";
		
		System.out.println("path: "+ path);
		List<CommodityDTO> comDTOList = new ArrayList<>();

		
		fakeSession(); // 假session
		System.out.println("userID: " + userId);
		System.out.println("comId: " + comId);
		System.out.println("數量: " + itemNum);
		try {
			// 利用此去做事

			List<Commoditys> returnList = comFService.getOneComList(comId);

			if (returnList != null) {
				cartList = comFUtilService.convertCommodityCartList(returnList, itemNum);
				cartList = comFUtilService.loadJson(cartList,  path, userId);

				// 做完寫入後Json後
				System.out.println("已加入購物車");
				comDTOList = comFUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			model.addAttribute("OneComList", comDTOList);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	
	//刪除購物車商品
		@PostMapping(path = "/getRemoveCart.func")
		public String getRemoveCart(@ModelAttribute("UserId") String userId, @RequestParam("comId") int comId, HttpServletRequest request, Model model) {
			
			System.out.println("userID: " + userId);
			System.out.println("comID: " + comId);
//			String path = request.getServletContext().getRealPath("/WEB-INF/resource/Cart"); //取得路徑
			String path="C:\\gymLifeBoot\\workspace\\gymlifeproject\\src\\main\\resources\\static\\gym\\com\\cart";
			List<Cart> cartList = new ArrayList<>();

			try {
				cartList = comFUtilService.deleteCart(path, userId, comId);
				System.out.println(cartList);
				
				model.addAttribute("CartList", cartList);
				model.addAttribute("length", cartList.size());
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "redirect:/getIntoCart.func";
		}

	/*------------shop單筆商品頁功能結束------------*/
	// 關鍵字查詢(JS)
	@GetMapping("/userCheckByKeyWord.fun")
	public ResponseEntity<Object> getKeywordListPageJS(Model m, @RequestParam("keywords") String keywords) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getKeywordComList(keywords);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			// 將資料轉為JSON轉發到商品頁面
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDTOList = objectMapper.writeValueAsString(comDTOList);

			m.addAttribute("comList", comDTOList);
			// 返回 JSON 数据
			return ResponseEntity.ok(jsonDTOList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/test.func")
	public ResponseEntity<Object> testAjax() {

		System.out.println("123");
		return ResponseEntity.ok().build();
	}

	/*------------shopList頁功能結束------------*/
	/*------------shopCart頁功能開始------------*/
	
	//進入購物車
	@GetMapping("/getIntoCart.func")
	public String getUserCart(@ModelAttribute("UserId") String userId, Model model) {
		
		String path="C:\\gymLifeBoot\\workspace\\gymlifeproject\\src\\main\\resources\\static\\gym\\com\\cart";
		List<Cart> cartList = new ArrayList<>();

		try {
			cartList = comFUtilService.goIntoCart(path, userId);
			if (cartList != null) {
				System.out.println(cartList);
				model.addAttribute("CartList", cartList);
				model.addAttribute("length",cartList.size());
			}

		} catch ( Exception e) {
			e.printStackTrace();
		}
		
		return "frontgymlife/com/front_shoppingCart";
	}
	/*------------shopCart頁功能結束------------*/
	
	

}
