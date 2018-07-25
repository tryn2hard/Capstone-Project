package com.example.robot.pockettally;

public class Winner {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    private String name;
    private int avatarId;

    public Winner(){}

    public Winner(String name, int avatarId){
        this.name = name;
        this.avatarId = avatarId;
    }
}
