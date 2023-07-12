package tw.gymlife.activity.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.gymlife.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {


}
   




