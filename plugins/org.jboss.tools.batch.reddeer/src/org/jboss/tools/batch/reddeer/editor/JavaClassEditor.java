package org.jboss.tools.batch.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
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

	public String getClassName(){
		ClassNameVisitor visitor = new ClassNameVisitor();
		getCompilationUnit().accept(visitor);
		return visitor.getName();
	}

	public String getSuperClass(){
		SuperClassVisitor visitor = new SuperClassVisitor();
		getCompilationUnit().accept(visitor);
		return visitor.getName();
	}

	public List<String> getInterfaces(){
		ClassInterfacesVisitor visitor = new ClassInterfacesVisitor();
		getCompilationUnit().accept(visitor);
		return visitor.getInterfaces();
	}
	
	public List<String> getClassAnnotations(String className){
		AnnotationVisitor annotationVisitor = new AnnotationVisitor();
		ClassAnnotationVisitor visitor = new ClassAnnotationVisitor(className, annotationVisitor);
		getCompilationUnit().accept(visitor);
		return annotationVisitor.getAnnotations();
	}

	public String getClassAnnotationValue(String className, String annotation){
		AnnotationValueVisitor annotationVisitor = new AnnotationValueVisitor(annotation);
		ClassAnnotationVisitor visitor = new ClassAnnotationVisitor(className, annotationVisitor);
		getCompilationUnit().accept(visitor);
		return annotationVisitor.getValue();
	}

	public List<String> getFieldAnnotations(String variableName){
		VariableAnnotationVisitor visitor = new VariableAnnotationVisitor(variableName);
		getCompilationUnit().accept(visitor);
		return visitor.getAnnotations();
	}

	private CompilationUnit getCompilationUnit(){
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(getText().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		return(CompilationUnit) parser.createAST(null);
	}

	private static class ClassNameVisitor extends ASTVisitor {

		private String name;

		@Override
		public boolean visit(TypeDeclaration node) {
			name = node.getName().getFullyQualifiedName();
			return false;
		}

		public String getName() {
			return name;
		}
	}
	
	private static class SuperClassVisitor extends ASTVisitor {

		private String name;

		@Override
		public boolean visit(final TypeDeclaration typeNode) {
			if (typeNode.getSuperclassType() != null && typeNode.getSuperclassType().isSimpleType()){
				SimpleType superClassType = (SimpleType) typeNode.getSuperclassType();
				name = superClassType.getName().getFullyQualifiedName();
			}
			return false;
		}

		public String getName() {
			return name;
		}
	}
	
	private static class ClassInterfacesVisitor extends ASTVisitor {

		private List<String> interfaces = new ArrayList<String>();

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

		public List<String> getInterfaces() {
			return interfaces;
		}
	}

	private static class ClassAnnotationVisitor extends ASTVisitor {

		private AnnotationVisitor visitor;
		
		private AnnotationValueVisitor valueVisitor;

		private String className;

		public ClassAnnotationVisitor(String className, AnnotationVisitor visitor) {
			this.visitor = visitor;
			this.className = className;
		}

		public ClassAnnotationVisitor(String className, AnnotationValueVisitor annotationVisitor) {
			this.valueVisitor = annotationVisitor;
			this.className = className;
		}

		@Override
		public boolean visit(final TypeDeclaration typeNode) {
			if (typeNode.getName().getIdentifier().equals(className)){
				if (visitor != null) {
					visitor.setParent(typeNode);
					typeNode.accept(visitor);	
				} else {
					valueVisitor.setParent(typeNode);
					typeNode.accept(valueVisitor);
				}
			}
			return false;
		}
	}

	private static class VariableAnnotationVisitor extends ASTVisitor {

		private List<String> annotations = new ArrayList<String>();

		private String variableName;

		public VariableAnnotationVisitor(String variableName) {
			this.variableName = variableName;
		}

		@Override
		public boolean visit(final FieldDeclaration fieldNode) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) fieldNode.fragments().get(0);

			if (fragment.getName().getIdentifier().equals(variableName)){
				AnnotationVisitor visitor = new AnnotationVisitor(fieldNode);
				fieldNode.accept(visitor);
				annotations = visitor.getAnnotations();
			}
			return false;
		}

		public List<String> getAnnotations() {
			return annotations;
		}
	}

	private static class AnnotationVisitor extends ASTVisitor {

		private List<String> annotations = new ArrayList<String>();

		private ASTNode parent;

		public AnnotationVisitor() {
			// parent will be set
		}
		
		public AnnotationVisitor(ASTNode parent) {
			this.parent = parent;
		}

		@Override
		public boolean visit(MarkerAnnotation node) {
			if (node.getParent().equals(parent)){
				annotations.add(node.getTypeName().getFullyQualifiedName());
			}
			return false;
		}

		@Override
		public boolean visit(NormalAnnotation node) {
			if (node.getParent().equals(parent)){
				annotations.add(node.getTypeName().getFullyQualifiedName());
			}
			return false;
		}

		@Override
		public boolean visit(SingleMemberAnnotation node) {
			if (node.getParent().equals(parent)){
				annotations.add(node.getTypeName().getFullyQualifiedName());
			}
			return false;
		}

		public List<String> getAnnotations() {
			return annotations;
		}
		
		public void setParent(ASTNode parent) {
			this.parent = parent;
		}
	}
	
	private static class AnnotationValueVisitor extends ASTVisitor {

		private String annotation;
		
		private String value;

		private ASTNode parent;

		public AnnotationValueVisitor(String annotation) {
			this.annotation = annotation;
		}
		
		@Override
		public boolean visit(SingleMemberAnnotation node) {
			if (node.getParent().equals(parent)) {
				if (node.getTypeName().getFullyQualifiedName().equals(annotation)){
					StringLiteral literal = (StringLiteral) node.getValue();
					value = literal.getLiteralValue();					
				}
			}
			return false;
		}

		public String getValue() {
			return value;
		}
		
		public void setParent(ASTNode parent) {
			this.parent = parent;
		}
	}
}
