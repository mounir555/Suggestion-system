package com.example.suggestion_system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUser(User user);
    List<Suggestion> findByStatus(String status);
}
