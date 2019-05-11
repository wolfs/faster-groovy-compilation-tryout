import org.codehaus.groovy.ast.ClassCodeVisitorSupport
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.CodeVisitorSupport
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.classgen.GeneratorContext
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.customizers.CompilationCustomizer

class CompilerTracker extends CompilationCustomizer {
    CompilerTracker() {
        super(CompilePhase.CLASS_GENERATION)
    }

    @Override
    void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
        println source.getSource().URI
        println classNode.name

        context.compileUnit.classes.each {
            println "Compile Unit: ${it.name}"
            it.innerClasses.each {
                println "Inner class: ${it.name}"
            }
        }
        classNode.innerClasses.each {
            println "Inner class (classnode): ${it.name}"
        }
        context.getCompileUnit()
        def codeVisitor = new CodeVisitorSupport() {
            @Override
            void visitClosureExpression(ClosureExpression expression) {
                println expression.getType().name
                super.visitClosureExpression(expression)
            }
        }
        def visitor = new ClassCodeVisitorSupport() {
            @Override
            void visitClosureExpression(ClosureExpression expression) {
                println expression.getType().name
                super.visitClosureExpression(expression)
            }

            @Override
            protected SourceUnit getSourceUnit() {
                return null
            }
        }
        classNode.visitContents(visitor)
    }
}

configuration.addCompilationCustomizers(new CompilerTracker())