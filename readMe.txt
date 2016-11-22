This is a Chat room with file transfer implementation with java socket.
The soucre code contains Java file and Java compile file.
How to use this: 
	make sure you have Javac and Java in your environment.
	In Windows cmd, cd to the directory, type command: java Server [port number (range from 0 - 2^16)]
	In new Windows cmd, cd to the directory, type: java Client [client Number] [server port Number].
	you can create multiple cilents.
the command in the client window: 
	(1) broadcast message "[your message]"
	(2) broadcast file "[your file path (can be either relative or absolute)]"
	(3) unicast message "[your message]" [client Target]
	(4) unicast file "[your file path]" [client Target]
	(5)blockcast message "[your message]" [blocked client Target]
	(6)blockcast file "[your file path]" [blocked client Target]

when you finish running the program, press ctrl + c to quit server or client.

	2016/11/22
	Computer Network