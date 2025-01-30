package Panel;

import User.User;

public class Entry {
    private int key;
    private User value;

    public Entry(int key, User value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    public User getValue() {
        return value;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setValue(User value) {
        this.value = value;
    }
}

