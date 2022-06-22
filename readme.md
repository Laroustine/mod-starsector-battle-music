# Custom Battle Music

Welcome to **Custom Battle Music**. This mod adds in the option for Faction music during battle.

This mod can be added and removed without conflict. To add compatibility with you mod is also very easy.

# Making it Compatible

## For Players

You first need to add a music to the this folder `Battle_Music/sounds/music` _(the file name is not important)_. The file extension should be in `.ogg`.

You first need to go in the file `Battle_Music/data/config/sounds.json` _(if it does not exist create it)_ and then add this.

```json
{
  "music": {
    "my_music_name": [
      {
        "source": "sounds/music",
        "volume": 0.15,
        "file": "my_music_name.ogg"
      }
    ],
  }
}
```

Now the song should be loaded by the game but you will not hear it anywhere.

You can have multiple music per playlist and multiple playlist like this.

```json
{
  "music": {
    "playlist1": [
      {
        "source": "sounds/music",
        "volume": 0.15,
        "file": "music1.ogg"
      },
      {
        "source": "sounds/music",
        "volume": 0.15,
        "file": "music2.ogg"
      },
    ],
    "playlist2": [
      {
        "source": "sounds/music",
        "volume": 0.15,
        "file": "music3.ogg"
      },
      {
        "source": "sounds/music",
        "volume": 0.15,
        "file": "music4.ogg"
      },
    ],
  }
}
```

Now to add a song to a faction we need to select a faction from this list :
- derelict
- hegemony
- independent
- knights_of_ludd
- lions_guard
- luddic_church
- luddic_path
- omega
- persean_league
- pirates
- remnants
- sindrian_diktat
- tritachyon

in the folder `Battle_Music/data/world/factions` create a file named `<FACTION_NAME>.faction` _(example: omega.faction)_

in the file put in this:
```json
{
	"music": {
		"battle": "my_music_name"
	}
}
```

There can only be 1 music per faction.

## For Modders

To add compatibility for your mod you just need to add this in your ***.faction**

```json
"music": {
  "battle": "my_music_name"
}
```

and you also need to add the list of songs for your faction in the config file **sounds.json**.

```json
{
  "music": {
    "music_faction_battle": [
      {
        "source": "sounds/music/",
        "volume": 0.15,
        "file": "song1.ogg"
      }
    ]
  }
}
```

The name of the list of songs can be different from the one above and need to be different from any other existing list.
You can also use song lists present in the game already.
