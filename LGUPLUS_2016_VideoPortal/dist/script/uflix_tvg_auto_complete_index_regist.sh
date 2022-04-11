# ========================================================================
# IndexTaskCommander : Register index task for each type.
# [IP] [Port] [CollectionID] [type] (option) (dbwatcher) (-file filename)
# IP : Destination server IP address
# Port : Destination server port number
# CollectionID : ID of collection to index
# type : Index type(FULL, PIPE, INC, UPDATE, ADDDOC, DELDOC, REBUILD)
# option : If type is ADDDOC or DELDOC, document IDs to be add or delete.
#          (ex : 123,523,132,9)
# dbwatcher : If type is ADDDOC and using DBwatcher, DBWatcher ID must be here. 
# -file filename : If type is ADDDOC or DELDOC, filename contains document
#                  IDs. (File content is document ID for each line.)
# <Notice>
# Each task does not execute in this process but register to server. Server
# must be alive.
# ========================================================================
IP=localhost
PORT=5555
COLL=UFLIX_TVG_AUTO_COMPLETE
TYPE=FULL
OPTION=
DBWATCHER=
java -server -Xms32m -Xmx512m -classpath "$IR4_HOME/lib/chiana.jar:$IR4_HOME/lib/dqdic-1.0.3.jar:$IR4_HOME/lib/jwsdp.jar:$IR4_HOME/lib/m4_mgr.jar:$IR4_HOME/lib/mysql-connector-java-ga-bin.jar:$IR4_HOME/lib/xerces.jar::$IR4_HOME/lib/informix.jar:$IR4_HOME/lib/m4_client.jar:$IR4_HOME/lib/m4_server.jar:$IR4_HOME/lib/ojdbc14.jar:$IR4_HOME/lib/commons.jar:$IR4_HOME/lib/javamail.jar:$IR4_HOME/lib/m4_common.jar:$IR4_HOME/lib/m4_util.jar:$IR4_HOME/lib/pg74.214.jdbc2.jar:$IR4_HOME/lib/cubrid_jdbc.jar:$IR4_HOME/lib/jdom.jar:$IR4_HOME/lib/m4_core.jar:$IR4_HOME/lib/msbase.jar:$IR4_HOME/lib/sql_server.jar:$IR4_HOME/lib/db2java.jar:$IR4_HOME/lib/jiana1.5.jar:$IR4_HOME/lib/m4_extension.jar:$IR4_HOME/lib/mssqlserver.jar:$IR4_HOME/lib/sqljdbc.jar:$IR4_HOME/lib/db2jcc.jar:$IR4_HOME/lib/jtds.jar:$IR4_HOME/lib/m4_framework.jar:$IR4_HOME/lib/msutil.jar:$IR4_HOME/lib/tibero-jdbc.jar:$IR4_HOME/lib/Altibase5.jar"  com.diquest.ir.client.apps.IndexTaskCommander $IP $PORT $COLL $TYPE $OPTION $DBWATCHER

