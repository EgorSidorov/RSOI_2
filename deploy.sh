eval mvn tomcat7:redeploy -DskipTests=true cobertura:cobertura
eval bash <(curl -s https://codecov.io/bash)
