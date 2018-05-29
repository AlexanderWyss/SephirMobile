package application.sephirmobile.sephirinterface.htmlparser;

@FunctionalInterface
public interface SimpleMapper<T> {

	void map(T t, String value);
}
