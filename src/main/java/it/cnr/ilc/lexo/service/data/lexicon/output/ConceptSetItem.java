 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import it.cnr.ilc.lexo.service.data.lexicon.output.skos.SKOSClass;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a concept set item")
public class ConceptSetItem extends SKOSClass implements Data {

    @ApiModelProperty(value = "IRI")
    private String conceptSet;
    @ApiModelProperty(value = "short IRI")
    private String conceptSetInstanceName;

    public String getConceptSet() {
        return conceptSet;
    }

    public void setConceptSet(String conceptSet) {
        this.conceptSet = conceptSet;
    }

    public String getConceptSetInstanceName() {
        return conceptSetInstanceName;
    }

    public void setConceptSetInstanceName(String conceptSetInstanceName) {
        this.conceptSetInstanceName = conceptSetInstanceName;
    }

}
