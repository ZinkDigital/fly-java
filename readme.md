# Fly Object Space For Java


Welcome to Fly for Java!  

**Fly** is an Object Space server that is specifically written to provide 
lightweight object based messaging between computers running on a network. 


## Getting Started 

The Fly distribution contains binaries for the Fly Server and the prebuilt jar built for Java clients
of the space. If you need these then you need to download the release from 
github by pressing the release tab on the github fly-java project page.

Download the latest release of the package in order to get the binaries and prebuilt library,
and unzip the download.

### Check Java Install and Version

Before you start you need to make sure that have a version of Java installed 
and running on your machine. Type -

```
> java -version
```

into a command prompt or shell. If this fails you will need to download a
recent version of Java SE (1.5 or above) from www.java.com and install this 
onto your machine.



### Starting the Server and Example in Windows

Double click the startFly.bat file from the windows explorer and
then double click the runExample.bat file. 

Alternativley in a windows command prompt 'cd' to the root of the fly directory 
and start the server

```
> .\startFly.bat
```

Preferably in a new command prompt, run the example code.

```
> .\runExample.bat 
```


### Starting the Server and Example on Linux, MacOS and Solaris

Start a shell and 'cd' into the root of the fly directory 

On unix systems the startFly.sh script is set up to run the version of
fly for your host platform. 

```
% sh startFly.sh
```

See the comments in the script if encounter problems or you want to
run the fly server directly from the bin directory.

Preferably in a new shell, run the example code.

```
% sh runExample.sh
```

### Expected Results

In either case, if this successful you will see something like this 

```

      >      
    <----    
  -------->  
    <----       Fly Server (c) 2013 Zink Digital Ltd. 
      >       Ver 2.0 : LBI 2.0 : Non Commercial License.

Fly Server started on port 4396

```

and then some output from the example code which writes and takes 1000 example 
objects to and from the space server. To write more or less objects, vary the 
final parameter in the runExample scritp, or try running a number of example 
clients in parallel.

If you want to see the java source for the WriteTake example look in the src 
directory. There are many examples in here of how to use the server via the 
java bindings.

Now you are flying.
