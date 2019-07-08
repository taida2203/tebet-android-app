package com.tebet.mojual.common.eventbus;

import androidx.fragment.app.Fragment;

/**
 * Created by kal on 6/10/2017.
 */

public class OnReplaceFragmentEvent {
    private static Fragment attachFragment;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getAttachFragment() {
        return attachFragment;
    }

    public void setAttachFragment(Fragment attachFragment) {
        OnReplaceFragmentEvent.attachFragment = attachFragment;
    }
}
