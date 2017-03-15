#!/usr/bin/env bash

xterm -hold -e java -cp production Systems.Client &
xterm -hold -e java -cp production Protocols.Server

# -cp is a flag standing for Class Path
