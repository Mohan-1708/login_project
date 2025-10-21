package com.login_project.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    // --- CHANGE THIS ---
    @Lob // Specifies that this should be stored as a Large Object (BLOB for byte[])
    @Column(columnDefinition="LONGBLOB") // Explicitly define column type for large images (optional but good practice for some DBs)
    private byte[] imageData;
    // ------------------

    // --- ADD MIME TYPE ---
    // Store the image type (e.g., "image/jpeg", "image/png") to serve it correctly
    private String imageType;
    // ---------------------

    private LocalDateTime postedAt;

    public Event() {
        // Automatically set the timestamp when a new event is created
        this.postedAt = LocalDateTime.now();
    }

    // --- Getters and Setters for all fields ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }
}


