# Text-Adventure
A simple backbone for a text adventure

Created by Mitchell Brown

All this does is read any file place in the source files named "game.txt", it also needs a "gameOriginal.txt" 
it then reads it and uses it to determine what the locations are in the game and what directions you need to go to get to them.
It also determines what items are in a loctions. It does this with special characters that are next to everything.
Eg.:
'@' = locations
'%' = descriptions
'!' = a direction you can go from the location
'*'= where the above direction leads
'#' = an individual item (each item has to be on a seperate line with its' own '#'
You can go around and interact with objects, look at your inventory, and drop objects in new locations.
