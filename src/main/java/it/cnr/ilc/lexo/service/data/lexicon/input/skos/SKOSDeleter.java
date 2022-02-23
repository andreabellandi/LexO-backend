/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.skos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing a skos deleter")
public class SKOSDeleter implements Data {

    @ApiModelProperty(value = "skos property", allowEmptyValue = false,
            allowableValues = "see skos properties at https://www.w3.org/TR/2009/REC-skos-reference-20090818/")
    private String relation;
    @ApiModelProperty(value = "the subject IRI of the relation", allowEmptyValue = false)
    private String source;
    @ApiModelProperty(value = "the object IRI of the relation", allowEmptyValue = false)
    private String target;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
