package Panel;

import User.User;

public class UserPanel {

    private User currentUser;
    private static UserPanel userPanel;

    public UserPanel() {
    }

    public static UserPanel getUserPanel() {
        if (userPanel == null)
            userPanel = new UserPanel();
        return userPanel;
    }

}
