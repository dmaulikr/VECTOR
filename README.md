VECTOR
======

An online game developed in Java that is loosely based off of Pool.

To Run
======

Create a text file in the root of the application directory called "combos setup.txt", and for each line enter a username and password combination deliminated by a space, for example:

username1 password1
username2 password2
username3 password3
username4 password4
username5 password5
...

Then create another text file called "combos.txt" in the same directory location and run CombosSetup.java to populate "combos.txt" with the username-password data from "combos setup.txt."  

Next, update the "schoolIP" instance field to the IP address of computer running the application.  You can just put the localhost IP (127.0.0.1) if you will be running the client and server all on the same computer.  Finally, start the server by running "Server.java," and start the game by running "Driver.java."  You will have to login with the credentials you entered in to "combos setup.txt."

Enjoy!
