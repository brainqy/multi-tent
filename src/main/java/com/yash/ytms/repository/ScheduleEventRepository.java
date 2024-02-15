package com.yash.ytms.repository;

import com.yash.ytms.domain.ScheduleEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduleEventRepository extends JpaRepository<ScheduleEvent, Integer> {

    @Query("select e from ScheduleEvent e where e.scheduleUser.emailAdd=:trainerEmail")
    List<ScheduleEvent> findAllEventsByTrainerEmail(@Param("trainerEmail") String trainerEmail);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("delete from ScheduleEvent se where se.eventId = :eventId and se.scheduleUser.emailAdd=:userEmail")
    Integer deleteScheduleEventByEventId(@Param("eventId") Integer eventId,
                                         @Param("userEmail") String userEmail);
}
