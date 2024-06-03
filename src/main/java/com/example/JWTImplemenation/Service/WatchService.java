package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.ImageUrl;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.IRespository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IImageService;
import com.example.JWTImplemenation.Service.IService.IWatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchService implements IWatchService {

    @Autowired
    private WatchRespository watchRepository;
    @Autowired
    private IImageService imageService;

    @Override
    public ResponseEntity<List<WatchDTO>> findAll() {
        List<Watch> watches = watchRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(watches));
    }

    @Override
    public ResponseEntity<WatchDTO> findById(Integer id) {
        Optional<Watch> watch = watchRepository.findById(id);
        return watch.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<WatchDTO> save(Watch watch) {
        Watch savedWatch = watchRepository.save(watch);
        return ResponseEntity.ok(convertToDTO(savedWatch));
    }

    @Override
    public ResponseEntity<WatchDTO> update(Integer id, Watch watch) {
        if (watchRepository.existsById(id)) {
            watch.setId(id);
            Watch updatedWatch = watchRepository.save(watch);
            return ResponseEntity.ok(convertToDTO(updatedWatch));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        if (watchRepository.existsById(id)) {
            watchRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<WatchDTO> addImageToWatch(Integer watchId, MultipartFile imageFile) {
        Optional<Watch> optionalWatch = watchRepository.findById(watchId);
        if (optionalWatch.isPresent()) {
            Watch watch = optionalWatch.get();
            String imageUrl = imageService.uploadImage(imageFile);
            if (imageUrl != null) {
                ImageUrl image = new ImageUrl();
                image.setImageUrl(imageUrl);
                image.setWatch(watch);
                if (watch.getImageUrl() == null) {
                    watch.setImageUrl(new ArrayList<>());
                }
                watch.getImageUrl().add(image);
                Watch updatedWatch = watchRepository.save(watch);
                return ResponseEntity.ok(convertToDTO(updatedWatch));
            }
        }
        return ResponseEntity.notFound().build();
    }


    private WatchDTO convertToDTO(Watch watch) {
        WatchDTO watchDTO = new WatchDTO();
        watchDTO.setId(watch.getId());
        watchDTO.setName(watch.getName());
        watchDTO.setBrand(watch.getBrand());
        watchDTO.setDescription(watch.getDescription());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setCreatedDate(watch.getCreatedDate());
        if (watch.getImageUrl() != null) {
            List<String> imageUrls = watch.getImageUrl()
                    .stream()
                    .map(ImageUrl::getImageUrl)
                    .collect(Collectors.toList());
            watchDTO.setImageUrl(imageUrls);
        }
        return watchDTO;
    }

    private List<WatchDTO> convertToDTOList(List<Watch> watches) {
        return watches.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
