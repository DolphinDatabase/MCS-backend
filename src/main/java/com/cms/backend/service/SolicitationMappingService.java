package com.cms.backend.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cms.backend.SummaryModel.ResponseSummaryModel;
import com.cms.backend.entity.SolicitationMapping;
import com.cms.backend.repository.SolicitationMappingRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/SolicitationMapping")
public class SolicitationMappingService {
    
    @Autowired
    SolicitationMappingRepository smRepository;
 
    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(SolicitationService.class);

    @PostMapping
    @PreAuthorize("hasRole('SUP')")
    public ResponseEntity<ResponseSummaryModel> addProblem(@RequestBody SolicitationMapping sm){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            res.setAll(200, true, "SolicitationMapping "+sm.getMapping().getId()+" adicionado", toSolicitationMappingRepository(smRepository.save(sm)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUP')")
    public ResponseEntity<ResponseSummaryModel> deleteAddedProblem(@PathVariable Long id){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            SolicitationMapping sm = smRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            sm.setMapping(null);
            sm.setSolicitation(null);
            smRepository.delete(sm);
            res.setAll(200, true, "SolicitationMapping "+id+" deletado", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "SolicitationMapping "+id+" não encontrado", null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    private SolicitationMappingRepository toSolicitationMappingRepository(SolicitationMapping sm){
        return modelMapper.map(sm,SolicitationMappingRepository.class);
    }

}
