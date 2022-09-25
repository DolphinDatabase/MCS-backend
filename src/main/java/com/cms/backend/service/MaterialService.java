package com.cms.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.cms.backend.SummaryModel.MaterialSummaryModel;
import com.cms.backend.SummaryModel.ResponseSummaryModel;
import com.cms.backend.entity.Material;
import com.cms.backend.repository.MaterialRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/material")
public class MaterialService {
    
    @Autowired
    MaterialRepository mRepository;

    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(MaterialService.class);

    @GetMapping
    private ResponseEntity<ResponseSummaryModel> listMaterials(){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            List<MaterialSummaryModel> all = mRepository.findAll().stream().map(this::toMaterialSummaryModel).collect(Collectors.toList());
            res.setAll(200, true, "List All Materials", all);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSummaryModel> findMaterial(@PathVariable Long id){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            Material m = mRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            res.setAll(200, true, "Material "+id+" Found", toMaterialSummaryModel(m));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "Material "+id+" Not Found", null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseSummaryModel> createMaterial(@RequestBody Material Material){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            Material n = mRepository.save(Material);
            res.setAll(200, true, "New Material Created", toMaterialSummaryModel(n));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSummaryModel> updateMaterial(@PathVariable Long id, @RequestBody Material material){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            Material m = mRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            m.setModel(material.getModel());
            m.setQuantity(material.getQuantity());
            m.setInventory(material.getInventory());
            res.setAll(200, true, "Material "+id+" Updated", toMaterialSummaryModel(mRepository.save(m)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "Material "+id+" Not Found", null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSummaryModel> deleteMaterial(@PathVariable Long id){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            Material m = mRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            m.getSolicitations().forEach(s->{
                s.getMaterials().remove(m);
            });
            mRepository.delete(m);
            res.setAll(200, true, "Material "+id+" Deleted", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "Material "+id+" Not Found", null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    private MaterialSummaryModel toMaterialSummaryModel(Material material){
        return modelMapper.map(material,MaterialSummaryModel.class);
    }

}
