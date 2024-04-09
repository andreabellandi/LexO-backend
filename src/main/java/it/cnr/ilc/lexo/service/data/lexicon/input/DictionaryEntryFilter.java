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
@ApiModel(description = "Input model representing dictionary entry filter options")
public class DictionaryEntryFilter implements Data {

    @ApiModelProperty(value = "chars sequence to search", example = "disso", allowEmptyValue = true)
    private String text;
    @ApiModelProperty(value = "search type to perform (it cannot be empty)", example = "startsWith", allowableValues = "equals, startsWith, contains, endsWith", allowEmptyValue = false)
    private String searchMode;
    @ApiModelProperty(value = "part of speech (empty means all)", example = "noun", allowEmptyValue = true)
    private String pos;
//    @ApiModelProperty(value = "the type of form the search is performed on (entry refers to the entry label only, and flexed means all the forms)", allowableValues = "flexed, entry", example = "flexed", allowEmptyValue = false)
//    private String formType;
    @ApiModelProperty(value = "author (empty means all)", example = "user1", allowEmptyValue = true)
    private String author;
    @ApiModelProperty(value = "language (empty for all languages)", example = "it", allowEmptyValue = true)
    private String lang;
    @ApiModelProperty(value = "status (empty means all)", example = "reviewed", allowableValues = "working, completed, reviewed", allowEmptyValue = true)
    private String status;
    @ApiModelProperty(value = "result set offset", example = "0", allowEmptyValue = false)
    private int offset;
    @ApiModelProperty(value = "result set limit", example = "500", allowEmptyValue = false)
    private int limit;

    public DictionaryEntryFilter() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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


}
