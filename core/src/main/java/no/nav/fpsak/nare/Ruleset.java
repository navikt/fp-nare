package no.nav.fpsak.nare;

import no.nav.fpsak.nare.specification.Specification;

public class Ruleset {

	public <V> Specification<V> regel(String id, String beskrivelse, Specification<V> specification) {
		return specification.medBeskrivelse(beskrivelse).medID(id);
	}

	public <V> Specification<V> regel(String id, Specification<V> specification) {
		return specification.medID(id);
	}

}
