package com.example.robot.pockettally;

import java.util.ArrayList;
import java.util.List;

public class AvatarImageAssets {

    private static final List<Integer> avatars = new ArrayList<Integer>() {{
        add(R.drawable.man);
        add(R.drawable.man_1);
        add(R.drawable.man_2);
        add(R.drawable.man_3);
        add(R.drawable.man_4);
        add(R.drawable.man_5);
        add(R.drawable.man_6);
        add(R.drawable.woman);
        add(R.drawable.woman_1);
    }};

    public static List<Integer> getAvatars(){ return avatars; }
}
