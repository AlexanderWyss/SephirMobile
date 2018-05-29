package application.sephirmobile.sephirinterface.htmlparser;

@FunctionalInterface
public interface Mapper<T> {

	void map(T t, String value);
}
