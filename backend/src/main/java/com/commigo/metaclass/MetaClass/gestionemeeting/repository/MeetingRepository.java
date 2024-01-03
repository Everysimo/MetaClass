package com.commigo.metaclass.MetaClass.gestionemeeting.repository;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MeetingRespository")
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
