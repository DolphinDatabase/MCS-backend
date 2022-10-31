package com.cms.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Layer")
@Getter
@Setter
@NoArgsConstructor
public class Layer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lyr_id")
    private Long id;

    @Column(name = "lyr_x")
    private Long x;

    @Column(name = "lyr_y")
    private Long y;

    @Column(name = "lyr_color")
    private String color;

    @ManyToOne
    @JoinColumn(name = "map_id", nullable = true)
    private SolicitationMapping solicitationMapping;

}
