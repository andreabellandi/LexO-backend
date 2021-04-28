package it.cnr.ilc.lexo.service.data;

/**
 *
 * @author andreabellandi
 */
public class PasswordData implements Data {

    private String currentPassword;
    private String newPassword;

    public PasswordData() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
