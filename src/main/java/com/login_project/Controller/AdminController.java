package com.login_project.Controller;

import com.login_project.Entity.Event;
import com.login_project.Entity.EventApplication;
import com.login_project.Repo.EventApplicationRepository;
import com.login_project.Repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <-- Import MultipartFile

import java.io.IOException; // <-- Import IOException
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventApplicationRepository applicationRepository;

    /**
     * Endpoint for an admin to create a new event poster with an optional image upload.
     * Uses @RequestParam for text fields and the image file.
     * @param title Title of the event.
     * @param description Description of the event.
     * @param imageFile Optional image file for the event.
     * @return The saved event object.
     * @throws IOException If there's an error reading the image file bytes.
     */
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestParam("title") String title,
                                             @RequestParam("description") String description,
                                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);

        // Check if an image file was provided and is not empty
        if (imageFile != null && !imageFile.isEmpty()) {
            event.setImageData(imageFile.getBytes()); // Store image bytes
            event.setImageType(imageFile.getContentType()); // Store MIME type (e.g., "image/png")
        }

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    /**
     * Endpoint for an admin to update an existing event, optionally changing the image.
     * @param id The ID of the event to update.
     * @param title The updated title.
     * @param description The updated description.
     * @param imageFile The optional new image file.
     * @return The updated event object.
     * @throws IOException If there's an error reading the new image file bytes.
     */
    @PutMapping("/events/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id,
                                             @RequestParam("title") String title,
                                             @RequestParam("description") String description,
                                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        // Find the existing event by ID
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event existingEvent = optionalEvent.get();
            existingEvent.setTitle(title);
            existingEvent.setDescription(description);

            // If a new image file is provided, update the image data and type
            if (imageFile != null && !imageFile.isEmpty()) {
                existingEvent.setImageData(imageFile.getBytes());
                existingEvent.setImageType(imageFile.getContentType());
            }
            // Note: If you want to *remove* an existing image, you might need
            // another parameter or logic here to set imageData and imageType to null.

            Event updatedEvent = eventRepository.save(existingEvent);
            return ResponseEntity.ok(updatedEvent);
        } else {
            // Return 404 Not Found if the event doesn't exist
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for an admin to delete an event.
     * Also deletes all applications associated with that event first.
     * @param id The ID of the event to delete.
     * @return 204 No Content on successful deletion.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        // Important: Delete associated applications first to avoid database constraint errors
        List<EventApplication> applications = applicationRepository.findByEventId(id);
        applicationRepository.deleteAll(applications);

        // Now delete the event itself
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Standard response for successful delete
        } else {
            return ResponseEntity.notFound().build(); // Event not found
        }
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
        // Extract just the email addresses of the applicants
        List<String> applicantEmails = applications.stream()
                .map(EventApplication::getUsername)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicantEmails);
    }
}