# ========================================================================
# LogMaker : make LGUPLUS IS, MARINER log
# [BUILDER_HOME]
# ========================================================================

BUILDER_HOME=/diquest/mariner4/LGUPLUS_VIDEO_BUILDER/
java -Xms2g -Xmx2g -classpath ".:$BUILDER_HOME/lib/videoBuilder.jar:$BUILDER_HOME/lib/commons.jar:$BUILDER_HOME/lib/dq_util.jar:$BUILDER_HOME/lib/mysql-connector-java-ga-bin.jar" com.diquest.app.LogMaker $BUILDER_HOME
