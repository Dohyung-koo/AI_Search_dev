# ========================================================================
# CommandDrama : excute drama index
# [IP] [MERGE_IN_DIR] [PORT] [COL]
# ========================================================================

IP=localhost
PORT=5555
COL=UFLIX_TVG_KEYWORD
java -classpath "$IR4_HOME/lib/chiana.jar:$IR4_HOME/lib/dqdic-1.0.3.jar:$IR4_HOME/lib/jwsdp.jar:$IR4_HOME/lib/m4_mgr.jar:$IR4_HOME/lib/mysql-connector-java-ga-bin.jar:$IR4_HOME/lib/xerces.jar:$IR4_HOME/lib/informix.jar:$IR4_HOME/lib/m4_client.jar:$IR4_HOME/lib/m4_server.jar:$IR4_HOME/lib/ojdbc14.jar:$IR4_HOME/lib/commons.jar:$IR4_HOME/lib/javamail.jar:$IR4_HOME/lib/m4_common.jar:$IR4_HOME/lib/m4_util.jar:$IR4_HOME/lib/pg74.214.jdbc2.jar:$IR4_HOME/lib/cubrid_jdbc.jar:$IR4_HOME/lib/jdom.jar:$IR4_HOME/lib/m4_core.jar:$IR4_HOME/lib/msbase.jar:$IR4_HOME/lib/sql_server.jar:$IR4_HOME/lib/db2java.jar:$IR4_HOME/lib/jiana1.5.jar:$IR4_HOME/lib/m4_extension.jar:$IR4_HOME/lib/mssqlserver.jar:$IR4_HOME/lib/sqljdbc.jar:$IR4_HOME/lib/db2jcc.jar:$IR4_HOME/lib/jtds.jar:$IR4_HOME/lib/m4_framework.jar:$IR4_HOME/lib/msutil.jar:$IR4_HOME/lib/tibero-jdbc.jar:$IR4_HOME/lib/Altibase5.jar:$IR4_HOME/lib/drama.jar" com.diquest.ir.extension.drama.command.CommandDrama $IP $PORT $COL
