package no.nav.fpsak.nare.doc;

import java.util.List;

import no.nav.fpsak.nare.evaluation.Operator;

public interface RuleDescription {

    Operator getOperator();

    RuleDescription firstChild();

    RuleDescription secondChild();

    String getRuleDescription();

    String getRuleIdentification();

    List<RuleDescription> getChildren();

    RuleDescription lastChild();

    boolean hasChildren();

}