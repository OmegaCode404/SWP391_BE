package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Service.IService.IWatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/watch")
@RequiredArgsConstructor
public class WatchController {

    @Autowired
    private IWatchService iWatchService;

    @GetMapping
    public ResponseEntity<List<WatchDTO>> getAllWatches() {
        return iWatchService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchDTO> getWatchById(@PathVariable Integer id) {
        return iWatchService.findById(id);
    }

    @PostMapping
    public ResponseEntity<WatchDTO> createWatch(@RequestBody Watch watch) {
        return iWatchService.save(watch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchDTO> updateWatch(@PathVariable Integer id, @RequestBody Watch watch) {
        return iWatchService.update(id, watch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatch(@PathVariable Integer id) {
        return iWatchService.deleteById(id);
    }

    @PostMapping("/{watchId}/images")
    public ResponseEntity<WatchDTO> addImageToWatch(@PathVariable Integer watchId, @RequestParam("imageFile") MultipartFile imageFile) {
        return iWatchService.addImageToWatch(watchId, imageFile);
    }
}
