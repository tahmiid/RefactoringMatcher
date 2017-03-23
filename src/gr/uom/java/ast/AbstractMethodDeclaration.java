package gr.uom.java.ast;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import gr.uom.java.ast.decomposition.MethodBodyObject;
import gr.uom.java.ast.decomposition.cfg.PlainVariable;

public interface AbstractMethodDeclaration {

    public String getName();

	public Access getAccess();

    public MethodDeclaration getMethodDeclaration();

    public MethodBodyObject getMethodBody();

    public String getClassName();
    
    public ListIterator<ParameterObject> getParameterListIterator();

    public ParameterObject getParameter(int position);

    public List<LocalVariableDeclarationObject> getLocalVariableDeclarations();
    
    public List<LocalVariableInstructionObject> getLocalVariableInstructions();

	public List<CreationObject> getCreations();

	public List<LiteralObject> getLiterals();
	
	public List<AnonymousClassDeclarationObject> getAnonymousClassDeclarations();

	public Set<String> getExceptionsInThrowStatements();
	
	public Set<String> getExceptionsInJavaDocThrows();

	public Set<PlainVariable> getDeclaredLocalVariables();

	public Set<PlainVariable> getDefinedLocalVariables();

	public Set<PlainVariable> getUsedLocalVariables();

    public List<TypeObject> getParameterTypeList();

    public List<String> getParameterList();

    public String getSignature();

    public boolean isAbstract();
}
