package application.sephirmobile.sephirinterface;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class StringFromResourceReader {
	public static String read(String path) throws IOException, URISyntaxException {
		return Files.readAllLines(Paths.get(StringFromResourceReader.class.getClassLoader().getResource(path).toURI()))
				.stream().collect(Collectors.joining());
	}
}
