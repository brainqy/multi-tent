package com.yash.ytms.repository;

import com.yash.ytms.domain.InterviewSlot;
import com.yash.ytms.domain.Job;
import com.yash.ytms.domain.ScheduleEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-04-2024
 */
@Repository
public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    @Query("SELECT r FROM InterviewSlot r WHERE r.job = :job and r.scheduleUser.emailAdd=:userEmail")
    List<InterviewSlot> getAllInterviewsyJobId(@Param("job")Job job,@Param("userEmail") String userEmail);
    @Query("SELECT r FROM InterviewSlot r WHERE r.scheduleUser.emailAdd=:userEmail")
    List<InterviewSlot> getByEmail(String userEmail);
    @Query("select r from InterviewSlot r where r.scheduleUser.emailAdd <> :loggedUserEmail")
    List<InterviewSlot> getAllInterviewSlotsExceptLoggedUser(@Param("loggedUserEmail") String loggedUserEmail);

}
