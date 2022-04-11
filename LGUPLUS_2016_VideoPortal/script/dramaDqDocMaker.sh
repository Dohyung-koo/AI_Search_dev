# ========================================================================
# dramaDqDocMaker : make Drama DqDoc file use lgUplus metaData
# [CONFPATH]
# ========================================================================

CONFPATH=$IR4_HOME/LGUPLUS_VIDEO_BUILDER/conf/
java -Xms2g -Xmx2g -classpath ".:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/dqdic-1.0.3.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/jiana1.5.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/jiana-common-result.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/log4j-1.2.14.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/plot2.3.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/videoBuilder.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/com.google.guava_1.6.0.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/commons.jar:$IR4_HOME/lib/dq_util.jar:$IR4_HOME/LGUPLUS_VIDEO_BUILDER/lib/mysql-connector-java-ga-bin.jar" com.diquest.app.DramaDqDocMaker $CONFPATH
