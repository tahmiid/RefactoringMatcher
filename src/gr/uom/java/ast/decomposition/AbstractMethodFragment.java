package gr.uom.java.ast.decomposition;

import gr.uom.java.ast.Access;
import gr.uom.java.ast.AnonymousClassDeclarationObject;
import gr.uom.java.ast.ArrayCreationObject;
import gr.uom.java.ast.ClassInstanceCreationObject;
import gr.uom.java.ast.ConstructorInvocationObject;
import gr.uom.java.ast.ConstructorObject;
import gr.uom.java.ast.CreationObject;
import gr.uom.java.ast.FieldInstructionObject;
import gr.uom.java.ast.FieldObject;
import gr.uom.java.ast.LiteralObject;
import gr.uom.java.ast.LocalVariableDeclarationObject;
import gr.uom.java.ast.LocalVariableInstructionObject;
import gr.uom.java.ast.MethodInvocationObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.ParameterObject;
import gr.uom.java.ast.SuperFieldInstructionObject;
import gr.uom.java.ast.SuperMethodInvocationObject;
import gr.uom.java.ast.TypeObject;
import gr.uom.java.ast.decomposition.cfg.AbstractVariable;
import gr.uom.java.ast.decomposition.cfg.PlainVariable;
import gr.uom.java.ast.util.MethodDeclarationUtility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public abstract class AbstractMethodFragment {
	private AbstractMethodFragment parent;

	/*private List<FieldInstructionObject> fieldInstructionList;
	private List<SuperFieldInstructionObject> superFieldInstructionList;*/
	private List<LocalVariableDeclarationObject> localVariableDeclarationList;
	private List<LocalVariableInstructionObject> localVariableInstructionList;
	private List<CreationObject> creationList;
	private List<LiteralObject> literalList;
	private List<AnonymousClassDeclarationObject> anonymousClassDeclarationList;
	private Set<String> exceptionsInThrowStatements;

/*	private List<AbstractVariable> nonDistinctDefinedFieldsThroughFields;
	private List<AbstractVariable> nonDistinctUsedFieldsThroughFields;
	private List<AbstractVariable> nonDistinctDefinedFieldsThroughParameters;
	private List<AbstractVariable> nonDistinctUsedFieldsThroughParameters;
	private List<AbstractVariable> nonDistinctDefinedFieldsThroughLocalVariables;
	private List<AbstractVariable> nonDistinctUsedFieldsThroughLocalVariables;
	private List<PlainVariable> nonDistinctDefinedFieldsThroughThisReference;
	private List<PlainVariable> nonDistinctUsedFieldsThroughThisReference;*/

	private Set<PlainVariable> declaredLocalVariables;
	private Set<PlainVariable> definedLocalVariables;
	private Set<PlainVariable> usedLocalVariables;
	private Map<PlainVariable, LinkedHashSet<MethodInvocationObject>> parametersPassedAsArgumentsInMethodInvocations;
	private Map<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>> parametersPassedAsArgumentsInSuperMethodInvocations;
	private Map<PlainVariable, LinkedHashSet<ConstructorInvocationObject>> parametersPassedAsArgumentsInConstructorInvocations;
	private Map<PlainVariable, LinkedHashSet<ClassInstanceCreationObject>> variablesAssignedWithClassInstanceCreations;

	protected AbstractMethodFragment(AbstractMethodFragment parent) {
		this.parent = parent;
/*		this.fieldInstructionList = new ArrayList<FieldInstructionObject>();
		this.superFieldInstructionList = new ArrayList<SuperFieldInstructionObject>();*/
		this.localVariableDeclarationList = new ArrayList<LocalVariableDeclarationObject>();
		this.localVariableInstructionList = new ArrayList<LocalVariableInstructionObject>();
		this.creationList = new ArrayList<CreationObject>();
		this.literalList = new ArrayList<LiteralObject>();
		this.anonymousClassDeclarationList = new ArrayList<AnonymousClassDeclarationObject>();
		this.exceptionsInThrowStatements = new LinkedHashSet<String>();

/*		this.nonDistinctDefinedFieldsThroughFields = new ArrayList<AbstractVariable>();
		this.nonDistinctUsedFieldsThroughFields = new ArrayList<AbstractVariable>();
		this.nonDistinctDefinedFieldsThroughParameters = new ArrayList<AbstractVariable>();
		this.nonDistinctUsedFieldsThroughParameters = new ArrayList<AbstractVariable>();
		this.nonDistinctDefinedFieldsThroughLocalVariables = new ArrayList<AbstractVariable>();
		this.nonDistinctUsedFieldsThroughLocalVariables = new ArrayList<AbstractVariable>();
		this.nonDistinctDefinedFieldsThroughThisReference = new ArrayList<PlainVariable>();
		this.nonDistinctUsedFieldsThroughThisReference = new ArrayList<PlainVariable>();*/

		this.declaredLocalVariables = new LinkedHashSet<PlainVariable>();
		this.definedLocalVariables = new LinkedHashSet<PlainVariable>();
		this.usedLocalVariables = new LinkedHashSet<PlainVariable>();
		this.parametersPassedAsArgumentsInMethodInvocations = new LinkedHashMap<PlainVariable, LinkedHashSet<MethodInvocationObject>>();
		this.parametersPassedAsArgumentsInSuperMethodInvocations = new LinkedHashMap<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>>();
		this.parametersPassedAsArgumentsInConstructorInvocations = new LinkedHashMap<PlainVariable, LinkedHashSet<ConstructorInvocationObject>>();
		this.variablesAssignedWithClassInstanceCreations = new LinkedHashMap<PlainVariable, LinkedHashSet<ClassInstanceCreationObject>>();
	}

	public AbstractMethodFragment getParent() {
		return this.parent;
	}

/*	protected void processVariables(List<Expression> variableInstructions, List<Expression> assignments,
			List<Expression> postfixExpressions, List<Expression> prefixExpressions) {
		for (Expression variableInstruction : variableInstructions) {
			SimpleName simpleName = (SimpleName) variableInstruction;
			IBinding binding = simpleName.resolveBinding();
			if (binding != null && binding.getKind() == IBinding.VARIABLE) {
				IVariableBinding variableBinding = (IVariableBinding) binding;
				if (variableBinding.isField()) {
					// field
					if (variableBinding.getDeclaringClass() != null) {
						String originClassName = variableBinding.getDeclaringClass().getQualifiedName();
						String qualifiedName = variableBinding.getType().getQualifiedName();
						TypeObject fieldType = TypeObject.extractTypeObject(qualifiedName);
						String fieldName = variableBinding.getName();
						if (!originClassName.equals("")) {
							if (simpleName.getParent() instanceof SuperFieldAccess) {
								// from super class
								SuperFieldInstructionObject superFieldInstruction = new SuperFieldInstructionObject(
										originClassName, fieldType, fieldName);
								superFieldInstruction.setSimpleName(simpleName);
								if ((variableBinding.getModifiers() & Modifier.STATIC) != 0)
									superFieldInstruction.setStatic(true);
								addSuperFieldInstruction(superFieldInstruction);
							} else {
								// from same class
								FieldInstructionObject fieldInstruction = new FieldInstructionObject(originClassName,
										fieldType, fieldName);
								fieldInstruction.setSimpleName(simpleName);
								if ((variableBinding.getModifiers() & Modifier.STATIC) != 0)
									fieldInstruction.setStatic(true);
								addFieldInstruction(fieldInstruction);
								Set<Assignment> fieldAssignments = getMatchingAssignments(simpleName, assignments);
								Set<PostfixExpression> fieldPostfixAssignments = getMatchingPostfixAssignments(
										simpleName, postfixExpressions);
								Set<PrefixExpression> fieldPrefixAssignments = getMatchingPrefixAssignments(simpleName,
										prefixExpressions);
								AbstractVariable variable = MethodDeclarationUtility.createVariable(simpleName, null);
								if (!fieldAssignments.isEmpty()) {
									handleDefinedField(variable);
									for (Assignment assignment : fieldAssignments) {
										Assignment.Operator operator = assignment.getOperator();
										if (!operator.equals(Assignment.Operator.ASSIGN))
											handleUsedField(variable);
									}
								}
								if (!fieldPostfixAssignments.isEmpty()) {
									handleDefinedField(variable);
									handleUsedField(variable);
								}
								if (!fieldPrefixAssignments.isEmpty()) {
									handleDefinedField(variable);
									handleUsedField(variable);
								}
								if (fieldAssignments.isEmpty() && fieldPostfixAssignments.isEmpty()
										&& fieldPrefixAssignments.isEmpty()) {
									handleUsedField(variable);
								}
							}
						}
					}
				} else {
					// local variable
					if (variableBinding.getDeclaringClass() == null) {
						String variableName = variableBinding.getName();
						String variableType = variableBinding.getType().getQualifiedName();
						TypeObject localVariableType = TypeObject.extractTypeObject(variableType);
						PlainVariable variable = new PlainVariable(variableBinding);
						if (simpleName.isDeclaration()) {
							// declaration
							LocalVariableDeclarationObject localVariable = new LocalVariableDeclarationObject(
									localVariableType, variableName);
							VariableDeclaration variableDeclaration = (VariableDeclaration) simpleName.getParent();
							localVariable.setVariableDeclaration(variableDeclaration);
							addLocalVariableDeclaration(localVariable);
							addDeclaredLocalVariable(variable);
						} else {
							// instruction
							LocalVariableInstructionObject localVariable = new LocalVariableInstructionObject(
									localVariableType, variableName);
							localVariable.setSimpleName(simpleName);
							addLocalVariableInstruction(localVariable);
							Set<Assignment> localVariableAssignments = getMatchingAssignments(simpleName, assignments);
							Set<PostfixExpression> localVariablePostfixAssignments = getMatchingPostfixAssignments(
									simpleName, postfixExpressions);
							Set<PrefixExpression> localVariablePrefixAssignments = getMatchingPrefixAssignments(
									simpleName, prefixExpressions);
							if (!localVariableAssignments.isEmpty()) {
								addDefinedLocalVariable(variable);
								for (Assignment assignment : localVariableAssignments) {
									Assignment.Operator operator = assignment.getOperator();
									if (!operator.equals(Assignment.Operator.ASSIGN))
										addUsedLocalVariable(variable);
								}
							}
							if (!localVariablePostfixAssignments.isEmpty()) {
								addDefinedLocalVariable(variable);
								addUsedLocalVariable(variable);
							}
							if (!localVariablePrefixAssignments.isEmpty()) {
								addDefinedLocalVariable(variable);
								addUsedLocalVariable(variable);
							}
							if (localVariableAssignments.isEmpty() && localVariablePostfixAssignments.isEmpty()
									&& localVariablePrefixAssignments.isEmpty()) {
								addUsedLocalVariable(variable);
							}
						}
					}
				}
			}
		}
	}*/

	protected void processVariablesWithoutBindingInfo(List<Expression> variableInstructions,
			List<Expression> assignments, List<Expression> postfixExpressions, List<Expression> prefixExpressions) {
		for (Expression variableInstruction : variableInstructions) {
			SimpleName simpleName = (SimpleName) variableInstruction;	
			String variableName = simpleName.getIdentifier();
				
			if (simpleName.isDeclaration()) {
				// declaration
				TypeObject type = null;
				ASTNode scope = null;
				VariableDeclaration variableDeclaration = (VariableDeclaration) simpleName.getParent();
				PlainVariable variable = new PlainVariable(variableDeclaration);
				if(variableDeclaration instanceof VariableDeclarationFragment)
				{
					ASTNode variableDeclarationParent = variableDeclaration.getParent();						if(variableDeclarationParent instanceof VariableDeclarationStatement)
					{
						VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement) variableDeclarationParent; 
						type = new TypeObject(variableDeclarationStatement.getType().toString());
						scope =  variableDeclarationStatement.getParent();
					} else if(variableDeclarationParent instanceof VariableDeclarationExpression){							
						type = new TypeObject( ((VariableDeclarationExpression)variableDeclarationParent).getType().toString() ) ;
						if(variableDeclarationParent.getParent() instanceof ForStatement) 
							scope = variableDeclarationParent.getParent();
						else {
							throw new NullPointerException();
						}						} else {
						throw new NullPointerException();
					}
				}
				else if (variableDeclaration instanceof SingleVariableDeclaration)
				{
					type = new TypeObject( ((SingleVariableDeclaration)variableDeclaration).getType().toString() ) ;						scope = variableDeclaration.getParent();					
				} 
				LocalVariableDeclarationObject localVariable = new LocalVariableDeclarationObject(type, variableName, scope);	
				localVariable.setVariableDeclaration(variableDeclaration);
				addLocalVariableDeclaration(localVariable);		
				addDeclaredLocalVariable(variable);
			} else{
				// instruction
				LocalVariableInstructionObject localVariable = new LocalVariableInstructionObject(variableName);
				localVariable.setSimpleName(simpleName);
				addLocalVariableInstruction(localVariable);
				PlainVariable variable = new PlainVariable(variableName);
				
				Set<Assignment> localVariableAssignments = getMatchingAssignments(simpleName, assignments);
				Set<PostfixExpression> localVariablePostfixAssignments = getMatchingPostfixAssignments(
						simpleName, postfixExpressions);
				Set<PrefixExpression> localVariablePrefixAssignments = getMatchingPrefixAssignments(
						simpleName, prefixExpressions);
				if (!localVariableAssignments.isEmpty()) {
					addDefinedLocalVariable(variable);
					for (Assignment assignment : localVariableAssignments) {
						Assignment.Operator operator = assignment.getOperator();
						if (!operator.equals(Assignment.Operator.ASSIGN))
							addUsedLocalVariable(variable);
					}
				}
				if (!localVariablePostfixAssignments.isEmpty()) {
					addDefinedLocalVariable(variable);
					addUsedLocalVariable(variable);
				}
				if (!localVariablePrefixAssignments.isEmpty()) {
					addDefinedLocalVariable(variable);
					addUsedLocalVariable(variable);
				}
				if (localVariableAssignments.isEmpty() && localVariablePostfixAssignments.isEmpty()
						&& localVariablePrefixAssignments.isEmpty()) {
					addUsedLocalVariable(variable);
				}
				
			}
			
		}
	}

/*	private void addFieldInstruction(FieldInstructionObject fieldInstruction) {
		fieldInstructionList.add(fieldInstruction);
		if (parent != null) {
			parent.addFieldInstruction(fieldInstruction);
		}
	}

	private void addSuperFieldInstruction(SuperFieldInstructionObject superFieldInstruction) {
		superFieldInstructionList.add(superFieldInstruction);
		if (parent != null) {
			parent.addSuperFieldInstruction(superFieldInstruction);
		}
	}*/

	private void addLocalVariableDeclaration(LocalVariableDeclarationObject localVariable) {
		localVariableDeclarationList.add(localVariable);
		if (parent != null) {
			parent.addLocalVariableDeclaration(localVariable);
		}
	}

	private void addLocalVariableInstruction(LocalVariableInstructionObject localVariable) {
		localVariableInstructionList.add(localVariable);
		if (parent != null) {
			parent.addLocalVariableInstruction(localVariable);
		}
	}

	private void addDeclaredLocalVariable(PlainVariable variable) {
		declaredLocalVariables.add(variable);
		if (parent != null) {
			parent.addDeclaredLocalVariable(variable);
		}
	}

	private void addDefinedLocalVariable(PlainVariable variable) {
		definedLocalVariables.add(variable);
		if (parent != null) {
			parent.addDefinedLocalVariable(variable);
		}
	}

	private void addUsedLocalVariable(PlainVariable variable) {
		usedLocalVariables.add(variable);
		if (parent != null) {
			parent.addUsedLocalVariable(variable);
		}
	}

	protected void processArrayCreations(List<Expression> arrayCreations) {
		for (Expression arrayCreationExpression : arrayCreations) {
			ArrayCreation arrayCreation = (ArrayCreation) arrayCreationExpression;
			Type type = arrayCreation.getType();
			// ITypeBinding typeBinding = type.resolveBinding();
			// String qualifiedTypeName = typeBinding.getQualifiedName();
			TypeObject typeObject = TypeObject.extractTypeObject(type.toString());
			ArrayCreationObject creationObject = new ArrayCreationObject(typeObject);
			creationObject.setArrayCreation(arrayCreation);
			addCreation(creationObject);
		}
	}

	private void addCreation(CreationObject creationObject) {
		creationList.add(creationObject);
		if (parent != null) {
			parent.addCreation(creationObject);
		}
	}

	protected void processLiterals(List<Expression> literals) {
		for (Expression literal : literals) {
			LiteralObject literalObject = new LiteralObject(literal);
			addLiteral(literalObject);
		}
	}

	private void addLiteral(LiteralObject literalObject) {
		literalList.add(literalObject);
		if (parent != null) {
			parent.addLiteral(literalObject);
		}
	}


	protected void processThrowStatement(ThrowStatement throwStatement) {
		Expression expression = throwStatement.getExpression();
		if (expression instanceof ClassInstanceCreation) {
			/*
			 * ClassInstanceCreation creation =
			 * (ClassInstanceCreation)expression; ITypeBinding typeBinding =
			 * creation.getType().resolveBinding();
			 * addExceptionInThrowStatement(typeBinding.getQualifiedName());
			 */
			addExceptionInThrowStatement(throwStatement.toString());
		}
	}

	private void addExceptionInThrowStatement(String exception) {
		exceptionsInThrowStatements.add(exception);
		if (parent != null) {
			parent.addExceptionInThrowStatement(exception);
		}
	}

	private Set<Assignment> getMatchingAssignments(SimpleName simpleName, List<Expression> assignments) {
		Set<Assignment> matchingAssignments = new LinkedHashSet<Assignment>();
		for (Expression expression : assignments) {
			Assignment assignment = (Assignment) expression;
			Expression leftHandSide = assignment.getLeftHandSide();
			SimpleName leftHandSideName = MethodDeclarationUtility.getRightMostSimpleName(leftHandSide);
			if (leftHandSideName != null && leftHandSideName.equals(simpleName)) {
				matchingAssignments.add(assignment);
			}
		}
		return matchingAssignments;
	}

	private Set<PostfixExpression> getMatchingPostfixAssignments(SimpleName simpleName,
			List<Expression> postfixExpressions) {
		Set<PostfixExpression> matchingPostfixAssignments = new LinkedHashSet<PostfixExpression>();
		for (Expression expression : postfixExpressions) {
			PostfixExpression postfixExpression = (PostfixExpression) expression;
			Expression operand = postfixExpression.getOperand();
			SimpleName operandName = MethodDeclarationUtility.getRightMostSimpleName(operand);
			if (operandName != null && operandName.equals(simpleName)) {
				matchingPostfixAssignments.add(postfixExpression);
			}
		}
		return matchingPostfixAssignments;
	}

	private Set<PrefixExpression> getMatchingPrefixAssignments(SimpleName simpleName,
			List<Expression> prefixExpressions) {
		Set<PrefixExpression> matchingPrefixAssignments = new LinkedHashSet<PrefixExpression>();
		for (Expression expression : prefixExpressions) {
			PrefixExpression prefixExpression = (PrefixExpression) expression;
			Expression operand = prefixExpression.getOperand();
			PrefixExpression.Operator operator = prefixExpression.getOperator();
			SimpleName operandName = MethodDeclarationUtility.getRightMostSimpleName(operand);
			if (operandName != null && operandName.equals(simpleName)
					&& (operator.equals(PrefixExpression.Operator.INCREMENT)
							|| operator.equals(PrefixExpression.Operator.DECREMENT))) {
				matchingPrefixAssignments.add(prefixExpression);
			}
		}
		return matchingPrefixAssignments;
	}

/*	private void handleDefinedField(AbstractVariable variable) {
		if (variable != null) {
			PlainVariable initialVariable = variable.getInitialVariable();
			if (variable instanceof PlainVariable) {
				nonDistinctDefinedFieldsThroughThisReference.add((PlainVariable) variable);
			} else {
				if (initialVariable.isField()) {
					nonDistinctDefinedFieldsThroughFields.add(variable);
				} else if (initialVariable.isParameter()) {
					nonDistinctDefinedFieldsThroughParameters.add(variable);
				} else {
					nonDistinctDefinedFieldsThroughLocalVariables.add(variable);
				}
			}
			if (parent != null) {
				parent.handleDefinedField(variable);
			}
		}
	}

	private void handleUsedField(AbstractVariable variable) {
		if (variable != null) {
			PlainVariable initialVariable = variable.getInitialVariable();
			if (variable instanceof PlainVariable) {
				nonDistinctUsedFieldsThroughThisReference.add((PlainVariable) variable);
			} else {
				if (initialVariable.isField()) {
					nonDistinctUsedFieldsThroughFields.add(variable);
				} else if (initialVariable.isParameter()) {
					nonDistinctUsedFieldsThroughParameters.add(variable);
				} else {
					nonDistinctUsedFieldsThroughLocalVariables.add(variable);
				}
			}
			if (parent != null) {
				parent.handleUsedField(variable);
			}
		}
	}

	public List<FieldInstructionObject> getFieldInstructions() {
		return fieldInstructionList;
	}

	public List<SuperFieldInstructionObject> getSuperFieldInstructions() {
		return superFieldInstructionList;
	}*/

	public List<LocalVariableDeclarationObject> getLocalVariableDeclarations() {
		return localVariableDeclarationList;
	}

	public List<LocalVariableInstructionObject> getLocalVariableInstructions() {
		return localVariableInstructionList;
	}

	public List<CreationObject> getCreations() {
		return creationList;
	}

	public List<ClassInstanceCreationObject> getClassInstanceCreations() {
		List<ClassInstanceCreationObject> classInstanceCreations = new ArrayList<ClassInstanceCreationObject>();
		for (CreationObject creation : creationList) {
			if (creation instanceof ClassInstanceCreationObject) {
				classInstanceCreations.add((ClassInstanceCreationObject) creation);
			}
		}
		return classInstanceCreations;
	}

	public List<ArrayCreationObject> getArrayCreations() {
		List<ArrayCreationObject> arrayCreations = new ArrayList<ArrayCreationObject>();
		for (CreationObject creation : creationList) {
			if (creation instanceof ArrayCreationObject) {
				arrayCreations.add((ArrayCreationObject) creation);
			}
		}
		return arrayCreations;
	}

	public List<LiteralObject> getLiterals() {
		return literalList;
	}

	public List<AnonymousClassDeclarationObject> getAnonymousClassDeclarations() {
		return anonymousClassDeclarationList;
	}

	public Set<String> getExceptionsInThrowStatements() {
		return exceptionsInThrowStatements;
	}

/*	public boolean containsFieldInstruction(FieldInstructionObject fieldInstruction) {
		return fieldInstructionList.contains(fieldInstruction);
	}

	public Set<AbstractVariable> getDefinedFieldsThroughFields() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctDefinedFieldsThroughFields);
	}

	public Set<AbstractVariable> getUsedFieldsThroughFields() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctUsedFieldsThroughFields);
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughFields() {
		return nonDistinctDefinedFieldsThroughFields;
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughFields() {
		return nonDistinctUsedFieldsThroughFields;
	}

	public Set<AbstractVariable> getDefinedFieldsThroughParameters() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctDefinedFieldsThroughParameters);
	}

	public Set<AbstractVariable> getUsedFieldsThroughParameters() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctUsedFieldsThroughParameters);
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughParameters() {
		return nonDistinctDefinedFieldsThroughParameters;
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughParameters() {
		return nonDistinctUsedFieldsThroughParameters;
	}

	public Set<AbstractVariable> getDefinedFieldsThroughLocalVariables() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctDefinedFieldsThroughLocalVariables);
	}

	public Set<AbstractVariable> getUsedFieldsThroughLocalVariables() {
		return new LinkedHashSet<AbstractVariable>(nonDistinctUsedFieldsThroughLocalVariables);
	}

	public List<AbstractVariable> getNonDistinctDefinedFieldsThroughLocalVariables() {
		return nonDistinctDefinedFieldsThroughLocalVariables;
	}

	public List<AbstractVariable> getNonDistinctUsedFieldsThroughLocalVariables() {
		return nonDistinctUsedFieldsThroughLocalVariables;
	}

	public Set<PlainVariable> getDefinedFieldsThroughThisReference() {
		return new LinkedHashSet<PlainVariable>(nonDistinctDefinedFieldsThroughThisReference);
	}

	public List<PlainVariable> getNonDistinctDefinedFieldsThroughThisReference() {
		return nonDistinctDefinedFieldsThroughThisReference;
	}

	public Set<PlainVariable> getUsedFieldsThroughThisReference() {
		return new LinkedHashSet<PlainVariable>(nonDistinctUsedFieldsThroughThisReference);
	}

	public List<PlainVariable> getNonDistinctUsedFieldsThroughThisReference() {
		return nonDistinctUsedFieldsThroughThisReference;
	}*/

	public Set<PlainVariable> getDeclaredLocalVariables() {
		return declaredLocalVariables;
	}

	public Set<PlainVariable> getDefinedLocalVariables() {
		return definedLocalVariables;
	}

	public Set<PlainVariable> getUsedLocalVariables() {
		return usedLocalVariables;
	}

	public Map<PlainVariable, LinkedHashSet<MethodInvocationObject>> getParametersPassedAsArgumentsInMethodInvocations() {
		return parametersPassedAsArgumentsInMethodInvocations;
	}

	public Map<PlainVariable, LinkedHashSet<SuperMethodInvocationObject>> getParametersPassedAsArgumentsInSuperMethodInvocations() {
		return parametersPassedAsArgumentsInSuperMethodInvocations;
	}

	public Map<PlainVariable, LinkedHashSet<ConstructorInvocationObject>> getParametersPassedAsArgumentsInConstructorInvocations() {
		return parametersPassedAsArgumentsInConstructorInvocations;
	}

	public Map<PlainVariable, LinkedHashSet<ClassInstanceCreationObject>> getVariablesAssignedWithClassInstanceCreations() {
		return variablesAssignedWithClassInstanceCreations;
	}
}
