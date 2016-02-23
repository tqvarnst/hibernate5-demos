#!/bin/bash
basedir=`dirname $0`


DEMO="JBoss EAP 7 - Hibernate Demos using Docker"
AUTHORS="Thomas Qvarnstrom, Red Hat"
SRC_DIR=$basedir/installs


EAP_VERSION=7.0.0.ER5
EAP_INSTALL=jboss-eap-${EAP_VERSION}-installer.jar
EAP_REPO=jboss-eap-${EAP_VERSION}-maven-repository.zip

SOFTWARES=($EAP_INSTALL)
REPOS=($EAP_REPO)

DOWNLOADS=("${REPOS[@]}" "${SOFTWARES[@]}")

# wipe screen.
clear

echo

ASCII_WIDTH=70

printf "##  %-${ASCII_WIDTH}s  ##\n" | sed -e 's/ /#/g'
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" "Setting up the ${DEMO}"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" "    # ####   ###   ###  ###   ####      ##    ####   ######"
printf "##  %-${ASCII_WIDTH}s  ##\n" "    # #   # #   # #    #      #        #  #   #   #      #"
printf "##  %-${ASCII_WIDTH}s  ##\n" "    # ####  #   #  ##   ##    ###     #####   ####      #"
printf "##  %-${ASCII_WIDTH}s  ##\n" "#   # #   # #   #    #    #   #      #     #  #        #"
printf "##  %-${ASCII_WIDTH}s  ##\n" " ###  ####   ###  ###  ###    ####  #      #  #       #"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" "brought to you by,"
printf "##  %-${ASCII_WIDTH}s  ##\n" "${AUTHORS}"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n"
printf "##  %-${ASCII_WIDTH}s  ##\n" | sed -e 's/ /#/g'

echo
echo "Setting up the ${DEMO} environment..."
echo

docker --version > /dev/null 2>&1 || { echo >&2 "Docker is required, please install it first... aborting."; exit 1; }
docker ps > /dev/null 2>&1 || { echo >&2 "Can't connect to the docker server, please make sure that it's running and that all environment variables are correctly set... aborting."; exit 1; }

if [ -x target ]; then
		echo "  - deleting existing target directory..."
		echo
		rm -rf target
fi

echo "  - recreating the target directory..."
mkdir -p target/mvn-repos

# Verify that necesary files are downloaded
for DONWLOAD in ${DOWNLOADS[@]}
do
	if [[ -r $SRC_DIR/$DONWLOAD || -L $SRC_DIR/$DONWLOAD ]]; then
			echo $DONWLOAD are present...
			echo
	else
			echo You need to download $DONWLOAD from the Customer Support Portal
			echo and place it in the $SRC_DIR directory to proceed...
			echo
			exit
	fi
done



for repo in ${REPOS[@]}
do
	echo "  - unpacking repository $repo"
	echo
	unzip -q -d target/mvn-repos $SRC_DIR/$repo
done



#If docker containers is running stop it
echo "  - stopping any removing container with name=demodb or name=eap7"
echo

docker ps -q -f "name=demodb"  | xargs docker stop > /dev/null
docker ps -aq -f "name=demodb"  | xargs docker rm > /dev/null

docker ps -q -f "name=eap7" | xargs docker stop > /dev/null
docker ps -aq -f "name=eap7" | xargs docker rm > /dev/null

echo "  - pulling latest Postgres:9.4 from docker.io"
echo
docker pull postgres:9.4 > /dev/null

echo "  - staring Database containter"
echo
docker run --name demodb \
	-e POSTGRES_PASSWORD=demo \
	-e POSTGRES_USER=demo \
	-e POSTGRES_DB=demo \
	-d postgres:9.4 > /dev/null

echo "  - building JBoss EAP 7 images"
echo
docker build --quiet \
	--rm=true \
	-t "local/eap7-postgres" \
	-f support/Dockerfile . > /dev/null


echo "  - start eap7 images"
echo
docker run --name eap7 \
	--link demodb:demodb \
	-p 8080:8080 \
	-p 9990:9990 \
	-p 9999:9999 \
	-d local/eap7-postgres > /dev/null


# echo "  - adding store procedure my_sum to the database"
# docker exec demodb psql -U demo -d demo -c "CREATE OR REPLACE FUNCTION my_sum(x int, y int, OUT sum int) AS 'SELECT x+y' LANGUAGE SQL"

echo "${DEMO} has successfully been installed."
echo
