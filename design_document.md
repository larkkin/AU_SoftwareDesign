## Design Document for Balloons: roguelike game to help you bind your time

Actually, this roguelike game is quite classy: it is a roguelike, where a girl (named *Girl*) is travelling in the world of clouds: she can move through the air, and cannot - through the clouds. She gets damaged by birds and thunderclouds. Her goal is to be able to travel as long as she can: when the game is over, she returns to her real life.

## What can be extended: 

- Developers who want to upgrade this game can use tile graphics. Some png_s that may appear useful are provided in 'pictures' folder. By now, graphics is console;
- In case the game is too easy/boring for you, you can effortlessly add new obstacles for the Girl (e.g. new bird types);
- You can also add game levels: it can be achieved by varying Bird speed and Map difficulty (you can implement your own map generation algorithm);
- You can also extend Girl's super powers.

## Player characteristics

There can be three types of players:
- Portrait of the **player A**:
	It's a person who likes to think over and over all the things while playing the game. He/she does not want the game to take a lot of his/hers brain efforts. He/she wants the game to provide minimum distraction from thoughts and have something to do during the process of thinking. Player does not need any narrative line.
- Portrait of the **player B**:
	This player needs to have some narrative line underneath the game interface. He/she is full of dreams, energy to be creative and imagine everything. This type of player needs some basic graphics to be able to create the story in her/his imagination. For this player we have a bit of ascii graphics in our game.
- Portrait of the **player C**: 
	In case the game does not become popular, the one and only player of my game is The Professor. He is fed up with checking 20 homeworks, and what he wants to see is clear architecture and something play-able. Alas.

## architecture, logic, pattern

	Model-View-Controller architectural pattern seems to suit the best the needs of roguelike.
   - *Model*: game rules + all roguelike game objects;
   - *Controller*: units essential for interacting with user and model. They know scenarios.
   - *View*: is responsible for representation. It sees only the model. 

Map generation: TO BE DONE. As I see it, we will define a certain percentage of the walls + we will need some algorihm to ensure that next level can be unlocked. 

