Team Smash, Group Number 16
Mudita Verma(18200087), Elisa Bouvet(19207177), Qetelo Sabasaba(18205738)
Assignment 5 : Bot
The goal of this assignment is to write a bot that can play a game of Scrabble against another player. The structure of the bot is already given, the bulk of the work was to write a function that choses where the bot should put a word, and what word to put. In order do to that, the bot works as follows :
- A first function, called findEmptySpaces, parses the board and looks at every place it could put a word. It returns an array list of string objects, that have the following format : “A1 D M****”. For now, our function only returns place with the first letter already on the board. We started coding this function like that because it was easier, and did not have the time to include other cases, when several letters are already on the board or when they are not at the beginning of the word.
- The second function takes as a parameter this array list of string, and tries every combination possible with the letters in the bot’s frame. It then tests all these words with the dictionary, and stores all the real words in an array list of Word objects.
- A third function called maximisePoint takes as a parameter this array list of Words, computes the points for all these words and returns the word with the highest score.
- Finally, a last function called getWordCommand takes this Word as a parameter, and returns it as a String that has the right format so that the bot can send it as a command to put the word on the board.
