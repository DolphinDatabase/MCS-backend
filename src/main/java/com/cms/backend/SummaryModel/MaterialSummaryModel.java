package com.cms.backend.SummaryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaterialSummaryModel {
    private Long num;
    private String modelo;
    private Long quantity;
    private Long inventory;
}
