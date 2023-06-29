package tw.gymlife.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tw.gymlife.com.dao.ComUtil;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;

@Service("comUtilService")
public class ComUtilService {

	@Autowired
	private ComUtil comUtil;
	
	// 取得商品圖片
	public String getFileName(MultipartFile part) {
		return comUtil.getFileName(part);
	}
	
	// 將Bean轉成DTO
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList){
		return comUtil.convertCommodityDTOList(comList);
	}
	
	
	
	// 將圖片轉檔
	public String convertImageToBase64(byte[] imageBytes) {
		return comUtil.convertImageToBase64(imageBytes);
	}
	
	// 價格排序高->低
	public List<CommodityDTO> sortByHighPrice(List<CommodityDTO> comDTOList){
		return comUtil.sortByHighPrice(comDTOList);
	}
	// 價格排序低-> 高
	public List<CommodityDTO> sortByLowPrice(List<CommodityDTO> comDTOList){
		return comUtil.sortByLowPrice(comDTOList);
	}
}

