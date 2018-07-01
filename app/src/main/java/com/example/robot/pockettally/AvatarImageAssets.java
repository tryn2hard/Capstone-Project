package com.example.robot.pockettally;

import java.util.ArrayList;
import java.util.List;

public final class AvatarImageAssets {

    private static final List<Integer> avatars = new ArrayList<Integer>() {{
        add(R.mipmap.man);
        add(R.mipmap.man_1);
        add(R.mipmap.man_2);
        add(R.mipmap.man_3);
        add(R.mipmap.man_4);
        add(R.mipmap.man_5);
        add(R.mipmap.man_6);
        add(R.mipmap.woman);
        add(R.mipmap.woman_1);
    }};

    public static List<Integer> getAvatars(){ return avatars; }
}
