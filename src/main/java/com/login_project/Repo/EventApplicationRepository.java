package com.login_project.Repo;

import com.login_project.Entity.Event;
import com.login_project.Entity.EventApplication;
import com.login_project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventApplicationRepository extends JpaRepository<EventApplication, Long> {

    // Custom query to find all applications associated with a specific event ID
    List<EventApplication> findByEventId(Long eventId);

    // Custom query to check if an application already exists for a given user and event
    boolean existsByUserAndEvent(UserEntity user, Event event);
}
