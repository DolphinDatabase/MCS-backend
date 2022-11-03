package com.cms.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cms.backend.SummaryModel.MappingSummaryModel;
import com.cms.backend.SummaryModel.ResponseSummaryModel;
import com.cms.backend.entity.Mapping;
import com.cms.backend.repository.MappingRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/mapping")
public class Mappingservice {
    
    @Autowired
    MappingRepository mRepository;

    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(SolutionService.class);

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseSummaryModel> listMappings(){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            List<MappingSummaryModel> all = mRepository.findAll().stream().map(this::toMappingSummaryModel).collect(Collectors.toList());
            res.setAll(200, true, "Todos os mapeamentos listados", all);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('SUP')")
    public ResponseEntity<ResponseSummaryModel> createSolution(@RequestBody Mapping mapping){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            res.setAll(200, true, "Nova Mapeamento criado", toMappingSummaryModel(mRepository.save(mapping)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUP')")
    public ResponseEntity<ResponseSummaryModel> updateSolution(@PathVariable Long id, @RequestBody Mapping mapping){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            Mapping m = mRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            m.setName(mapping.getName());
            m.setLink(mapping.getLink());
            res.setAll(200, true, "Mapeamento "+id+" atualizado", toMappingSummaryModel(mRepository.save(m)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "Mapeamento "+id+" não encontrado", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUP')")
    public ResponseEntity<ResponseSummaryModel> deleteSolution(@PathVariable Long id){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            Mapping m = mRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            mRepository.delete(m);
            res.setAll(200, true, "Mapeamento "+id+" deletado",null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "Mapeamento "+id+" não encontrado", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    private MappingSummaryModel toMappingSummaryModel(Mapping mapping){
        return modelMapper.map(mapping,MappingSummaryModel.class);
    }

}
