package nl.inl.blacklab.search.fimatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NfaStateOr extends NfaState {

	List<NfaState> nextStates;

	public NfaStateOr(NfaState... nextStates) {
		this.nextStates = new ArrayList<>(Arrays.asList(nextStates));
	}

	public NfaStateOr(List<NfaState> nextStates) {
		this.nextStates = new ArrayList<>(nextStates);
	}

	@Override
	public boolean findMatchesInternal(ForwardIndexDocument fiDoc, int pos, int direction, Set<Integer> matchEnds) {
		// OR/Split state. Find matches for all alternatives.
		boolean result = false;
		for (NfaState nextState: nextStates) {
			boolean matchesFound = false;
			if (nextState == null) {
				// null stands for the matching state.
				if (matchEnds != null)
					matchEnds.add(pos);
				matchesFound = true;
			} else {
				matchesFound = nextState.findMatchesInternal(fiDoc, pos, direction, matchEnds);
			}
			if (matchesFound && matchEnds == null)
				return true; // we don't care about the match ends, just that there are matches
			result |= matchesFound;
		}
		return result;
	}

	@Override
	void fillDangling(NfaState state) {
		for (int i = 0; i < nextStates.size(); i++) {
			if (nextStates.get(i) == null)
				nextStates.set(i, state);
		}
	}

	@Override
	NfaStateOr copyInternal(Collection<NfaState> dangling, Map<NfaState, NfaState> copiesMade) {
		NfaStateOr copy = new NfaStateOr();
		copiesMade.put(this, copy);
		List<NfaState> clauseCopies = new ArrayList<>();
		boolean hasNulls = false;
		for (NfaState nextState: nextStates) {
			if (nextState == null)
				hasNulls = true;
			else
				nextState = nextState.copy(dangling, copiesMade);
			clauseCopies.add(nextState);
		}
		copy.nextStates.addAll(clauseCopies);
		if (hasNulls)
			dangling.add(copy);
		return copy;
	}

	@Override
	public void setNextState(int input, NfaState state) {
		nextStates.set(input, state);
	}

	@Override
	public boolean matchesEmptySequence(Set<NfaState> statesVisited) {
		if (statesVisited.contains(this)) {
			// We've found a cycle. Stop processing, and just return the
			// "safest" (least-guarantee) answer. In this case: we can't
			// guarantee that this DOESN'T match the empty sequence.
			return true;
		}
		statesVisited.add(this);
		for (NfaState nextState: nextStates) {
			if (nextState.matchesEmptySequence(statesVisited))
				return true;
		}
		return false;
	}

	@Override
	public boolean hitsAllSameLength(Set<NfaState> statesVisited) {
		if (statesVisited.contains(this)) {
			// We've found a cycle. Stop processing, and just return the
			// "safest" (least-guarantee) answer. In this case: we can't
			// guarantee that hits are all the same length.
			return false;
		}
		statesVisited.add(this);
		int hitLength = -1;
		for (NfaState nextState: nextStates) {
			if (!nextState.hitsAllSameLength(statesVisited))
				return false;
			if (hitLength != -1 && hitLength != nextState.hitsLengthMin(statesVisited))
				return false;
			hitLength = nextState.hitsLengthMin(statesVisited);
		}
		return true;
	}

	@Override
	public int hitsLengthMin(Set<NfaState> statesVisited) {
		int hitLengthMin = Integer.MAX_VALUE;
		if (statesVisited.contains(this)) {
			// We've found a cycle. Stop processing, and just return the
			// "safest" (least-guarantee) answer. In this case: the smallest
			// hit might be 0 long.
			return 0;
		}
		statesVisited.add(this);
		for (NfaState nextState: nextStates) {
			int i = nextState.hitsLengthMin(statesVisited);
			if (i < hitLengthMin)
				hitLengthMin = i;
		}
		return hitLengthMin;
	}

	@Override
	public int hitsLengthMax(Set<NfaState> statesVisited) {
		int hitLengthMax = 0;
		if (statesVisited.contains(this)) {
			// We've found a cycle. Stop processing, and just return the
			// "safest" (least-guarantee) answer. In this case: the largest
			// hit might be "infinitely" large.
			return Integer.MAX_VALUE;
		}
		statesVisited.add(this);
		for (NfaState nextState: nextStates) {
			int i = nextState.hitsLengthMax(statesVisited);
			if (i > hitLengthMax)
				hitLengthMax = i;
		}
		return hitLengthMax;
	}

	@Override
	protected String dumpInternal(Map<NfaState, Integer> stateNrs) {
		StringBuilder b = new StringBuilder();
		for (NfaState s: nextStates) {
			if (b.length() > 0)
				b.append(",");
			b.append(dump(s, stateNrs));
		}
		return "OR(" + b.toString() + ")";
	}

}
