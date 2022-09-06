package com.cms.backend.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Problema")
@Getter
@Setter
@NoArgsConstructor
public class Problem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prb_id")
    private Long id;

    @Column(name = "prb_nome")
    private String name;

    @Column(name = "prb_descricao")
    private String description;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.REMOVE)
    private Set<Solution> solutions;

    @ManyToMany(mappedBy = "problems")
    private Set<Solicitation> solicitations;

}
