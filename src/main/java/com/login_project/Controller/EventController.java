package com.login_project.Controller;

import com.login_project.Entity.Event;
import com.login_project.Entity.EventApplication;
import com.login_project.Entity.UserEntity;
import com.login_project.Repo.EventApplicationRepository;
import com.login_project.Repo.EventRepository;
import com.login_project.Repo.LoginRepo; // Assuming LoginRepo is your user repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Used to get the currently logged-in user
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events") // Base path for event-related endpoints for users
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventApplicationRepository applicationRepository;

    @Autowired
    private LoginRepo loginRepo; // Inject the repository for UserEntity

    /**
     * Endpoint for users to see all available events.
     * @return A list of all events.
     */
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    /**
     * Endpoint for a logged-in user to apply for a specific event.
     * Takes the event ID as a path variable.
     * @param id The ID of the event to apply for.
     * @param principal Represents the currently authenticated user.
     * @return A success or error message.
     */
    @PostMapping("/{id}/apply")
    public ResponseEntity<?> applyForEvent(@PathVariable Long id, Principal principal) { // <-- Change return to ResponseEntity<?>

        // 1. Find the user (you already do this)
        UserEntity user = loginRepo.findByEmailid(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + principal.getName()));

        // 2. --- THIS IS THE NEW MANDATORY CHECK ---
        if (user.getName() == null || user.getName().isEmpty() ||
                user.getMobileNumber() == null || user.getMobileNumber().isEmpty() ||
                user.getCollege() == null || user.getCollege().isEmpty()) {

            // Return a specific error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Please complete your profile before applying for an event."));
        }
        // ---------------------------------------------

        // 3. Find the event (you already do this)
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        // 4. Check for duplicates (you already do this)
        if (applicationRepository.existsByUserAndEvent(user, event)) {
            return ResponseEntity.badRequest().body(Map.of("error", "You have already applied for this event."));
        }

        // 5. Save the application (you already do this)
        EventApplication application = new EventApplication();
        application.setUser(user);
        application.setEvent(event);
        applicationRepository.save(application);

        return ResponseEntity.ok(Map.of("message", "Application submitted successfully!")); // <-- Return a JSON map
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getImageData() == null || event.getImageType() == null) {
            return ResponseEntity.notFound().build(); // No image data found
        }

        HttpHeaders headers = new HttpHeaders();
        // Set the correct Content-Type based on the stored image type
        headers.setContentType(MediaType.parseMediaType(event.getImageType()));

        return new ResponseEntity<>(event.getImageData(), headers, HttpStatus.OK);
    }
}