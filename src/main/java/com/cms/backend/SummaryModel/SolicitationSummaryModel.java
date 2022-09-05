package com.cms.backend.SummaryModel;

import java.util.Date;

import com.cms.backend.util.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SolicitationSummaryModel {
    
    private Long id;
    private String name;
    private Date date;
    private Status status;

}
