## Project 1 - Distrbuted Backup Service

Specification: https://web.fe.up.pt/~pfs/aulas/sd2017/projs/proj1/proj1.html

## Building the binary files

To build the binary files, the build.sh script should be run in the directory where you wish to place the 'bin' folder.

## Running the project with default arguments

To run the project, you must start a Peer and a TestApp.

To start a default Peer, simple run 'start_peer.sh' in the same directory that contains 'bin'.

For each functionality, there is a default TestApp script. Run each one of them in the same direcotry as 'bin' to run the program.

## Running the project with own arguments

### Peer

java -cp <bin folder> systems.Peer <MControlAddress> <MControlPort> <MBackupAddress> <MBackupPort> <MRestoreAddress> <MRestorePort>

### Test App

java -cp <bin folder> app.TestApp

(This will give you the usages of TestApp)

### Activate enhancements

To activate enhancements, simply change the boolean variable "enhancements" in source/systems.Peer from false to true and rebuild the project.
