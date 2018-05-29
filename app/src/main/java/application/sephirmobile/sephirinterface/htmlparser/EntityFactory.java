package application.sephirmobile.sephirinterface.htmlparser;

@FunctionalInterface
public interface EntityFactory<T> {

	T build();

}
