# ========================================================================
# FileToDb : lgUplus Data file to DB
# [BUILDER_HOME] [workOption(ALL - All work, V - Video, VA - Video AutoComplete, U - Uflix, UA - Uflix AutoComplete)]
# ========================================================================

BUILDER_HOME=/diquest/mariner4/LGUPLUS_VIDEO_BUILDER/
WORK_OPTION=ALL

java -Xms2g -Xmx2g -classpath ".:$BUILDER_HOME/lib/videoBuilder.jar:$BUILDER_HOME/lib/com.google.guava_1.6.0.jar:$BUILDER_HOME/lib/commons.jar:$BUILDER_HOME/lib/dq_util.jar:$BUILDER_HOME/lib/mysql-connector-java-ga-bin.jar:$BUILDER_HOME/lib/activation.jar:$BUILDER_HOME/lib/tar.jar" com.diquest.app.FileToDb $BUILDER_HOME $WORK_OPTION
