package tw.gymlife.com.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;

@Repository
public interface ComUtil {

	// 商品圖片名稱存取
	public String getFileName(MultipartFile part);

	// 將Bean轉成DTO
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList);

	// 將圖片轉檔
	public String convertImageToBase64(byte[] imageBytes);

	// 價錢排序高->低
	public List<CommodityDTO> sortByHighPrice(List<CommodityDTO> comDTOList);

	// 價錢排序低->高
	public List<CommodityDTO> sortByLowPrice(List<CommodityDTO> comDTOList);

}
