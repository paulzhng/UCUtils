# Medic-Commands

### ARecipeGiveCommand
**Why?**\
Some players, especially cops or badfaction-players with the appropriate arrangements, want up to
50 prescriptions. Counting and entering the command 50 times can be annoying.

**How?**\
The command `/arezept [Spieler] [Rezept (Antibiotika/Hustensaft/Schmerzmittel)] [Anzahl]` is used to
declare private variables.\
A process triggered by a ClientChatReceivedEvent checks if there is a give-recipe-process. If one of 
the private variables is not zero, a process is found. Now a check is made to see if all conditions 
for selling recipes are true. If so, the command `/rezept [Spieler] [Rezept (Antibiotika/Hustensaft/Schmerzmittel)]` 
is used. If not, the process stops and ends.\
If this recipe is sold successfully, the ClientChatReceivedEvent is triggered and the process 
begins again. This continues until a condition is not met or the number of `recipeGiveAmountLeft` 
becomes 0.

**Problem**\
It is not a command that has to be executed every 10 seconds. It is a command that has to be 
executed when the other player has responded. So you cannot use a timer.
- [x] reaction to ClientChatReceivedEvent instead of a fixed time interval
- [x] in this picture you can see that a new offer will be made immediately if the old one has been 
accepted

![](https://i.imgur.com/xxlkOwO.png)

### ARecipeAcceptCommand
**Why?**\
The counterpart to `/arezept [Spieler] [Rezept (Antibiotika/Hustensaft/Schmerzmittel)] [Anzahl]`.

**How?**\
The command `/arezeptannehmen [Anzahl]` is used to declare private variables.\
A process triggered by a ClientChatReceivedEvent checks if there is an accept-recipe-process. If one of
the private variables is not zero, a process is found. Now the command `/annehmen` is used. If not, 
the process stops and ends.\
If there is a new recipe request, the ClientChatReceivedEvent is triggered and the process
begins again. This continues until a condition is not met or the number of `recipeAcceptAmountLeft`
becomes 0.

**Problem**\
It is not a command that has to be executed every 10 seconds. It is a command that has to be
executed when the other player has responded. So you cannot use a timer.
- [x] reaction to ClientChatReceivedEvent instead of a fixed time interval