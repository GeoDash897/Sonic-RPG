# Sonic RPG
This is a project that I work on in my free time! :D
<br /> Note!- some of this might be moved README.md's in other repositories (specifically about the information about my previous projects)

- Table of Contents:
    - [History of Project](#history-of-project-overview)
        - [Java 2 Final Project](#java-2-final-project)
    - Characteristics
  
  ## History of Project (Overview)
I originally started this project back in early 2019 as my final project for my high school's Java 2 Gaming course. We were tasked with a game using the skills/concepts we learned throughout the school year. Being a Sonic fan, I decided to create 2D Sonic platformer similar to the original Sonic games on the [Sega Genesis](https://sonic.fandom.com/wiki/Category:Sega_Mega_Drive_games), [Master System](https://sonic.fandom.com/wiki/Category:Sega_Master_System_games), and [Game Gear](https://sonic.fandom.com/wiki/Category:Game_Gear_games). 

I started to develop the game in early March in my spare time (yeah, I started it before I was even *assigned* the project). 
This was the first project that I worked full time on (actually put time and effort into it). I was so dedicated to the project in fact that I worked on it during my lunch period instead of eating.

### Java 2 Final Project
Prior to programming the project, I was required to create a summary of the game and its components (story, game mechanics, etc). I pitched the game as being a crossover of [Sonic the Hedgehog](https://en.wikipedia.org/wiki/Sonic_the_Hedgehog) and [Metal Slug](https://en.wikipedia.org/wiki/Metal_Slug). Going into the project, I had a few goals I wanted to accomplish/implement: animations with multiple frames, system to store multiple levels, music system to play and change music, different kinds of enemies, a title screen, multiple characters, etc. I wanted to highlight some of the systems/code that I created:
#### Animation System
<img src ="/GitHub Resources/Sonic Intro Gif.gif" width= "1100" height = "500"/>

*Apparently Sonic doesn't like being grayscale.*

One of the topics that we covered in the Java 2 course was drawing Images, specifically using the method below:
```java
Image temp = Toolkit.getDefaultToolkit().getImage("insert path to source here");
```
However, we never went over how to create and display animations in Java. This caused me to start messing around with Toolkit to try and achieve that effect myself. After working on the issue for a few days, I was the first person in my class to implement them in Java as well as one of the few who actually had animations in their final project.
#### Title Screen
<img src ="/GitHub Resources/Sonic Title Gif.gif" width= "1100" height = "500"/>

*Look at Sonic go!*

This is one of the aspects that I am *stll* the most proud of when it comes to this project. The title screen is made of 2 main compontents- the palmtrees, and the ground. Implementing the scrolling consisted of 2 steps: shifting over the images a certain distance and then resetting them back to their original position. I added the parallax effect by shifting over the palmtrees at a slower rate.
