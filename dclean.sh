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

echo "  - stopping any removing container with name=demodb or name=eap7"
echo

docker ps -q -f "name=demodb"  | xargs docker stop > /dev/null
docker ps -aq -f "name=demodb"  | xargs docker rm > /dev/null

docker ps -q -f "name=eap7" | xargs docker stop > /dev/null
docker ps -aq -f "name=eap7" | xargs docker rm > /dev/null
