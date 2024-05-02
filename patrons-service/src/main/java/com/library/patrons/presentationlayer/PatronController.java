package com.library.patrons.presentationlayer;

import com.library.patrons.businesslayer.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/patrons")
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<PatronResponseModel>> getAllPatrons() {
        return ResponseEntity.ok().body(patronService.getAllPatrons());
    }

    @GetMapping(value = "{patronId}", produces = "application/json")
    public ResponseEntity<PatronResponseModel> getPatron(@PathVariable String patronId) {
        return ResponseEntity.ok().body(patronService.getPatron(patronId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<PatronResponseModel> addPatron(@RequestBody PatronRequestModel patronRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patronService.addPatron(patronRequestModel));
    }

    @PutMapping(consumes = "application/json", value = "{patronId}", produces = "application/json")
    public ResponseEntity<PatronResponseModel> updatePatron(@PathVariable String patronId,
                                                            @RequestBody PatronRequestModel patronRequestModel) {
        return ResponseEntity.ok().body(patronService.updatePatron(patronRequestModel, patronId));
    }

    @DeleteMapping("{patronId}")
    public ResponseEntity<Void> removePatron(@PathVariable String patronId) {
        patronService.removePatron(patronId);
        return ResponseEntity.noContent().build();
    }
}
