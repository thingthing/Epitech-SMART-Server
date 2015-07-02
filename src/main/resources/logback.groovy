import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.html.HTMLLayout
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.classic.PatternLayout
import eip.smart.server.util.SlackAppender
import eip.smart.server.util.ConfigFilter;
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.TRACE

scan()

def CATALINA_BASE = System.getProperty("catalina.base")
def LOG_FOLDER = "${CATALINA_BASE}/log"

appender("STDOUT", ConsoleAppender) {
	encoder(PatternLayoutEncoder) { pattern = "%date - %highlight(%-5level) %cyan([%class{0}.%method:%line]) - %msg%n" }
}

appender("FILE", RollingFileAppender) {
	file = "${LOG_FOLDER}/log.html"
	rollingPolicy(TimeBasedRollingPolicy) {
		fileNamePattern = "${LOG_FOLDER}/%d{yyyy-MM,aux}/%d.log.html"
		maxHistory = 3
		cleanHistoryOnStart = true
	}
	encoder(LayoutWrappingEncoder) {
		layout(HTMLLayout) { pattern = "%date%level%class{0}%method%line%msg" }
	}
}

appender("SLACK", SlackAppender) {

	token = "xoxp-2904194704-2904194706-3291941732-31bd84"
	channel = "#team-server"
	username = "ServerLog"
	filter(ThresholdFilter) { level = ERROR }
	filter(ConfigFilter) {
		file = "logging"
		key = "LOGGING_SLACK"
		value = "TRUE"
	}
}

root(ALL, ["STDOUT", "FILE", "SLACK"])