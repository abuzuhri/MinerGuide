package com.minergame.minerguide.utils.Login;

import android.content.Intent;

/**
 * Created by Tareq on 04/21/2015.
 */
public interface SocialNetwork
{
    boolean isLoggedIn();
    void Logout();
    void Login(OnLoginListener lsnr);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
