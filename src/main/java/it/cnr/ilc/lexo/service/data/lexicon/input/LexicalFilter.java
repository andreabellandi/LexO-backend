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
@ApiModel(description = "Input model representing lexical filter options")
public class LexicalFilter implements Data {

    @ApiModelProperty(value = "chars sequence to search", example = "a")
    private String text;
    @ApiModelProperty(value = "search type to perform (it cannot be empty)", example = "equals", allowableValues = "equals, startsWith, contains, endsWith")
    private String searchMode;
    @ApiModelProperty(value = "lexcial entry types (empty means all)", example = "word", allowableValues = "word, multi-word expression, affix")
    private String type;
    @ApiModelProperty(value = "part of speech (empty means all)", example = "verb")
    private String pos;
    @ApiModelProperty(value = "the type of form the serach is performed on (entry means lemma, empty means all)", allowableValues = "flexed, entry", example = "flexed")
    private String formType;
    @ApiModelProperty(value = "author (empty means all)", example = "fmurano")
    private String author;
    @ApiModelProperty(value = "language (empty for all languages)", example = "it")
    private String lang;
    @ApiModelProperty(value = "status (empty means all)", example = "working", allowableValues = "working, completed, reviewed")
    private String status;
    @ApiModelProperty(value = "result set offset", example = "0")
    private int offset;
    @ApiModelProperty(value = "result set limit", example = "500")
    private int limit;

    public LexicalFilter() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

}
