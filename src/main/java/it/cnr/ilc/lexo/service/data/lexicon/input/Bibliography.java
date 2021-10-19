/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing the bibliographic data")
public class Bibliography implements Data {

    @ApiModelProperty(value = "bibliographic item ID", allowEmptyValue = false)
    private String id;
    @ApiModelProperty(value = "title of the bibliographic item", allowEmptyValue = true)
    private String title;
    @ApiModelProperty(value = "authors of the bibliographic item", allowEmptyValue = true)
    private String author;
    @ApiModelProperty(value = "publication date of the bibliographic item", allowEmptyValue = true)
    private String date;
    @ApiModelProperty(value = "web page of the bibliographic item", allowEmptyValue = true)
    private String url;
    @ApiModelProperty(value = "external link to the document", allowEmptyValue = true)
    private String seeAlsoLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeeAlsoLink() {
        return seeAlsoLink;
    }

    public void setSeeAlsoLink(String seeAlsoLink) {
        this.seeAlsoLink = seeAlsoLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
