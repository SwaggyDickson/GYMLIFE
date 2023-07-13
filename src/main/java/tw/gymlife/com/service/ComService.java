package tw.gymlife.com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.gymlife.com.dao.ComPicRepository;
import tw.gymlife.com.dao.ComRepository;
import tw.gymlife.com.model.ComPic;
import tw.gymlife.com.model.Commoditys;

@Service
public class ComService {

	@Autowired
	private ComRepository cRepo;
	@Autowired
	private ComPicRepository picRepo;

	// 新增商品
	public Commoditys addCom(Commoditys com) {

		return cRepo.save(com);
	}

	// 查詢全部(上架靠前)
	public List<Commoditys> checkAllCommoditys() {
		Sort sort = Sort.by(Sort.Direction.DESC, "comStatus");
		return cRepo.findAll(sort);
	}

	// 修改單張圖片
	public String updateComPic(ComPic comPicBean, int comPicId) {

		Optional<ComPic> optional = picRepo.findById(comPicId);

		if (optional.isPresent()) {

			ComPic comPic = optional.get();
			comPic.setComId(comPicBean.getComId());
			comPic.setComPicFile(comPicBean.getComPicFile());
			comPic.setComPicId(comPicBean.getComPicId());
			comPic.setComPicName(comPicBean.getComPicName());
			return "修改成功";
		}

		return "沒有此資料";
	}

	// 修改商品資訊
	public String updateCom(Commoditys comBean, int comId) {

		Optional<Commoditys> optional = cRepo.findById(comId);

		if (optional.isPresent()) {

			Commoditys com = optional.get();
			com.setComId(comBean.getComId());
			com.setComName(comBean.getComName());
			com.setComNumber(comBean.getComNumber());
			com.setComPrice(comBean.getComPrice());
			com.setComContent(comBean.getComContent());
			com.setComStatus(comBean.getComStatus());
			com.setComType(comBean.getComType());

			return "修改成功";
		}

		return "沒有此資料";
	}

	// ID查詢圖片
	public ComPic getPhotoById(Integer id) {

		Optional<ComPic> option = picRepo.findById(id);

		if (option.isPresent()) {
			return option.get();
		}
		return null;
	}

	// 一鍵上下架
	public String changeStatus(Commoditys comBean, int comId) {

		Optional<Commoditys> optional = cRepo.findById(comId);

		if (optional.isPresent()) {

			Commoditys com = optional.get();
			com.setComId(comBean.getComId());
			com.setComStatus(comBean.getComStatus());

			return "修改成功";
		}

		return "沒有此資料";
	}

	// 關鍵字查詢
	// 如果用id以外的CRUD則要用上JPQL的語句
	public List<Commoditys> seachByKeyword(String keyword) {

		List<Commoditys> returnList = cRepo.searchByKeyword(keyword);
		return returnList;
	}

	// 價格排序
	public List<Commoditys> searchByPrice() {

		List<Commoditys> returnList = cRepo.findByOrderByComPriceDesc();
		return returnList;
	}

	// 類別查詢
	public List<Commoditys> searchByType(String keyword) {

		List<Commoditys> returnList = cRepo.findByComTypeContainingOrderByComStatusDesc(keyword);
		return returnList;
	}

	// 上下架查詢
	public List<Commoditys> searchByStatus(String keyword) {

		List<Commoditys> returnList = cRepo.findByComStatusContaining(keyword);
		return returnList;
	}

	// 購買數量排序
	public List<Commoditys> sortByComBuyNumber() {
		Sort sort = Sort.by(Sort.Direction.DESC, "comBuyNumber");
		List<Commoditys> returnList = cRepo.findAll(sort);
		return returnList;
	}

	// 瀏覽次數排序
	public List<Commoditys> sortByClickTime() {
		Sort sort = Sort.by(Sort.Direction.DESC, "clickTime");
		List<Commoditys> returnList = cRepo.findAll(sort);
		System.out.println(returnList);
		return returnList;
	}
	
}
