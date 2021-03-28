package it.cnr.ilc.lexo.service.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

/**
 *
 * @author andreabellandi
 */
public class Account implements Data {

    private Long id;
    private String type;
    private String username;
    private String password;
    private String name;
    private String email;
    private Boolean enabled;
    private Map<String, String> settings;

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

}
