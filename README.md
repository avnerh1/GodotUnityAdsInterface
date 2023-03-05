# Unity ads for Godot Engine 3.5.x

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
		unityads = Engine.get_singleton("UnityAdsGodot")
		unityads.connect("UnityAdsReady", self, "_on_UnityAdsReady")
		unityads.connect("UnityAdsStart", self, "_on_UnityAdsStart")
		unityads.connect("UnityAdsFinish", self, "_on_UnityAdsFinished")
		unityads.connect("UnityAdsLoad", self, "_on_UnityAdsLoad")
		unityads.connect("UnityAdsError", self, "_on_UnityAdsError")
		unityads.connect("UnityAdsLoadError", self, "_on_UnityAdsLoadError")
		addsEngine.initialise("1687685", false) # UNITY_ADS_PROJECT (from your Unity console) and TestMode enabled
	else:
		print("Couldn't find UnityAdsGodot plug-in")

func _on_UnityAdsReady():
	print("UnityAdsReady is ready")
			
func _on_UnityAdsError(reason, message=""):
	print("UnityAdsError. Reason: "+reason+", "+message)

var loadedAdsCount = 0
func _on_UnityAdsLoad(id=""):
	print("_on_UnityAdsLoad")
	loadedAdsCount += 1	
	
func _on_UnityAdsLoadError(reason, message=""):
	print("UnityAdsLoadError. Reason: "+reason+", "+message)
	unityads.loadAd("placementID")

func _on_RewardedVideo_pressed():
	if unityads != null:
		unityads.loadAd("placementID")
		if loadedAdsCount==0:
			unityads.loadAd("placementID")
			while loadedAdsCount==0:
				yield(get_tree().create_timer(0.5), "timeout")		
		unityads.show("placementID") #Take placementID from unity console
		
		
func _on_UnityAdsFinished(placement, reason):
	loadedAdsCount -= 1
	reason = int(reason)
	if reason == 2:
		print("UnityAdsFinished good")
	elif reason == 1:
		print("UnityAdsFinished User skipped ad")
	else:
		print("UnityAdsFinished Something went wrong")		

```

## Known issues

- Banners are not working?
