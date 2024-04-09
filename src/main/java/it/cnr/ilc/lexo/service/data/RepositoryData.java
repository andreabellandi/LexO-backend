/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model for creating and configuring a new repository")
public class RepositoryData implements Data {

//    @ApiModelProperty(value = "repository ID", allowEmptyValue = false)
//    private String repoID;
    @ApiModelProperty(value = "repository description", allowEmptyValue = true)
    private String labelID;
    @ApiModelProperty(value = "specifies the default namespace for the main persistence file.", allowEmptyValue = true)
    private String baseUrl;
    @ApiModelProperty(value = "Sets of axiomatic triples, consistency checks and entailment rules, which determine the applied semantics.",
            allowEmptyValue = true, allowableValues = "rdfs, owl-horst, owl-max, and owl2-rl, rdfs-optimized, owl-horst-optimized, owl-max-optimized, owl2-rl-optimized")
    private String ruleset;

//    public String getRepoID() {
//        return repoID;
//    }
//
//    public void setRepoID(String repoID) {
//        this.repoID = repoID;
//    }

    public String getLabelID() {
        return labelID;
    }

    public void setLabelID(String labelID) {
        this.labelID = labelID;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRuleset() {
        return ruleset;
    }

    public void setRuleset(String ruleset) {
        this.ruleset = ruleset;
    }

}
