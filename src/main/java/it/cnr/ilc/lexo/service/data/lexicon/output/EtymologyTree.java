/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the etymology tree of a lexical entry")
public class EtymologyTree implements Data {

    @ApiModelProperty(value = "etymology data")
    private Etymology etymology;
    @ApiModelProperty(value = "etymological links of the etymology")
    private List<EtymologicalLink> etyLinks;

    public Etymology getEtymology() {
        return etymology;
    }

    public void setEtymology(Etymology etymology) {
        this.etymology = etymology;
    }

    public List<EtymologicalLink> getEtyLinks() {
        return etyLinks;
    }

    public void setEtyLinks(List<EtymologicalLink> etyLinks) {
        this.etyLinks = etyLinks;
    }

}
