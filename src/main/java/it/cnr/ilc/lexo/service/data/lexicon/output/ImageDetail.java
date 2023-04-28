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

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Output model representing the detais of an image")
public class ImageDetail extends Entity implements Data {

    @ApiModelProperty(value = "image IRI")
    private String id;
    @ApiModelProperty(value = "absolute url of the image")
    private String url;
    @ApiModelProperty(value = "title of the image")
    private String title;
    @ApiModelProperty(value = "description of the image")
    private String description;
    @ApiModelProperty(value = "uploader of the image")
    private String publisher;
    @ApiModelProperty(value = "source for the image")
    private String source;
    @ApiModelProperty(value = "rights about the image")
    private String rights;
    @ApiModelProperty(value = "document format")
    private String format;
    @ApiModelProperty(value = "image extension type")
    private String type;
    @ApiModelProperty(value = "IRI of the entity associated to the image")
    private String lexicalEntityIRI;
    @ApiModelProperty(value = "image note", example = "textual content ...")
    private String note;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLexicalEntityIRI() {
        return lexicalEntityIRI;
    }

    public void setLexicalEntityIRI(String lexicalEntityIRI) {
        this.lexicalEntityIRI = lexicalEntityIRI;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
