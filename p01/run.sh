#!/usr/bin/env bash

# A; B    Run A and then B, regardless of success of A
# A && B  Run B if A succeeded
# A || B  Run B if A failed
# A &     Run A in background.

xterm -hold -e java -cp production RMI.Server &
sleep 1 &&
xterm -hold -e java -cp production RMI.Client

# -cp is a flag standing for Class Path
