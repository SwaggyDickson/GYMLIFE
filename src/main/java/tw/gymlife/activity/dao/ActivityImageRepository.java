package tw.gymlife.activity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.gymlife.activity.model.ActivityImage;

public interface ActivityImageRepository extends JpaRepository<ActivityImage, Integer> {

}
