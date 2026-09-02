package com.example.suggestion_system;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(origins = "http://localhost:5173")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = {"multipart/form-data"})
    public Suggestion create(@RequestParam("title") String title, @RequestParam("description") String description, @RequestParam(value = "file", required = false) MultipartFile file, Principal principal){
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(title);
        suggestion.setDescription(description);

        if(file != null && !file.isEmpty()){
            String fileName = fileStorageService.save(file);
            suggestion.setPresentationFileName(fileName);
        }
        return suggestionService.createSuggestion(suggestion, principal.getName());
    }

    @GetMapping("/my")
    public List<Suggestion> getMyIdeas(Principal principal){
        return suggestionService.getMySuggestions(principal.getName());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Suggestion> getAllForAdmin(){
        return suggestionService.getAllSuggestions();
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public void accept(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file, Principal principal){
        String implementationFileName = null;
        if(file != null && !file.isEmpty()){
            implementationFileName = fileStorageService.save(file);
        }
        suggestionService.acceptSuggestion(id, principal.getName(), implementationFileName);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public void reject(@PathVariable Long id){
        suggestionService.rejectSuggestion(id);
    }
}
