/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing a zotero bibliography record")
public class BibliographicItem implements Data {

    @ApiModelProperty(value = "bibliographic item ID")
    private String id;
    @ApiModelProperty(value = "internal bibliographic IRI")
    private String bibliography;
    @ApiModelProperty(value = "internal bibliographic Ishort RI")
    private String bibliographyInstanceName;
    @ApiModelProperty(value = "title of the bibliographic item")
    private String title;
    @ApiModelProperty(value = "authors of the bibliographic item")
    private String author;
    @ApiModelProperty(value = "publication date of the bibliographic item")
    private String date;
    @ApiModelProperty(value = "web page of the bibliographic item")
    private String url;
    @ApiModelProperty(value = "external link to the document")
    private String seeAlsoLink;
    @ApiModelProperty(value = "external link to the document", notes = "series of page number followed by line numbers range", example = "4[12-34]; 9[7-21]")
    private String textualReference;
    @ApiModelProperty(value = "bibliographic link creator")
    private String creator;
    @ApiModelProperty(value = "bibliographic link note")
    private String note;
    @ApiModelProperty(value = "bibliographic link creation date")
    private String creationDate;
    @ApiModelProperty(value = "bibliographic link last update")
    private String lastUpdate;

    public String getBibliography() {
        return bibliography;
    }

    public void setBibliography(String bibliography) {
        this.bibliography = bibliography;
    }

    public String getBibliographyInstanceName() {
        return bibliographyInstanceName;
    }

    public void setBibliographyInstanceName(String bibliographyInstanceName) {
        this.bibliographyInstanceName = bibliographyInstanceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTextualReference() {
        return textualReference;
    }

    public void setTextualReference(String textualReference) {
        this.textualReference = textualReference;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
