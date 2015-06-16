import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.html.HTMLLayout
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import ch.qos.logback.classic.PatternLayout
import eip.smart.server.util.SlackAppender
import eip.smart.server.util.SlackFilter;
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.TRACE

scan()

appender("STDOUT", ConsoleAppender) {
	encoder(PatternLayoutEncoder) { pattern = "%date - %highlight(%-5level) %cyan([%class{20}:%method:%line]) - %msg%n" }
}

appender("FILE", FileAppender) {
	encoder(LayoutWrappingEncoder) {
		layout(HTMLLayout) { pattern = "%date%level%class{0}%method%line%msg" }
	}
	file = "log/test.html"
}

appender("SLACK", SlackAppender) {

	token = "xoxp-2904194704-2904194706-3291941732-31bd84"
	channel = "#logtest"
	filter(ThresholdFilter) { level = ERROR }
	filter(SlackFilter)
	layout(PatternLayout) { pattern = "%-4relative [%thread] %-5level %class - %msg%n" }
}

root(ALL, ["STDOUT", "FILE", "SLACK"])