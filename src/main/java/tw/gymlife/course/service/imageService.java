package tw.gymlife.course.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.course.model.ImageBean;
import tw.gymlife.course.model.ImageRepostiory;

@Service
public class imageService {
	@Autowired
	private ImageRepostiory iRepo;
	//新增圖片
	public void insertCourseImg(ImageBean ibean) {
		iRepo.save(ibean);
	}
	//查詢所有圖片
	public List<ImageBean> selectAllCourseImage() {
		return iRepo.findAll();
	}
	
	//查詢圖片by CourseId
	public List<ImageBean> selectCourseImageById(int courseId) {
		List<ImageBean> ibeans = iRepo.selectCourseImageById(courseId);
		return ibeans;
	}
	//查詢圖片by ImageId
	public ImageBean findCourseImg(Integer imageId) {
		Optional<ImageBean> optional = iRepo.findById(imageId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	//查詢教練圖片
	public ImageBean findCoachImg(Integer courseId) {
		List<ImageBean> ibeans = iRepo.selectCourseImageById(courseId);
		return ibeans.get(0);
	}

	//更新圖片
	@Transactional
	public void updateCourseImg(Integer imageId, String imageName, byte[] imageData, String imgmimeType
			) {
		Optional<ImageBean> optional = iRepo.findById(imageId);
		if(optional.isPresent()) {
			ImageBean ibean = optional.get();
			ibean.setImageName(imageName);
			ibean.setImgmimeType(imgmimeType);
			ibean.setImageData(imageData);
		}
		
		System.out.println("no update data");

	}

	//刪除圖片
	public void deleteCourseImg(int imageId) {
		iRepo.deleteById(imageId);
	}


}
