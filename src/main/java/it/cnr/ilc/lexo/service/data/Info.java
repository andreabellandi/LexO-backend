package it.cnr.ilc.lexo.service.data;

import java.util.Date;

/**
 *
 * @author andreabellandi
 */
public class Info implements Data {

    private String name;
    private String version;
    private Date serverTime;
    private String javaVersion;

    public Info() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

}
