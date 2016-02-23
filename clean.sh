DEMO="JBoss Data Grid - Hibernate Demos"
AUTHORS="Thomas Qvarnstrom, Red Hat"
# wipe screen.
clear

echo

ASCII_WIDTH=52

printf "##  %-${ASCII_WIDTH}s  ##\n" | sed -e 's/ /#/g'
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" "Setting up the ${DEMO}"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" "    # ####   ###   ###  ###   ####      ##    #### "
printf "##  %-${ASCII_WIDTH}s  ##\n" "    # #   # #   # #    #      #        #  #   #   #"
printf "##  %-${ASCII_WIDTH}s  ##\n" "    # ####  #   #  ##   ##    ###     #####   #### "
printf "##  %-${ASCII_WIDTH}s  ##\n" "#   # #   # #   #    #    #   #      #     #  #    "
printf "##  %-${ASCII_WIDTH}s  ##\n" " ###  ####   ###  ###  ###    ####  #      #  #    "
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" "brought to you by,"
printf "##  %-${ASCII_WIDTH}s  ##\n" "${AUTHORS}"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" | sed -e 's/ /#/g'

echo
echo "Cleaning upthe ${DEMO} environment..."
echo

#If EAP is running stop it
echo "  - stopping any running jboss eap instances"
echo
jps -lm | grep jboss-eap | grep -v grep | awk '{print $1}' | xargs kill -KILL

sleep 2

echo "  - removing target directory"
echo
rm -rf target
