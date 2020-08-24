# SkDynmap

**SkDynmap** is a Skript addon that let you communicate with the Dynmap plugin for Spigot.
**SkDynmap** allow you to create coloured areas, like this:

<img src=https://i.imgur.com/zzCoBs6.png> <img src="https://i.imgur.com/rNaVjN5.png">

This addon is actually in developement, and many features will come later.

For any questions, or report bugs, contact me on **Discord:** `@Skylyxx#8816`.
---
## Usage

```vbs
command /pos1:
	trigger:
		set {pos1} to player's location
		send "&apos1 set !"

command /pos2:
	trigger:
		set {pos2} to player's location
		send "&apos2 set"

command /createarea:
    trigger:
        create new dynmap area named "My Area" with description "A beautiful description" in world of player between {pos1} and {pos2}
        send "&aarea created !"
```

#### Result

<img src="https://i.imgur.com/FZbwDnl.png"><br>
And when I click on the area:
<img src="https://i.imgur.com/HoguCKw.png"><br>
<br>
---
## Installation

To install **SkDynmap**:
- Download <a href="https://github.com/SkriptLang/Skript/releases/latest">Skript</a> and put in the **plugins/** folder of your server
- Download **SkDynmap** <a href="https://github.com/SkylyxxFR/skdynmap/releases/latest">here</a> and put in the **plugins/** folder of your server
- Download <a href="https://dev.bukkit.org/projects/dynmap">Dynmap</a> and put in the **plugins/** folder of your server
- Restart your server
- Enjoy ! 😉
<br><br>

---
## Syntaxes

The differents syntaxes of SkDynmap are listed below.

#### Creating an area

**Syntax:** `create [new] [dynmap] area (named|with name) %string% [with description %string%] in %world% between %locations% and %locations% [with style %strings%]`

**Exemple (Without Stylizing):** 
```py
command /myarea:
    trigger:
        create new dynmap area named "My Area" with description "A beautiful description" in world of player between {pos1} and {pos2}
```
**Exemple (With Stylizing):** <br>
___Note__: For making custom style, you will need use a text list in the following format: __"fillColor", "fillOpacity", "lineColor", "lineOpacity" and "lineWeight"__. For using values set in the config, use the keyword __"default"___
```py
command /myarea:
    trigger:
        set {_style::*} to "20b848", "default", "default", "0.8" and "3"
        create new dynmap area named "My Area" with description "A beautiful description" in world of player between {pos1} and {pos2} with style {_style::*}
```

#### Deleting an area

**Syntax:** `delete [dynmap] area (named|with name) %string% in %world%`

**Exemple**:
```py
command /deletemyarea:
	trigger:
    	    delete area named "MyArea" in world of player
```

---
## Future features

##### In the future, I plan to add some features:

- Adding the possibility to make areas with several locations, for making non-squared areas
- Adding the `all [dynmap] areas` expression, for 
making loops of
areas.
- Adding effects to edit style of existing areas. (Now, you can already do that using the areas.yml file, with <a href="https://github.com/Sashie/skript-yaml">Skript-Yaml</a>.