package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.AppraisalDTO;
import com.example.JWTImplemenation.Entities.Appraisal;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.AppraisalRepository;
import com.example.JWTImplemenation.Repository.IRespository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IAppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppraisalService implements IAppraisalService {

    @Autowired
    private AppraisalRepository appraisalRepository;

    @Autowired
    private WatchRespository watchRepository;

    @Override
    public ResponseEntity<List<AppraisalDTO>> findAll() {
        List<Appraisal> appraisals = appraisalRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(appraisals));
    }

    @Override
    public ResponseEntity<AppraisalDTO> findById(Integer id) {
        Optional<Appraisal> appraisal = appraisalRepository.findById(id);
        return appraisal.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AppraisalDTO> save(Integer watchId, Appraisal appraisal) {
        Optional<Watch> optionalWatch = watchRepository.findById(watchId);
        if (optionalWatch.isPresent()) {
            Watch watch = optionalWatch.get();
            appraisal.setWatch(watch);
            Appraisal savedAppraisal = appraisalRepository.save(appraisal);

            // Update the price of the associated watch
            watch.setPrice(savedAppraisal.getValue());
            watchRepository.save(watch);

            return ResponseEntity.ok(convertToDTO(savedAppraisal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AppraisalDTO> update(Integer id, Appraisal appraisal) {
        if (appraisalRepository.existsById(id)) {
            appraisal.setId(id);
            Appraisal updatedAppraisal = appraisalRepository.save(appraisal);
            return ResponseEntity.ok(convertToDTO(updatedAppraisal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        if (appraisalRepository.existsById(id)) {
            appraisalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private AppraisalDTO convertToDTO(Appraisal appraisal) {
        return AppraisalDTO.builder()
                .id(appraisal.getId())
                .comments(appraisal.getComments())
                .value(appraisal.getValue())
                .yearOfProduction(appraisal.getYearOfProduction())
                .material(appraisal.getMaterial())
                .thickness(appraisal.getThickness())
                .dial(appraisal.getDial())
                .movement(appraisal.getMovement())
                .crystal(appraisal.getCrystal())
                .bracket(appraisal.getBracket())
                .buckle(appraisal.getBuckle())
                .build();
    }

    private List<AppraisalDTO> convertToDTOList(List<Appraisal> appraisals) {
        return appraisals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
