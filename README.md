# Unity ads for godot using new export template system

Current version of the `unityadsgodot-release.aar` is available for download under [Releases](https://github.com/avnerh1/GodotUnityAdsInterface/releases)

## Requirements

The new export templates for android was introduced in Godot 3.5.1

## Setup

- In Godot open up the Android export template settings by going to `Project -> Export`. 
  - Select `Android` and enable costom build. 
  - Instal custom android build template. 
- Download UnityAds Android library from [here](https://github.com/Unity-Technologies/unity-ads-android/releases)
- Open up the Godots projects `android` folder and create a folder called `plugins` if it isn't there. 
- Place the `unityadsgodot-release.aar` inside the freshly created plugins folder along with `unity-ads.aar` and `UnityAdsGodot.gdap`. 
- Last thing is to enable this plugin under `Project -> Export -> Android -> Plugins` and check the checkbox besides `Unity Ads Godot`

## Usage

Usage example is availible in `AdsExampleProject` in this repository but basically the code below sums it up

```
var addsEngine = null

# Called when the node enters the scene tree for the first time.
func _ready():
	if Engine.has_singleton("UnityAdsGodot"):
		addsEngine = Engine.get_singleton("UnityAdsGodot")
		addsEngine.connect("UnityAdsReady", self, "_on_adsReady")
		addsEngine.connect("UnityAdsFinish", self, "_on_adsFinished")
		addsEngine.connect("UnityAdsError", self, "_on_adsError")
		addsEngine.connect("UnityBannerLoaded", self, "_on_bannerLoaded")
		addsEngine.connect("UnityBannerError", self, "_on_bannerError")
		addsEngine.initialise("1687685", false) # placement id (from your Unity console) and TestMode enabled
	else:
		print("Couldn't find HelloSignals singleton")

func _on_adsReady():
	print("video adds should be ready.")
	
func _on_adsFinished(placement, reason):
	reason = int(reason)
	if reason == 2:
		print("Completed")
	elif reason == 1:
		print("User skiped ad")
	else:
		print("Something went wrong")

func _on_adsError(reasonString):
	print(reasonString)
	
func _on_bannerLoaded():
	print("Banner loaded")
	
func _on_bannerError(reasonString):
	print(reasonString)

func _on_VideoAd_pressed():
	if addsEngine != null:
		addsEngine.loadAd("video")
		while !addsEngine.isReady("video"):
			pass # There should be another way to do this!
		
		addsEngine.show("video")

func _on_RewardedVideo_pressed():
	if addsEngine != null:
		addsEngine.loadAd("rewardedVideo")
		while !addsEngine.isReady("rewardedVideo"):
			pass # There should be another way to do this!
		
		addsEngine.show("rewardedVideo")


func _on_BannerAd_pressed():
	if addsEngine != null:
		addsEngine.showBanner("banners")
```

## Known issues

- Banners are not working?
