package jmh;

import org.openjdk.jmh.annotations.*;

/**
 * @author WangBei
 * @since 2015/3/18
 */
@Fork(2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JMH_StringSplit {
	public static final String data = "ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,ajfidpaj3214ip,fdsaipjigdasf,fdsajijgi,dafaf,fa,aga,a";

	@Benchmark
	public String[] measureJavaSplit() {
		return data.split(",");
	}

	@Benchmark
	public String[] measureStringUtils() {
		return org.apache.commons.lang3.StringUtils.split(data, ',');
	}
}
