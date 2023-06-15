/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import it.cnr.ilc.lexo.service.data.output.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing lexico semantic relations according to the vartrans module")
public class VarTransData implements Data {

    @ApiModelProperty(value = "relations")
    private List<LinkedEntity> directRelations;
    @ApiModelProperty(value = "reificated relations")
    private List<ReifiedRelation> indirectRelations;

    public List<LinkedEntity> getDirectRelations() {
        return directRelations;
    }

    public void setDirectRelations(List<LinkedEntity> directRelations) {
        this.directRelations = directRelations;
    }

    public List<ReifiedRelation> getIndirectRelations() {
        return indirectRelations;
    }

    public void setIndirectRelations(List<ReifiedRelation> indirectRelations) {
        this.indirectRelations = indirectRelations;
    }

}
