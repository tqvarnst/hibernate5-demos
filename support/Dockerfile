FROM centos:7
MAINTAINER tqvarnst <tqvarnst@redhat.com>

RUN yum -y install java-1.8.0-openjdk-devel unzip sed

RUN yum -y localinstall http://yum.postgresql.org/9.4/redhat/rhel-7-x86_64/pgdg-centos94-9.4-2.noarch.rpm

RUN yum -y install postgresql94.x86_64 postgresql94-jdbc


ENV JAVA_HOME /usr/lib/jvm/jre

RUN useradd -m -d /home/jbosseap -p jbosseap jbosseap

USER jbosseap

ENV HOME /home/jbosseap
RUN mkdir $HOME/eap && \
 mkdir $HOME/tmp

COPY installs/jboss-eap-7.0.0.ER5.zip $HOME/tmp/

RUN /usr/bin/unzip -q $HOME/tmp/jboss-eap-7.0.0.ER5.zip -d $HOME/eap

ENV JBOSS_HOME $HOME/eap/jboss-eap-7.0

RUN $JBOSS_HOME/bin/add-user.sh -g admin -u admin -p admin-123 -s

RUN sed -i "s/-Xms1303m -Xmx1303m/-Xms128m -Xmx512m/" $JBOSS_HOME/bin/standalone.conf

COPY support/eap-config-docker.cli $HOME/tmp/

RUN $JBOSS_HOME/bin/jboss-cli.sh --file=$HOME/tmp/eap-config-docker.cli

# Workaround to avoid getting issues with versioning of the configuration.
RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current

RUN rm $HOME/tmp/*

CMD $HOME/eap/jboss-eap-7.0/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0

EXPOSE 8080 9990 9999
