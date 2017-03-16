#!/usr/bin/env bash

# A; B    Run A and then B, regardless of success of A
# A && B  Run B if A succeeded
# A || B  Run B if A failed
# A &     Run A in background.

xterm -hold -e java -cp production Systems.Server &
sleep 1 &&
xterm -hold -e java -cp production Systems.Client

# -cp is a flag standing for Class Path
