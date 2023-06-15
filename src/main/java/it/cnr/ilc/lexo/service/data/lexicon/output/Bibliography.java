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
@ApiModel(description = "Output model representing the bibliography")
public class Bibliography implements Data {

    @ApiModelProperty(value = "title")
    private String title;
    @ApiModelProperty(value = "author")
    private String author;
    @ApiModelProperty(value = "date")
    private String date;
    @ApiModelProperty(value = "id")
    private String publisher;
    @ApiModelProperty(value = "the number of lexical entities referenced by the bibliography record")
    private int references;

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getReferences() {
        return references;
    }

    public void setReferences(int references) {
        this.references = references;
    }

}
