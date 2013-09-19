# Fly Object Space For Java


Welcome to Fly for Java!  

**Fly** is an Object Space server that is specifically written to provide 
lightweight object based messaging between computers running on a network. 

Before you start you need to make sure that have a version of Java installed 
and running on your machine. Type -

```
> java -version
```

- into a command prompt or shell. If this fails you will need to download a
recent version of Java SE (1.5 or above) from www.java.com and install this 
onto your machine.

Unzip the FlyJava download which will create the fly directory.

In  windows double click the startFly.bat file from the windows explorer and
then double click the runExample.bat file. Alternativley type ... 

```
> cd fly
> .\startFly.bat
> .\runExample.bat 
```

in a windows command prompt. 

On unix systems the startFly.sh script is set up to run the version of
fly for the host platform. See the comments in the script if you want to
run the fly server directly.

```
% cd fly
% sh startFly.sh
% sh runExample.sh
```

In either case, if this successful you will see something like this -

```
      >      
    <----    
  -------->  
    <----       Fly Server (c) MMVI Zink Digital Ltd. 
      >       Ver 1.0 : LBI 1.0 : Non Commercial License.

Fly Server started on port 4396
```

and then some output from the example code which writes and takes 1000 example 
objects to and from the space server. To write more or less objects, vary the 
final parameter, or try running a number of example clients in parallel.

If you want to see the java source for the WriteTake example look in the src 
directory. There are many examples in here of how to use the server via the 
java bindings.

Enjoy using Fly! 
