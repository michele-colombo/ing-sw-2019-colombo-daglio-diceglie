# Prova Finale Ingegneria del Software 2019
## Gruppo AM35

- ###   10521451    Colombo Michele ([@michele-colombo](https://github.com/michele-colombo))<br>michele11.colombo@mail.polimi.it
- ###   10537168    Daglio Gabriele ([@GDaglio](https://github.com/GDaglio))<br>gabriele.daglio@mail.polimi.it
- ###   10538525    Diceglie Giuseppe ([@DiceglieGiuseppe](https://github.com/DiceglieGiuseppe))<br>giuseppe.diceglie@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Complete rules | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) |
| Socket | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) |
| RMI | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) |
| GUI | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) |
| CLI | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

##Server configuration
###Default
If server is runned without arguments, it will run using:
IP address open: 127.0.0.1
Port: 12345
Layout configuration for new games: casual
Number of skulls for new games: 8
Time to wait before starting the game with less than 5 players: 10 seconds
Time to wait before disconnecting a player who is taking to long to decide his move: 90 seconds
###Passing arguments
To change configuration to desired value, user must type:   -field argument
IP address open: -ip ip_address (x.y.z.w)
Port: -port port_number (from 1024 to 65534)
Layout configuration for new games: -layout layout_config (from 0 to 3, -1 for casual)
Number of skulls for new games: -skulls skulls_number (from 5 to 8)
Time to wait before starting the game with less than 5 players: -login login_time (in milliseconds)
Time to wait before disconnecting a player who is taking to long to decide his move: -input input_time (in milliseconds)

If user don't set configuration, deefault is loaded. If he sets invalid configuration a help message id printed.
To print help type -h

###Example
server -ip 10.0.0.1 -login 30000 -skulls 5 -layout 0 -port 1025 -input 50000


##Client configuration
###Default
If client is runned without arguments, it will run using:
IP address of server: 127.0.0.1
Port of the server: 12345
User Interface: gui
###Passing arguments
To change configuration to desired value, user must type:   -field argument
IP address of server: -ip ip_address (x.y.z.w)
Port of the server: -port port_number (from 1024 to 65534)
User Interface: -ui user_interface_type (gui or cli)

If user don't set configuration, deefault is loaded. If he sets invalid configuration it's re-asked in a command line dialog.
To print help type -h

###Example
client -ui cli -ip 14.0.2.3 -port 65533


##Layout guide
0: good for 4 or 5 players  (12 squares)
1: big one in pages 2 and 3 of manual (11 squares, no top-right corner)
2: good for every players number (11 squares, no bottom-left corner)
3: good for 3 or 4 players (10 squares) 
