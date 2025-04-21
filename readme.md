# Custom Battle Music

Welcome to **Custom Battle Music**! This mod allows you to add faction-specific music during battles in Starsector.

This mod is safe to add or remove without causing conflicts. Adding compatibility with your mod is also straightforward.

## For Players

### Removing Simulation Music
You can disable the simulation music by editing the `Battle_Music/data/config/settings.json` file. Locate the `cbm_simulation_ost` setting and set its value to `false`:

```json
{
  "cbm_simulation_ost": false
}
```

This will prevent the simulation music to play.

### Adding Music to the Game
1. **Add Your Music Files**  
   Place your `.ogg` music files in the following folder:  
   `Battle_Music/sounds/music`  
   _(The file name can be anything you like.)_

2. **Edit or Create `sounds.json`**  
   Navigate to `Battle_Music/data/config/sounds.json`. If the file does not exist, create it. Add or update the following structure:

   ```json
   {
     "music": {
       "my_music_name": [
         {
           "source": "sounds/music",
           "volume": 0.15,
           "file": "my_music_name.ogg"
         }
       ]
     }
   }
   ```

   Replace `"my_music_name.ogg"` with the name of your music file.

3. **Create Playlists (Optional)**  
   You can define multiple songs in a playlist or create multiple playlists. Here's an example:

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
         }
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
         }
       ]
     }
   }
   ```

   > **Info**  
   > Each playlist can contain multiple songs, and you can create as many playlists as you need.

### Assigning Music to Factions

1. **Choose a Faction**  
  Select a faction from the following list:
    - **derelict**
    - **hegemony**
    - **independent**
    - **knights_of_ludd**
    - **lions_guard**
    - **luddic_church**
    - **luddic_path**
    - **omega**
    - **persean_league**
    - **pirates**
    - **remnants**
    - **sindrian_diktat**
    - **tritachyon**

2. **Create a Faction File**  
  Navigate to the folder:  
  `Battle_Music/data/world/factions`  
  Create a file named `<FACTION_NAME>.faction` (e.g., `hegemony.faction`).

3. **Add Music to the Faction**  
  In the newly created file, add the following structure:

    ```json
    {
      "music": {
        "battle": "my_music_name"
      }
    }
    ```

    Replace `"my_music_name"` with the name of the music or playlist you want to assign to the faction.

    > **WARNING**  
    > Each faction can only have one battle music track or playlist assigned at a time.

## For Modders

### Extra Info

There are seven types of songs you can assign to your faction, each corresponding to specific battle scenarios. Below is a list of these song types, along with their default conditions:

- **battle**:  
  Plays by default for the faction during battles.  
  *(The `music_combat_set` key, which is part of the base game, is also supported. While unused by any faction in the vanilla game, it can still function even if `Custom Battle Music` is not installed.)*

- **battle_xtreme**:  
  Plays when the faction has 2.5 times more **Deployment Points** than you.

- **battle_advantage**:  
  Plays when the faction has 1.5 times more **Deployment Points** than you.

- **battle_losing**:  
  Plays when the faction has 0.5 times or fewer **Deployment Points** than you.

- **battle_destroy**:  
  Plays when the faction has 0.2 times or fewer **Deployment Points** than you.

- **battle_retreat**:  
  Plays when the faction is retreating.

- **player_retreat**:  
  Plays when you are retreating from the faction. (Can be deactivated in the settings)

> **INFO**  
> These conditions are based on the relative **Deployment Points** of the player and the opposing fleet. You are not forced to implement all of them, but it is recommended to at least implement `battle`.

### Add Music for your faction

> **INFO**  
> This is mainly goes back over the explaination for players on how to edit existing factions

To add compatibility for your mod you just need to add this in your **.faction** (e.g., `MyCustomFaction.faction`).

```json
"music": {
  "battle": "music_faction_battle"
}
```

and you also need to add the list of songs for your faction in the config file `MyMod/data/config/sounds.json`.

```json
{
  "music": {
    "music_faction_battle": [
      {
        "source": "sounds/music/",
        "volume": 0.1,
        "files": [
          "song1.ogg",
          "song2.ogg"
        ],
        "randomStart": false
      }
    ]
  }
}
```

The name of your song list can be unique and does not need to match the example above. However, it must not conflict with any existing song lists in the game or other mods.

You can also reuse song lists that are already present in the game.

### Add Music for Specific Ships

To assign custom music to a particular ship, follow these steps:

1. **Add a Custom Tag**  
  Open your `ship_data.csv` file and add a custom tag for the ship in the appropriate column. The tag must start with `cbm_` (e.g., `cbm_custom_ship_music`).

2. **Match the Tag with a Song**  
  Ensure the name of the song matches the custom tag you added. For example, if the tag is `cbm_custom_ship_music`, the corresponding song should be named `cbm_custom_ship_music` in the `MyMod/data/config/sounds.json` file.

### Add Music for Your Fleets

When creating a custom fleet with a script, you can assign custom music by following these steps:

1. **Add a Custom Tag**  
  Assign a tag to your fleet that starts with `cbm_` and matches the name of the desired song. For example, if your song is named `cbm_fleet_theme`, the tag should also be `cbm_fleet_theme`.

2. **Ensure the Song Exists**  
  Verify that the song with the matching name is defined in your `MyMod/data/config/sounds.json` file.

### Adding Music to Missions

To add custom music to a mission, include the following code snippet in your mission script. Replace `"mission_id"` with the unique identifier of your mission and `"my_mission_song"` with the name of the song or playlist defined in your `MyMod/data/config/sounds.json` file:

```java
Global.getSector().getMemory().set("mission_id", "my_mission_song");
```

> **INFO**  
> Ensure that the song or playlist specified in `"my_mission_song"` exists in your `sounds.json` file. This will allow the custom music to play during the mission.

