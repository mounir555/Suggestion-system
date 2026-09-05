package com.example.suggestion_system;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "suggestions")
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private String status = "PENDING";

    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "processed_by_id")
    private User processedBy;

    private String presentationFileName;
    private String implementationFileName;

    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public User getProcessedBy() { return processedBy; }
    public void setProcessedBy(User processedBy) { this.processedBy = processedBy; }

    public String getPresentationFileName() { return presentationFileName; }
    public void setPresentationFileName(String presentationFileName) { this.presentationFileName = presentationFileName; }

    public String getImplementationFileName() { return implementationFileName; }
    public void setImplementationFileName(String implementationFileName) { this.implementationFileName = implementationFileName; }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Suggestion(){}

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
