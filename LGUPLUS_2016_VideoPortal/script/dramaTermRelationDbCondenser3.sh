# ========================================================================
# TermRelationCondenser : will remove term relation rows that don't change(insert/delete/update).
#                                                       Thus, TermRelationCondenser will delete rows where FLAG_ACT is equal to 'N'.
# [IP] [PORT] [collectionID] [indexSettingID] [OPTION]
# IP, PORT :  admin server IP, PORT
# collectionID : Target collection ID
# indexSettingID : Target index setting ID
# OPTION : CO, MI, XQ, ALL
#               CO - will delete rows where "CO" value of OPTION column
#               MI - will delete rows where "MI" value of OPTION column
#               XQ - will delete rows where "XQ" value of OPTION column
#               ALL - will delete rows that don't care the value of OPTION column
# ========================================================================

ADMIN_IP=localhost
ADMIN_PORT=5555
COLLECTION_ID=UFLIX_TVG_KEYWORD
INDEX_SETTING_ID=IDX_KEYWORD_RELATION
REL_OPTION=ALL

java -Xmx256M -classpath .:$IR4_HOME/lib/drama.jar:$IR4_HOME/lib/m4_common.jar:$IR4_HOME/lib/m4_client.jar:$IR4_HOME/lib/m4_util.jar:$IR4_HOME/lib/commons.jar:$IR4_HOME/lib/mysql-connector-java-ga-bin.jar:$IR4_HOME/lib/informix.jar:$IR4_HOME/lib/ojdbc14.jar:$IR4_HOME/lib/pg74.214.jdbc2.jar:$IR4_HOME/lib/cubrid_jdbc.jar:$IR4_HOME/lib/msbase.jar:$IR4_HOME/lib/sql_server.jar:$IR4_HOME/lib/db2java.jar:$IR4_HOME/lib/mssqlserver.jar:$IR4_HOME/lib/sqljdbc.jar:$IR4_HOME/lib/db2jcc.jar:$IR4_HOME/lib/jtds.jar:$IR4_HOME/lib/msutil.jar:$IR4_HOME/lib/tibero-jdbc.jar:$IR4_HOME/lib/Altibase5.jar:$IR4_HOME/lib/mariadb-java-client-1.1.3.jar com.diquest.ir.extension.drama.util.rdbms.TermRelationCondenser $ADMIN_IP $ADMIN_PORT $COLLECTION_ID $INDEX_SETTING_ID $REL_OPTION

