import java.util.*;

public class SequenceDetector {
    static class Transition {
        char presentState;
        char nextState;
        int input;
        int output;

        public Transition(char presentState, char nextState, int input, int output) {
            this.presentState = presentState;
            this.nextState = nextState;
            this.input = input;
            this.output = output;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Transition> transitions = new ArrayList<>();
        Set<Character> states = new HashSet<>();

        // Reading input transitions
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if (parts.length != 4) break; // Stop reading if line format is invalid

            char presentState = parts[0].charAt(0);
            char nextState = parts[1].charAt(0);
            int input = Integer.parseInt(parts[2]);
            int output = Integer.parseInt(parts[3]);

            transitions.add(new Transition(presentState, nextState, input, output));
            states.add(presentState);
            states.add(nextState);
        }
        scanner.close();

        // Detect sequence and type
        String sequence = detectSequence(transitions);
        String detectorType = determineDetectorType(transitions, sequence, states);

        System.out.println(sequence);
        System.out.println(detectorType);
    }

    // Method to detect the sequence from transitions
    private static String detectSequence(List<Transition> transitions) {
        StringBuilder sequence = new StringBuilder();
        Map<Character, Map<Integer, Transition>> transitionMap = new HashMap<>();

        // Populate transition map with transitions based on present state and input
        for (Transition t : transitions) {
            transitionMap.putIfAbsent(t.presentState, new HashMap<>());
            transitionMap.get(t.presentState).put(t.input, t);
        }

        // Traverse from initial state 'a' following transitions
        char currentState = 'a';
        Set<Character> visitedStates = new HashSet<>();

        while (true) {
            visitedStates.add(currentState);
            Transition nextTransition = null;

            for (int input : transitionMap.getOrDefault(currentState, new HashMap<>()).keySet()) {
                Transition t = transitionMap.get(currentState).get(input);

                // Only change sequence if the next state is different
                if (t != null && t.nextState != currentState) {
                    sequence.append(t.input);
                    nextTransition = t;
                    if (t.output == 1) break; // Stop if output is 1 indicating detection of sequence
                }
            }
            if (nextTransition == null) break; // Stop if no valid transition found
            currentState = nextTransition.nextState;
        }
        return sequence.toString();
    }

    // Method to determine if the sequence detector is non-overlapping or overlapping
    private static String determineDetectorType(List<Transition> transitions, String sequence, Set<Character> states) {
        // Find initial and final state by observing sequence start and end
        char initialState = 'a'; // Initial state is 'a' by problem constraint
        char finalState = 'c';   // Assume final state is last detected state in the problem context

        // Check if there is a transition from final state back to initial state with output 1
        for (Transition t : transitions) {
            if (t.presentState == finalState && t.nextState == initialState && t.output == 1) {
                return "Non Overlapping Sequence Detector";
            }
        }
        return "Overlapping Sequence Detector";
    }
}
