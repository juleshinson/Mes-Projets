import labyrinth.util.observable.SimpleObservableValue;
public class TestLib {
    public static void main(String[] args) {
        SimpleObservableValue<Integer> obs = new SimpleObservableValue<>(0);
        obs.addListener(observable -> System.out.printf("value = %d.%n", obs.getValue()));
        obs.setValue(42);
    }
}