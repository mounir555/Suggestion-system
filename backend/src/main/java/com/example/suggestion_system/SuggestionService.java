package com.example.suggestion_system;

import java.time.LocalDateTime;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService {
    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired 
    private UserRepository userRepository;

    public Suggestion createSuggestion(Suggestion suggestion, String username){
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

        suggestion.setUser(user);
        suggestion.setStatus("PENDING");
        suggestion.setCreatedAt(LocalDateTime.now());
        return suggestionRepository.save(suggestion);
    }

    public List<Suggestion> getAllSuggestions(){
        return suggestionRepository.findAll();
    }

    public List<Suggestion> getMySuggestions(String username){
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
        return suggestionRepository.findByUser(user);
    }

    public void acceptSuggestion(Long suggestionId, String adminUsername, String fileName){
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
        .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        User admin = userRepository.findByUsername(adminUsername)
        .orElseThrow(() -> new RuntimeException("Admin not found"));

        if(!"ACCEPTED".equals(suggestion.getStatus())){
        suggestion.setStatus("ACCEPTED");
        suggestion.setImplementationFileName(fileName);
        suggestion.setProcessedAt(LocalDateTime.now());
        suggestion.setProcessedBy(admin);

        User author = suggestion.getUser();
        author.setPoints(author.getPoints() + 10);
        userRepository.save(author);
        suggestionRepository.save(suggestion);
        }
    }

    public void rejectSuggestion(Long suggestionId){
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
        .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        suggestion.setStatus("REJECTED");
        suggestionRepository.save(suggestion);
    }
}
