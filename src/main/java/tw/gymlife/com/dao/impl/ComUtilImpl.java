package tw.gymlife.com.dao.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import tw.gymlife.com.dao.ComUtil;
import tw.gymlife.com.model.ComPic;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;

@Repository
public class ComUtilImpl implements ComUtil {

	// 取得商品圖片
	@Override
	public String getFileName(MultipartFile part) {
		
		String fileName = part.getOriginalFilename();
		// 生成唯一文件名
		String extension = fileName.substring(fileName.lastIndexOf("."));
		String uniqueName = UUID.randomUUID().toString();
		String uniqueFilename = uniqueName + extension;
		return uniqueFilename;
	}

	// 將Bean轉成DTO
	@Override
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList) {
		
		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			for (Commoditys com : comList) {
				CommodityDTO comDTO = new CommodityDTO(); // 將取出資訊配合轉成圖片裝進DTO中回傳
				comDTO.setComId(com.getComId()); // id
				comDTO.setComName(com.getComName()); // 商品名稱
				comDTO.setComNumber(com.getComNumber()); // 商品數量
				comDTO.setComPrice(com.getComPrice()); // 商品價格
				comDTO.setComType(com.getComType()); // 商品類別
				comDTO.setComContent(com.getComContent()); // 商品描述
				comDTO.setComStatus(com.getComStatus()); // 商品狀態
				comDTO.setComBuyNumber(com.getComBuyNumber()); // 商品總購買數
				comDTO.setClickTime(com.getClickTime());

				Map<Integer, byte[]> comPicInfo = new LinkedHashMap<>();
				for (ComPic comPic : com.getComPics()) {
					
//					String comPicBase64 = convertImageToBase64(comPic.getComPicFile());
					
					
					comPicInfo.put(comPic.getComPicId(), comPic.getComPicFile());
					comDTO.setComPicInfo(comPicInfo);
				}
				comDTOList.add(comDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comDTOList;
	}


	// 將圖片轉檔
	@Override
	public String convertImageToBase64(byte[] imageBytes) {

		String base64Image = null;
		try {
			base64Image = Base64.getEncoder().encodeToString(imageBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64Image;
	}

	// 價格排序高->低
	@Override
	public List<CommodityDTO> sortByHighPrice(List<CommodityDTO> comDTOList) {
		
		Comparator<CommodityDTO> comparator = new Comparator<CommodityDTO>() {
			@Override
			public int compare(CommodityDTO o1, CommodityDTO o2) {

				return Integer.compare(o2.getComPrice(), o1.getComPrice());
				// 價格高的排前面，使用 o2 和 o1 互換位置
			}
		};
		// 使用 Collections.sort() 方法進行排序
		Collections.sort(comDTOList, comparator);
		// 將取得的商品全部傳入一個標籤裡面
		return comDTOList;
	}

	// 價格排序低-> 高
	@Override
	public List<CommodityDTO> sortByLowPrice(List<CommodityDTO> comDTOList) {
		
		Comparator<CommodityDTO> comparator = new Comparator<CommodityDTO>() {
			@Override
			public int compare(CommodityDTO o1, CommodityDTO o2) {

				return Integer.compare(o1.getComPrice(), o2.getComPrice());
				// 價格低的排前面，使用 o1 和 o2 互換位置
			}
		};
		// 使用 Collections.sort() 方法進行排序
		Collections.sort(comDTOList, comparator);
		return comDTOList;
	}


}
