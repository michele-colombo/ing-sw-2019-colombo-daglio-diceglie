# Prova Finale Ingegneria del Software 2019
## Gruppo AM35

- ###   10521451    Colombo Michele ([@michele-colombo](https://github.com/michele-colombo))<br>michele11.colombo@mail.polimi.it
- ###   10537168    Daglio Gabriele ([@GDaglio](https://github.com/GDaglio))<br>gabriele.daglio@mail.polimi.it
- ###   10538525    Diceglie Giuseppe ([@DiceglieGiuseppe](https://github.com/DiceglieGiuseppe))<br>giuseppe.diceglie@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

## How to build the jars
In order to build the jars just run the Maven command: mvn package. 
You will find the two jars (client.jar and server.jar) in the folder target/shadeJars.

## Server
To run server, in terminal type: 
<br>java -jar server.jar

### Server configuration
#### Default
If server is runned without arguments, it will run using:
<br>IP address open: 127.0.0.1
<br>Port: 12345
<br>Layout configuration for new games: random
<br>Number of skulls for new games: 8
<br>Time to wait before starting the game with less than 5 players: 10 seconds
<br>Time to wait before disconnecting a player who is taking to long to decide his move: 90 seconds
#### Passing arguments
To change configuration to desired value, user must type:   -field argument
<br>IP address open: -ip [ip_address] (x.y.z.w)
<br>Port: -port [port_number] (from 1024 to 65534)
<br>Layout configuration for new games: -layout [layout_config] (from 0 to 3, -1 for random)
<br>Number of skulls for new games: -skulls [skulls_number] (from 5 to 8)
<br>Time to wait before starting the game with less than 5 players: -login [login_time] (in milliseconds)
<br>Time to wait before disconnecting a player who is taking to long to decide his move: -input [input_time] (in milliseconds)

<br>If user don't set configuration, default is loaded. He can set also only one configuration. If he tries sets invalid configuration a help message is printed.
<br>To print help type -h

### Example
java -jar server.jar -ip 10.0.0.1 -login 30000 -skulls 5 -layout 0 -port 1025 -input 50000

## Client
To run client, in terminal type: 
<br>java -jar --module-path [javafx lib folder path] --add-modules javafx.controls client.jar
### Client configuration
#### Default
If client is runned without arguments, it will run using:
<br>IP address of server: 127.0.0.1
<br>Port of the server: 12345
<br>User Interface: gui
#### Passing arguments
To change configuration to desired value, user must type:   -field argument
<br>IP address of server: -ip [ip_address] (x.y.z.w)
<br>Port of the server: -port [port_number] (from 1024 to 65534)
<br>User Interface: -ui [user_interface_type] (gui or cli)

<br><br>If user don't set configuration, default is loaded. He can set also only one configuration. If he sets invalid configuration it's re-asked in a command line dialog.
<br>To print help type -h

### Example
java -jar --module-path ./javafx-sdk-11.0.2/lib --add-modules javafx.controls client.jar -ui cli -ip 14.0.2.3 -port 65533


## Layout guide
0: good for 4 or 5 players  (12 squares)
<br>1: big one in pages 2 and 3 of manual (11 squares, no top-right corner)
<br>2: good for every number of players (11 squares, no bottom-left corner)
<br>3: good for 3 or 4 players (10 squares) 
