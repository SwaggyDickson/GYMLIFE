package tw.gymlife.com.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.com.dao.ComPicRepository;
import tw.gymlife.com.dao.ComFrontRepository;
import tw.gymlife.com.model.Commoditys;

@Service
public class ComFrontService {

	
	@Autowired
	private ComFrontRepository comFrontRepo;

	@Autowired
	private ComPicRepository picRepo;
	
	private String comStatus ="上架";

	/*------------shopList頁功能開始------------*/
	// 查詢所有商品
	public List<Commoditys> getAllComList() {

		return comFrontRepo.findByComStatus(comStatus);
	}

	// 關鍵字查詢
	public List<Commoditys> getKeywordComList(String keyword) {


		return comFrontRepo.findByComStatusAndComNameLike(comStatus, keyword);
	}

	// 價格高低排序
	public List<Commoditys> getSortByPrice() {

		return comFrontRepo.findByComStatusOrderByComPriceDesc(comStatus);
	}

	// 類別查詢
	public List<Commoditys> getComTypeList(String comType) {

		return comFrontRepo.findByComStatusAndComTypeLike(comStatus, comType);

	}

	public List<Commoditys> getTopThreeCommoditys(){
		
		
		
		return comFrontRepo.getTopThreeCommoditys(comStatus);
		
		
	}
	/*------------shop單筆商品頁功能開始------------*/
	//每次點擊單筆商品 次數+1
	public boolean updateClickTime(Integer comId) {

		Optional<Commoditys> optional = comFrontRepo.findById(comId);
		if (optional.isPresent()) {
			Commoditys com = optional.get();
			com.setClickTime(com.getClickTime() + 1);
			comFrontRepo.save(com);
			return true;
		}
		return false;
	}

	//加入購物車查找單筆商品list
	public List<Commoditys> getOneComList(Integer comId) {
		List<Commoditys> returnList = new ArrayList<>();
		Optional<Commoditys> optional = comFrontRepo.findById(comId);

		if (optional.isPresent()) {
			Commoditys com = optional.get();
			returnList = Collections.singletonList(com);
		}

		return returnList;
	}

	//生成訂單用
	public Commoditys getCommoditys(Integer comId) {
		Optional<Commoditys> optional = comFrontRepo.findById(comId);

		if (optional.isPresent()) {
			Commoditys com = optional.get();
			return com;
		}
		return null;
	}
	/*------------shop單筆商品頁功能結束------------*/

	//付款後更新購買數量
	public void updateComBuyNumber(int comId, int comBuyNumber){
		
		Optional<Commoditys> option = comFrontRepo.findById(comId);
		if(option.isPresent()) {
			Commoditys comBean = option.get();
			comBean.setComBuyNumber(comBean.getComBuyNumber()+ comBuyNumber);
			comFrontRepo.save(comBean);
		}
		
	}
	
	
	// 關鍵字查詢(JS)
	public List<Commoditys> getKeywordComListJS(String keyword) {

		String comStatus = "上架";
		return comFrontRepo.findByComStatusAndComNameLike(comStatus, keyword);
	}

	
}
