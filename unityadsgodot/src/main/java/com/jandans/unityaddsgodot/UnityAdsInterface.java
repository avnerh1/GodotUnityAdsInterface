package com.jandans.unityaddsgodot;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnityAdsInterface extends GodotPlugin implements IUnityAdsLoadListener, IUnityAdsShowListener, IUnityAdsInitializationListener {
    private final String TAG = "UnityAdsInterface";

    private boolean is_ready = false;
    private boolean is_loaded = false;

    private SignalInfo UnityAdsReady = new SignalInfo("UnityAdsReady");
    private SignalInfo UnityAdsStart = new SignalInfo("UnityAdsStart");
    private SignalInfo UnityAdsLoad = new SignalInfo("UnityAdsLoad");
    private SignalInfo UnityAdsFinish = new SignalInfo("UnityAdsFinish", String.class, String.class);
    private SignalInfo UnityAdsError = new SignalInfo("UnityAdsError", String.class, String.class);
    private SignalInfo UnityAdsLoadError = new SignalInfo("UnityAdsLoadError", String.class, String.class);


    public UnityAdsInterface(Godot godot) {
        super(godot);
    }

    @androidx.annotation.NonNull
    @Override
    public String getPluginName() {
        return "UnityAdsGodot";
    }

    @NonNull
    @Override
    public List<String> getPluginMethods() {
        return new ArrayList<String>() {
            {
                add("initialise");
                add("loadAd");
                add("show");
                add("isReady");
                add("adIsLoaded");
            }
        };
    }

    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        return new HashSet<SignalInfo>() {
            {
                add(UnityAdsReady);
                add(UnityAdsStart);
                add(UnityAdsFinish);
                add(UnityAdsError);
                add(UnityAdsLoad);
                add(UnityAdsLoadError);
            }
        };
    }

    public void initialise(String appId, boolean testMode) {
        try {
            UnityAds.initialize(getActivity(), appId, testMode, this);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
    public void loadAd(String placementId) { //Get placementId from your Unity Monetization console
        UnityAds.load(placementId, this);
    }

    public boolean show(String placementId) { //Get placementId from your Unity Monetization console
        if (is_loaded) {
            try {
                UnityAds.show(getActivity(), placementId, this);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
                return false;
            }
            return true;
        } else {
            Log.i(TAG, "Ad not loaded");
            return false;
        }
    }
    public boolean adIsLoaded() {
        return is_loaded;
    }
    public boolean isReady() {
        return is_ready;
    }

    @Override
    public void onInitializationComplete() {
        is_ready = true;
        emitSignal(UnityAdsReady.getName());
    }
    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        emitSignal(UnityAdsError.getName(), error.toString(), message);
        Log.e(TAG, "Unity Ads initialization failed: " + message);
    }

    @Override
    public void onUnityAdsAdLoaded(String placementId) {
        is_loaded = true;
        emitSignal(UnityAdsLoad.getName());
    }
    @Override
    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
        emitSignal(UnityAdsLoadError.getName(), error.toString(), message);
        Log.e(TAG, placementId);
    }
    @Override
    public void onUnityAdsShowStart(String placementId) {
        is_loaded = false;
        emitSignal(UnityAdsStart.getName());
    }

    @Override
    public void onUnityAdsShowClick(String s) {

    }

    @Override
    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState ad_state) {
        int state = -1;
        if (ad_state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
            // Reward the user for watching the ad to completion
            state = 2;
        } else if (ad_state.equals(UnityAds.UnityAdsShowCompletionState.SKIPPED))  {
            // user skipped the ad.
            state = 1;
        } else {
            // Log an error.
            Log.e(TAG, placementId);
            state = 0;
        }
        emitSignal(UnityAdsFinish.getName(), placementId, String.format("%d", state));
    }

    @Override
    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
        emitSignal(UnityAdsError.getName(), error.toString(), message);
        Log.e(TAG, placementId);
    }



}
