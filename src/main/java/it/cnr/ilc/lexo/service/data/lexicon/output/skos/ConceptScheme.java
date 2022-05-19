/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output.skos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a Concept Scheme")
public class ConceptScheme extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String conceptScheme;
    @ApiModelProperty(value = "short IRI")
    private String conceptSchemeInstanceName;

    public String getConceptScheme() {
        return conceptScheme;
    }

    public void setConceptScheme(String conceptScheme) {
        this.conceptScheme = conceptScheme;
    }

    public String getConceptSchemeInstanceName() {
        return conceptSchemeInstanceName;
    }

    public void setConceptSchemeInstanceName(String conceptSchemeInstanceName) {
        this.conceptSchemeInstanceName = conceptSchemeInstanceName;
    }

}
