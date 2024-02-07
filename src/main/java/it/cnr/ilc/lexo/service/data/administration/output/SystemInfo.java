/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.administration.output;

import it.cnr.ilc.lexo.service.data.Data;
import java.util.List;

/**
 *
 * @author andreabellandi
 */
public class SystemInfo implements Data {

    private String graphDBVersion;
    private String graphDBUrl;
    private String LexOVersion;
    private String storageSizeOnDisk;
    private String freeDiskSpace;
    private List<Repository> attachedRepositories;

    public enum repoType {
        LEXICON,
        USER,
        ONTOLOGY
    }

    public String getGraphDBVersion() {
        return graphDBVersion;
    }

    public void setGraphDBVersion(String graphDBVersion) {
        this.graphDBVersion = graphDBVersion;
    }

    public String getGraphDBUrl() {
        return graphDBUrl;
    }

    public void setGraphDBUrl(String graphDBUrl) {
        this.graphDBUrl = graphDBUrl;
    }

    public String getLexOVersion() {
        return LexOVersion;
    }

    public void setLexOVersion(String LexOVersion) {
        this.LexOVersion = LexOVersion;
    }

    public String getStorageSizeOnDisk() {
        return storageSizeOnDisk;
    }

    public void setStorageSizeOnDisk(String storageSizeOnDisk) {
        this.storageSizeOnDisk = storageSizeOnDisk;
    }

    public String getFreeDiskSpace() {
        return freeDiskSpace;
    }

    public void setFreeDiskSpace(String freeDiskSpace) {
        this.freeDiskSpace = freeDiskSpace;
    }

    public List<Repository> getAttachedRepositories() {
        return attachedRepositories;
    }

    public void setAttachedRepositories(List<Repository> attachedRepositories) {
        this.attachedRepositories = attachedRepositories;
    }

    public class Repository {

        private String name;
        private String type;
        private String namespace;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

}
