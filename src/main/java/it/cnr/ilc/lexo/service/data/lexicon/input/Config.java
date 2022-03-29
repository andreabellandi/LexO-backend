/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing the configuration parameters")
public class Config {

    @ApiModelProperty(value = "Keycloack endpoint")
    private String userEndpoint;
    @ApiModelProperty(value = "Zotero endpoint")
    private String biblioEndpoint;
    @ApiModelProperty(value = "GraphDB endpoint")
    private String repoEndpoint;

    public String getUserEndpoint() {
        return userEndpoint;
    }

    public void setUserEndpoint(String userEndpoint) {
        this.userEndpoint = userEndpoint;
    }

    public String getBiblioEndpoint() {
        return biblioEndpoint;
    }

    public void setBiblioEndpoint(String biblioEndpoint) {
        this.biblioEndpoint = biblioEndpoint;
    }

    public String getRepoEndpoint() {
        return repoEndpoint;
    }

    public void setRepoEndpoint(String repoEndpoint) {
        this.repoEndpoint = repoEndpoint;
    }

}
