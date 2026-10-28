# Define a imagem base utilizada para executar a aplicação Web Java.
# A imagem contém o Apache Tomcat 9.0 com JDK 8.
FROM tomcat:9.0-jdk8

# Remove as aplicações padrão do Tomcat para evitar conflito com a raiz (ROOT)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o arquivo WAR da aplicação, previamente gerado pelo Maven (mvn package),
# do computador para o diretório de aplicações Web do Tomcat dentro do container.
#
# Arquivo de origem gerado pelo Maven (<finalName>controle-estoque</finalName>):
# ./target/controle_de_estoque_java.war
#
# Arquivo de destino dentro do container:
# /usr/local/tomcat/webapps/ROOT.war
#
# O nome ROOT.war faz com que o Tomcat disponibilize a aplicação
# diretamente na raiz do servidor Web (http://localhost:8080).
COPY ./target/controle_de_estoque_java.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]