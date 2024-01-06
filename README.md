# sea-battle
Hi✋
This is a modest implementation of the famous game "Sea Battle" in the Kotlin programming language.
The program has three activity windows:
- Initial activity:
Contains buttons to go to placing ships on the boards. Reset ship settings button. Also a button to start the game.
- Place ships on board activity (appears once for each player in one game session):
  There are ships on one half, and a board on the other. And the ships need to be dragged to the right places on the board (there are all the rules for placing ships, so it will not work to place the ship according to the rules). Additionally, the activity has a dialog box that appears when the activity appears and is designed to retrieve the player's name.
- Battle activity:
  Contains the game boards of the players on both sides. Players take turns clicking on the cells of the opponent's board where, in their opinion, the ship is located. If the ship is hit, the player selects the cell again and so on until they miss. When any player's ships are completely destroyed, the game ends and a dialog box appears with the winner and a button to return to the main menu.