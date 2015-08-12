package org.jboss.tools.batch.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

/**
 * Provides useful methods for working with java files.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class JavaClassEditor extends TextEditor {

	public JavaClassEditor(String string) {
		super(string);
	}

	private String name;
	
	public String getClassName(){
		getCompilationUnit().accept(new ASTVisitor() {
 
			@Override
			public boolean visit(TypeDeclaration node) {
				name = node.getName().getFullyQualifiedName();
				return false;
			}
			
		});
		return name;
	}
	
	private List<String> annotations = new ArrayList<>();

	public List<String> getClassAnnotations(){
		annotations = new ArrayList<>();
		getCompilationUnit().accept(new ASTVisitor() {

			@Override
			public boolean visit(final TypeDeclaration typeNode) {
				typeNode.accept(new ASTVisitor() {
					@Override
					public boolean visit(MarkerAnnotation node) {
						if (node.getParent().equals(typeNode)) {
							annotations.add(node.getTypeName().getFullyQualifiedName());
						}
						return false;
					}
					
					@Override
					public boolean visit(NormalAnnotation node) {
						if (node.getParent().equals(typeNode)) {
							annotations.add(node.getTypeName().getFullyQualifiedName());
						}
						return false;
					}
					
					@Override
					public boolean visit(SingleMemberAnnotation node) {
						if (node.getParent().equals(typeNode)) {
							annotations.add(node.getTypeName().getFullyQualifiedName());
							node.getValue();
						}
						return false;
					}
				});
				return false;
			}
			
		});
		return annotations;
	}
	
	private String classAnnotationValue;

	public String getClassAnnotationValue(String annotation){
		classAnnotationValue = null;
		getCompilationUnit().accept(new ASTVisitor() {

			@Override
			public boolean visit(final TypeDeclaration typeNode) {
				typeNode.accept(new ASTVisitor() {
					@Override
					public boolean visit(SingleMemberAnnotation node) {
						if (node.getParent().equals(typeNode)) {
							classAnnotationValue = node.getValue().toString();
							StringLiteral literal = (StringLiteral) node.getValue();
							classAnnotationValue = literal.getLiteralValue();
						}
						return false;
					}
				});
				return false;
			}
			
		});
		return classAnnotationValue;
	}
	
	private String superClass;

	public String getSuperClass(){
		superClass = null;
		getCompilationUnit().accept(new ASTVisitor() {

			@Override
			public boolean visit(final TypeDeclaration typeNode) {
				if (typeNode.getSuperclassType() != null && typeNode.getSuperclassType().isSimpleType()){
					SimpleType superClassType = (SimpleType) typeNode.getSuperclassType();
					superClass = superClassType.getName().getFullyQualifiedName();
				}
				return false;
			}
		});
		
		return superClass;
	}

	private List<String> interfaces = new ArrayList<>();
	
	public List<String> getInterfaces(){
		interfaces = new ArrayList<>();
		getCompilationUnit().accept(new ASTVisitor() {

			@Override
			public boolean visit(final TypeDeclaration typeNode) {
				for (Object o : typeNode.superInterfaceTypes()){
					if (o instanceof SimpleType){
						SimpleType superClassType = (SimpleType) o;
						interfaces.add(superClassType.getName().getFullyQualifiedName());
					}					
				}
				return false;
			}
		});
		
		return interfaces;
	}

	private CompilationUnit getCompilationUnit(){
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(getText().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		return(CompilationUnit) parser.createAST(null);
	}
}
