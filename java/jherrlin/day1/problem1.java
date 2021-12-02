import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

enum Direction {
	first,
	increased,
	decreased
}

record Measurement(Integer depth, Direction direction) {}

public class problem1 {
	static Function<String, String> readFile = (fileName) -> {
		try {
			File           file          = new File(fileName);
			BufferedReader br            = new BufferedReader(new FileReader(file));
			StringBuilder  stringBuilder = new StringBuilder();
			String         line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			return stringBuilder.toString();
		}
		catch (Exception e) {
			System.err.println(e);
			return "";
		}
	};

	static Function<String, Stream<String>> splitLines = (s)
		-> Stream.of(s.split("\\r?\\n"));

	static Function<Stream<String>, Stream<Integer>> toInt = (s1)
		-> s1.map(s2 -> Integer.parseInt(s2));

	static Function<Stream<Integer>, Stream<Measurement>> findMeasurement = (numbers) -> {
		ArrayList<Measurement> arrayList = new ArrayList<>();
		numbers.forEach(number -> {
			if (arrayList.isEmpty()) {
				arrayList.add(new Measurement(number, Direction.first));
			} else if (arrayList.get(arrayList.size() - 1).depth() < number) {
				arrayList.add(new Measurement(number, Direction.increased));
			} else {
				arrayList.add(new Measurement(number, Direction.decreased));
			}
		});
		return arrayList.stream();
	};

	static Function<Stream<Measurement>, Stream<Measurement>> filterIncreased = (measurements) ->
		measurements.filter(m -> m.direction() == Direction.increased);

	static Function<Stream<Measurement>, Stream<Integer>> extractDepths = (measurements) ->
		measurements.map(m -> m.depth());

	public static void main(String[] args) {
		var result = readFile.andThen(splitLines)
			.andThen(toInt)
			.andThen(findMeasurement)
			.andThen(filterIncreased)
			.andThen(extractDepths)
			.apply("./input.txt")
			.count();
		System.out.println(result);
	}
}
