package it.cnr.ilc.lexo.hibernate.entity;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author andreabellandi
 */
@Entity
public class AccountEntity extends SuperEntity  {

    private AccountTypeEntity type;
    private String username;
    private String password;
    private String name;
    private String email;
    private Boolean enabled;
    private Map<String, String> settings;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public AccountTypeEntity getType() {
        return type;
    }

    public void setType(AccountTypeEntity type) {
        this.type = type;
    }

    @Column(nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false)
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

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @ElementCollection(fetch = FetchType.LAZY)
    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

}
