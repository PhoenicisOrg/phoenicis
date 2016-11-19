POL_HOME=$(dirname $0)
CLASSPATH=${CLASSPATH}:$POL_HOME/lib/*

# Ensure path of Java 8 takes precedence on systems with Java 7 and 8 both installed
# This is non-destructive and does not force users to set the default java env to Java 8
export PATH=/usr/lib/jvm/java-8-openjdk/jre/bin/:$PATH

java -classpath "$CLASSPATH" com.playonlinux.app.PlayOnLinuxApp "$@"
