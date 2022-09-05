package com.cms.backend.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.cms.backend.SummaryModel.ResponseSummaryModel;
import com.cms.backend.SummaryModel.UserSummaryModel;
import com.cms.backend.entity.Role;
import com.cms.backend.entity.User;
import com.cms.backend.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserService {
    
    @Autowired
    private UserRepository uRepository;

    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @GetMapping
    public ResponseEntity<ResponseSummaryModel> listUsers(){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            List<UserSummaryModel> all = uRepository.findAll().stream().map(this::toUserSummaryModel).collect(Collectors.toList());
            res.setAll(200, true, "List All Users", all);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/:id")
    public ResponseEntity<ResponseSummaryModel> findUser(@PathVariable Long id){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            User user = uRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            res.setAll(200, true, "User "+id+" Found", toUserSummaryModel(user));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "User "+id+" Not Found", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseSummaryModel> createUser(@RequestBody User user){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            if(filterRoles(user.getRoles())!=null && user.getRoles().size()>1){
                res.setAll(401, false, "Unauthorized roles", null);
                logger.warn(res.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }
            res.setAll(200, true, "New User Created", toUserSummaryModel(uRepository.save(user)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseSummaryModel> updateUser(@PathVariable Long id, @RequestBody User user){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            User u = uRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            u.setName(user.getName());
            u.setEmail(user.getEmail());
            u.setPassword(user.getPassword());
            res.setAll(200, true, "User "+id+" Updated", toUserSummaryModel(uRepository.save(u)));
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "User "+id+" Not Found", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseSummaryModel> deleteUser(@PathVariable Long id){
        ResponseSummaryModel res = new ResponseSummaryModel();
        try{
            User u = uRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            uRepository.delete(u);
            res.setAll(200, true, "User "+id+" Deleted",null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }catch(ResponseStatusException err){
            res.setAll(404, false, "User "+id+" Not Found", null);
            logger.info(res.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        catch(Exception err){
            res.setAll(500, false, err.getMessage(), null);
            logger.error(res.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    private UserSummaryModel toUserSummaryModel(User user){
        return modelMapper.map(user,UserSummaryModel.class);
    }

    private Role filterRoles(Set<Role> roles){
        return roles.stream().filter(role->role.getNivel().equals("ROLE_CLT")).findAny().orElse(null);
    }

}
