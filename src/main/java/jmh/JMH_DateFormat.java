package jmh;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.openjdk.jmh.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WangBei
 * @since 2015/3/18
 */
@Fork(2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JMH_DateFormat {

	@Benchmark
	public String measurFormatSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	@Benchmark
	public String measureFormatJodaTime() {
		return new DateTime().toString("yyyy-MM-dd HH:mm:ss");
	}

	@Benchmark
	public Date measurParseSimpleDateFormat() throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-03-15 12:05:33");
	}

	@Benchmark
	public DateTime measureParseJodaTime() {
		return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2015-03-15 12:05:33");
	}
}
