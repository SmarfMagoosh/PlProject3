import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }


    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashSet<Integer> empty;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
        empty = new HashSet<Integer>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        if (is_start) {
            start_states.add(s);
        }
        if(is_accept) {
            accept_states.add(s);
        }
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair slp = new StateLabelPair(s_initial, label);
        if (transitions.containsKey(slp)) {
            transitions.get(slp).add(s_final);
        } else {
            HashSet<Integer> start = new HashSet<>();
            start.add(s_final);
            transitions.put(slp, start);
        }
    }

    @Override
    public void reset() {
        current_states = start_states;
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> next = new HashSet<Integer>();
        for (Integer i : current_states) {
            StateLabelPair slp = new StateLabelPair(i, input);
            for (Integer j : transitions.getOrDefault(slp, empty)) {
                next.add(j);
            }
        }
        current_states = next;
    }

    @Override
    public boolean accepts() {
        for (Integer i : current_states) {
            if (accept_states.contains(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        for (Integer i : current_states) {
            StateLabelPair slp = new StateLabelPair(i, label);
            if (transitions.containsKey(slp)) {
                return true;
            }
        }
        return false;
    }
}
