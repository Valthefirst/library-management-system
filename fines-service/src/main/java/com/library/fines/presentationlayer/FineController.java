package com.library.fines.presentationlayer;

import com.library.fines.businesslayer.FineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FineResponseModel>> getAllFines() {
        return ResponseEntity.ok().body(fineService.getAllFines());
    }

    @GetMapping(value = "{fineId}", produces = "application/json")
    public ResponseEntity<FineResponseModel> getFine(@PathVariable String fineId) {
        return ResponseEntity.ok().body(fineService.getFine(fineId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<FineResponseModel> addFine(@RequestBody FineRequestModel fineRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fineService.addFine(fineRequestModel));
    }

    @PutMapping(consumes = "application/json", value = "{fineId}", produces = "application/json")
    public ResponseEntity<FineResponseModel> updateFine(@RequestBody FineRequestModel fineRequestModel,
                                                       @PathVariable String fineId) {
        return ResponseEntity.ok().body(fineService.updateFine(fineRequestModel, fineId));
    }

    @DeleteMapping("{fineId}")
    public ResponseEntity<Void> deleteFine(@PathVariable String fineId) {
        fineService.deleteFine(fineId);
        return ResponseEntity.noContent().build();
    }
}
