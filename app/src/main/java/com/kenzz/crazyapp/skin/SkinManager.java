package com.kenzz.crazyapp.skin;

import android.app.Activity;
import android.content.Context;

import com.kenzz.crazyapp.skin.attr.SkinView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangdefa on 28/08/2017.
 *  换肤管理
 */

public class SkinManager {

    private Context mContext;
    private SkinResource mSkinResource;
    private Map<Activity,List<SkinView>> mskinViews=new HashMap<>();

    private SkinManager(){}
    private static SkinManager sSkinManager;
    static {
        sSkinManager=new SkinManager();
    }

    public static SkinManager getInstance(){
        return sSkinManager;
    }

    public void init(Context context){
        mContext=context.getApplicationContext();
    }

    public void loadSkin(String skinPath){
      mSkinResource=new SkinResource(mContext,skinPath);

        Set<Map.Entry<Activity, List<SkinView>>> entries = mskinViews.entrySet();
        for (Map.Entry<Activity, List<SkinView>> entry : entries) {
            List<SkinView> skinViews = entry.getValue();
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
    }

    public void restoreSkin(){

    }

    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    public Map<Activity, List<SkinView>> getMskinViews() {
        return mskinViews;
    }

    public void putMskinViews(Activity activity,List<SkinView> skinViews){
        mskinViews.put(activity,skinViews);
    }
}
