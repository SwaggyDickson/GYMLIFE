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

	/*------------shopList頁功能開始------------*/
	// 查詢所有商品
	public List<Commoditys> getAllComList() {
		String comStatus = "上架";
		return comFrontRepo.findByComStatus(comStatus);
	}

	// 關鍵字查詢
	public List<Commoditys> getKeywordComList(String keyword) {

		String comStatus = "上架";
		return comFrontRepo.findByComStatusAndComNameLike(comStatus, keyword);
	}
	//價格高低排序
	public List<Commoditys> getSortByPrice(){
		
		String comStatus = "上架";
		return comFrontRepo.findByComStatusOrderByComPriceDesc(comStatus);
	}
	
	//類別查詢
	public List<Commoditys> getComTypeList(String comType){
		
		String comStatuString="上架";
		return comFrontRepo.findByComStatusAndComTypeLike(comStatuString, comType);
		
	}
	/*------------shop單筆商品頁功能開始------------*/
	
	public List<Commoditys> getOneComList(Integer comId){
		List<Commoditys> returnList= new ArrayList<>();
		Optional<Commoditys> optional = comFrontRepo.findById(comId);
		if(optional.isPresent()) {
			Commoditys com = optional.get();
			returnList = Collections.singletonList(com);
		}
		
		return returnList;
	}
	/*------------shop單筆商品頁功能結束------------*/

	// 關鍵字查詢(JS)
	public List<Commoditys> getKeywordComListJS(String keyword) {

		String comStatus = "上架";
		return comFrontRepo.findByComStatusAndComNameLike(comStatus, keyword);
	}
}
