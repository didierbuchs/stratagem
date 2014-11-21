/*
 * generated by Xtext
 */
package ch.unige.cui.smv.stratagem.xtext.validation

import ch.unige.cui.smv.metamodel.ts.All
import ch.unige.cui.smv.metamodel.ts.Choice
import ch.unige.cui.smv.metamodel.ts.DeclaredStrategy
import ch.unige.cui.smv.metamodel.ts.DeclaredStrategyInstance
import ch.unige.cui.smv.metamodel.ts.FixPointStrategy
import ch.unige.cui.smv.metamodel.ts.IfThenElse
import ch.unige.cui.smv.metamodel.ts.NonVariableStrategy
import ch.unige.cui.smv.metamodel.ts.Not
import ch.unige.cui.smv.metamodel.ts.One
import ch.unige.cui.smv.metamodel.ts.Saturation
import ch.unige.cui.smv.metamodel.ts.Sequence
import ch.unige.cui.smv.metamodel.ts.TsPackage
import ch.unige.cui.smv.metamodel.ts.Union
import ch.unige.cui.smv.metamodel.ts.VariableStrategy
import ch.unige.smv.cui.metamodel.adt.AdtPackage
import ch.unige.smv.cui.metamodel.adt.Equation
import ch.unige.smv.cui.metamodel.adt.Term
import ch.unige.smv.cui.metamodel.adt.Variable
import ch.unige.smv.cui.metamodel.adt.VariableDeclaration
import java.util.HashSet
import java.util.Set
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.validation.Check

//import org.eclipse.xtext.validation.Check
/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class TransitionSystemDslValidator extends AbstractTransitionSystemDslValidator {
	@Check
	def checkForNonLinearRules(Equation eq) {
		checkNoNonLinearRules(eq.leftHandTerm, new HashSet<VariableDeclaration>(), eq,
			AdtPackage.Literals.EQUATION__LEFT_HAND_TERM)
		checkNoNonLinearRules(eq.rightHandTerm, new HashSet<VariableDeclaration>(), eq,
			AdtPackage.Literals.EQUATION__RIGHT_HAND_TERM)
	}

	def dispatch checkNoNonLinearRules(Variable v, Set<VariableDeclaration> varSet, Equation eq, EReference ref) {
		if (varSet.contains(v.declaration)) {
			error("Non linear rules are not allowed in equations: " + eq, ref)
		} else {
			varSet.add(v.declaration)
		}
	}

	def dispatch checkNoNonLinearRules(Term t, Set<VariableDeclaration> varSet, Equation eq, EReference ref) {
		for (subterm : t.subterms) {
			checkNoNonLinearRules(subterm, varSet, eq, ref)
		}
	}

	@Check
	def checkDeclaredStrategyUsesOnlyDefinedVariablesAndNoNotIsBadlyUsed(DeclaredStrategy ds) {
		val setVariableStratNames = ds.formalParams.map[variableStrat|variableStrat.name].toSet
		if (ds.body != null) {
			checkStratForRightVariableNames(ds.body, setVariableStratNames)
		}
	}

	def dispatch checkStratForRightVariableNames(DeclaredStrategyInstance strat, Set<String> variableNames) {
		for (actualParam : strat.actualParams) {
			checkStratForRightVariableNames(actualParam, variableNames)
		}
	}

	def dispatch checkStratForRightVariableNames(FixPointStrategy strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s, variableNames)
	}

	def dispatch checkStratForRightVariableNames(Not strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s, variableNames)
	}

	def dispatch checkStratForRightVariableNames(One strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s, variableNames)
	}
	
	def dispatch checkStratForRightVariableNames(All strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s, variableNames)
	}

	def dispatch checkStratForRightVariableNames(Saturation strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s, variableNames)
	}

	def dispatch checkStratForRightVariableNames(Sequence strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s1, variableNames)
		checkStratForRightVariableNames(strat.s2, variableNames)
	}

	def dispatch checkStratForRightVariableNames(Union strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s1, variableNames)
		checkStratForRightVariableNames(strat.s2, variableNames)
	}

	def dispatch checkStratForRightVariableNames(IfThenElse strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s1, variableNames)
		checkStratForRightVariableNames(strat.s2, variableNames)
		checkStratForRightVariableNames(strat.s3, variableNames)
	}

	def dispatch checkStratForRightVariableNames(Choice strat, Set<String> variableNames) {
		checkStratForRightVariableNames(strat.s1, variableNames)
		checkStratForRightVariableNames(strat.s2, variableNames)
	}

	def dispatch checkStratForRightVariableNames(NonVariableStrategy strat, Set<String> variableNames) {
		// nothing
	}

	def dispatch checkStratForRightVariableNames(VariableStrategy v, Set<String> variableNames) {
		if (!variableNames.contains(v.name)) {
			error("Strategy variable name \'" + v.name + "\' is not in declaration. If you wanted to use a declared strategy you need to append parentheses to it, like this: " + v.name + "()",
				TsPackage.Literals.DECLARED_STRATEGY__BODY)
		}
	}

//  public static val INVALID_NAME = 'invalidName'
//
//	@Check
//	def checkGreetingStartsWithCapital(Greeting greeting) {
//		if (!Character.isUpperCase(greeting.name.charAt(0))) {
//			warning('Name should start with a capital', 
//					MyDslPackage.Literals.GREETING__NAME,
//					INVALID_NAME)
//		}
//	}
}
