package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.AppraisalDTO;
import com.example.JWTImplemenation.Entities.Appraisal;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAppraisalService {
    ResponseEntity<List<AppraisalDTO>> findAll();
    ResponseEntity<AppraisalDTO> findById(Integer id);
    ResponseEntity<AppraisalDTO> save(Integer watchId, Appraisal appraisal);
    ResponseEntity<AppraisalDTO> update(Integer id, Appraisal appraisal);
    ResponseEntity<Void> deleteById(Integer id);
}
