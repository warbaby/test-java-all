package jmh;

import org.jcodings.specific.UTF8Encoding;
import org.joni.Option;
import org.joni.Regex;
import org.openjdk.jmh.annotations.*;

import java.util.regex.Pattern;

/**
 * @author WangBei
 * @since 2015/3/18
 */
@Fork(2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JMH_RegularExpression {
	public static final String data = "ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,a";
	public static final String regex = "abi([0-9A-Z]*)";

	@Benchmark
	public boolean measureJavaMatches() {
		return data.matches(regex);
	}

	@State(Scope.Benchmark)
	public static class PrecompilePattern {
		Pattern pattern = Pattern.compile(regex);
	}
	@Benchmark
	public boolean measurePrecompile(PrecompilePattern state) {
		return state.pattern.matcher(data).matches();
	}

	@State(Scope.Benchmark)
	public static class StateRe2j {
		com.google.re2j.Pattern pattern = com.google.re2j.Pattern.compile(regex);
	}
	@Benchmark
	public boolean measureRe2j(StateRe2j state) {
		return state.pattern.matches(data);
	}

	@State(Scope.Benchmark)
	public static class StateJoni {
		Regex reg = new Regex(regex.getBytes(), 0, regex.length(), Option.NONE, UTF8Encoding.INSTANCE);
	}
	@Benchmark
	public int measureJoni(StateJoni state) {
		return state.reg.matcher(data.getBytes()).search(0, data.length(), Option.DEFAULT);
	}
}
