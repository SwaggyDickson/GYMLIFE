package tw.gymlife.activity.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import tw.gymlife.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

}




