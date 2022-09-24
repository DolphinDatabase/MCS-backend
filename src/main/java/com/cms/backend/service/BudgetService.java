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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.backend.SummaryModel.BudgetSummaryModel;
import com.cms.backend.SummaryModel.ResponseSummaryModel;
import com.cms.backend.entity.Budget;
import com.cms.backend.repository.BudgetRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/budget")
public class BudgetService {

    @Autowired
    BudgetRepository bRepository;
 
    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(SolutionService.class);

    @GetMapping
    public ResponseEntity<ResponseSummaryModel> listBudget(){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            List<BudgetSummaryModel> all = bRepository.findAll().stream().map(this::toBudgetSummaryModel).collect(Collectors.toList());
            res.setAll(200, true, "List All Budget", all);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseSummaryModel> createBudget(@RequestBody Budget budget){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            res.setAll(200, true, "New Budget Created", toBudgetSummaryModel(bRepository.save(budget)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }


    private BudgetSummaryModel toBudgetSummaryModel(Budget budget){
        return modelMapper.map(budget,BudgetSummaryModel.class);
    }

}