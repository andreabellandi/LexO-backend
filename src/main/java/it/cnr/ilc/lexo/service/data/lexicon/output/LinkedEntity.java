/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing label and IRI of a linked entity")
public class LinkedEntity {
    
    @ApiModelProperty(value = "lexical entity IRI")
    private String lexicalEntity;
    @ApiModelProperty(value = "lexical entity shor IRI")
    private String lexicalEntityInstanceName;
    @ApiModelProperty(value = "lexical entity label")
    private String label;
    @ApiModelProperty(value = "lexical entity type")
    private String lexicalType;
    @ApiModelProperty(value = "the type of linked the lexical entity", allowableValues = "internal, external")
    private String entityType;
    
    
}
