#!/bin/bash
basedir=`dirname $0`


DEMO="JBoss Data Grid - Hibernate Demos"
AUTHORS="Thomas Qvarnstrom, Red Hat"
SRC_DIR=$basedir/installs


EAP_VERSION=7.0.0.ER6
EAP_INSTALL=jboss-eap-${EAP_VERSION}.zip
EAP_REPO=jboss-eap-${EAP_VERSION}-maven-repository.zip

DB_DRIVER=postgresql-9.4.1207.jar

REPOS=($EAP_REPO)
SOFTWARES=($EAP_INSTALL)

JBOSS_HOME=target/jboss-eap-7.0



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
echo "Setting up the ${DEMO} environment..."
echo


java -version > /dev/null 2>&1 || { echo >&2 "Java is required but not available on the path... aborting."; exit 1; }

# Check that maven is installed and on the path
mvn -v -q >/dev/null 2>&1 || { echo >&2 "Maven is required but not installed yet... aborting."; exit 1; }

# docker --version > /dev/null 2>&1 || { echo >&2 "Docker is required, please install it first... aborting."; exit 1; }
# docker ps > /dev/null 2>&1 || { echo >&2 "Can't connect to the docker server, please make sure that it's running and that all environment variables are correctly set... aborting."; exit 1; }

# Verify that necesary files are downloaded
DOWNLOADS=("${REPOS[@]}" "${SOFTWARES[@]}")
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


#If EAP is running stop it
echo "  - stopping any running jboss eap instances"
echo
jps -lm | grep jboss-eap | grep -v grep | awk '{print $1}' | xargs kill -KILL

#If docker containers is running stop it
echo "  - stopping any removing container with name=demodb"
echo

docker ps -q -f "name=demodb"  | xargs docker stop > /dev/null
docker ps -aq -f "name=demodb"  | xargs docker rm > /dev/null

# Create the target directory if it does not already exist.
if [ -x target ]; then
		echo "  - deleting existing target directory..."
		echo
		rm -rf target
fi
echo "  - creating the target directory..."
echo
mkdir -p target/mvn-repos

for repo in ${REPOS[@]}
do
	echo "  - unpacking repository $repo"
	echo
	unzip -q -d target/mvn-repos $SRC_DIR/$repo
done

echo "  - pulling latest Postgres:9.4 from docker.io"
echo
docker pull postgres:9.4 > /dev/null

echo "  - staring Database containter"
echo
docker run --name demodb \
	-e POSTGRES_PASSWORD=demo \
	-e POSTGRES_USER=demo \
	-e POSTGRES_DB=demo \
	-p 5432:5432 \
	-d postgres:9.4 > /dev/null


# Unzip the software repo files
for software in ${SOFTWARES[@]}
do
	echo "  - installing software $software"
	echo
	unzip -q -d target $SRC_DIR/$software
done

echo "  - adding admin user to JBoss EAP 7"
echo
${JBOSS_HOME}/bin/add-user.sh -g admin -u admin -p admin-123 -s

echo "  - lowering default memory of JBoss EAP 7"
echo
if [ "$(uname)" =  "Darwin" ]
then
	sed -i '' "s/-Xms1303m -Xmx1303m/-Xms128m -Xmx512m/" ${JBOSS_HOME}/bin/standalone.conf
else
	sed -i "s/-Xms1303m -Xmx1303m/-Xms128m -Xmx512m/" ${JBOSS_HOME}/bin/standalone.conf
fi

echo "  - adding demoDS datasource to JBoss EAP 7"
echo
${JBOSS_HOME}/bin/jboss-cli.sh --file=support/eap-config-local.cli


# echo "  - installing jboss eap 7"
# echo
# cp support/install-config.xml .
# if [ "$(uname)" =  "Darwin" ]
# then
# 	sed -i '' "s#\${PROJECT_DIR}#$(pwd)#" install-config.xml
# else
# 	sed -i "s#\${PROJECT_DIR}#$(pwd)#" install-config.xml
# fi
# java -jar installs/jboss-eap-7.0.0.ER5-installer.jar install-config.xml
# rm install-config.xml


# Build the projects
# echo "  - building the stock-ticker project"
# echo
# pushd projects/stock-ticker > /dev/null
# mvn -q clean install
# popd > /dev/null




# Configure JDG cluster
# if [ "$(uname)" =  "Linux" ]
# then
	# $SED_REPLACE "s/-Xms1303m -Xmx1303m/-Xms128m -Xmx512m/" ./target/jboss-datagrid-${JDG_VERSION}*-server/bin/clustered.conf
	# $SED_REPLACE '
	# /<stack name="udp">/,/<\/stack>/ {
	#     N
	#         /<protocol type="pbcast.GMS"\/>/ {
	#             i\
	#             <protocol type="pbcast.GMS">
	#             a\
	#             <\/protocol>
	#             c\
	#             <property name="join_timeout">3000</property>
	#         }
	# }' ./target/jboss-datagrid-${JDG_VERSION}*-server/standalone/configuration/clustered.xml
# else
# 	sed -i '' "s/-Xms1303m -Xmx1303m/-Xms128m -Xmx512m/" ./target/jboss-datagrid-${JDG_VERSION}*-server/bin/clustered.conf
# 	sed -i '' '
# 	/<stack name="udp">/,/<\/stack>/ {
# 	    N
# 	        /<protocol type="pbcast.GMS"\/>/ {
# 	            i\
# 	            <protocol type="pbcast.GMS">
# 	            a\
# 	            <\/protocol>
# 	            c\
# 	            <property name="join_timeout">3000</property>
# 	        }
# 	}' target/jboss-datagrid-${JDG_VERSION}*-server/standalone/configuration/clustered.xml
# fi
