package com.login_project.Controller;

import com.login_project.Entity.Event;
import com.login_project.Entity.EventApplication;
import com.login_project.Repo.EventApplicationRepository;
import com.login_project.Repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin") // Base path for all admin endpoints
public class AdminController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventApplicationRepository applicationRepository;

    /**
     * Endpoint for an admin to create a new event poster.
     * Takes event details (title, description, imageUrl) in the request body.
     * @param event The event object from the request body.
     * @return The saved event object.
     */
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    /**
     * Endpoint for an admin to see all users who applied for a specific event.
     * Takes the event ID as a path variable.
     * @param id The ID of the event.
     * @return A list of email addresses of the applicants.
     */
    @GetMapping("/events/{id}/applications")
    public ResponseEntity<?> getApplicationsForEvent(@PathVariable Long id) {
        List<EventApplication> applications = applicationRepository.findByEventId(id);
        // Extract just the email addresses of the applicants for simplicity
        List<String> applicantEmails = applications.stream()
                .map(EventApplication::getUsername) // Use the helper method from EventApplication
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicantEmails);
    }
}