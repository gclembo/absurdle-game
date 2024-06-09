This project is a variation on the game Wordle called Absurdle. I adapted it from <br />
a project in my CSE 122 class at the University of Washington and added JavaFX for user input. <br />
The game starts off with a list of 5 letter words from src\main\resources\gclembo\absurdle\words.txt <br />
and cuts words when he user makes a guess. The game takes all remaining words and creates a Wordle style <br />
pattern based on the user guess for each word. It then chooses the largest list of words with the same <br />
pattern and replaces the old list of words with this list. The user is then shown the pattern associated <br />
with the remaining words. <br />