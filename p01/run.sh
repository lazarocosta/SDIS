#!/usr/bin/env bash

# A; B    Run A and then B, regardless of success of A
# A && B  Run B if A succeeded
# A || B  Run B if A failed
# A &     Run A in background.

xterm -hold -e java -cp production systems.Peer 1.0 2 accessPoint 228.5.6.7 3000 228.5.6.6 4000 228.5.6.8 5000 &
xterm -hold -e java -cp production systems.Peer 1.0 2 accessPoint 228.5.6.7 3000 228.5.6.6 4000 228.5.6.8 5000 &
sleep 1 &&
xterm -hold -e java -cp production app.TestApp accessPoint BACKUP test/McastSnooper.jar 1

# -cp is a flag standing for Class Path
