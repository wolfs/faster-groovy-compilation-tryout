import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.classgen.GeneratorContext
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.customizers.CompilationCustomizer

class CompilerTracker extends CompilationCustomizer {
    CompilerTracker() {
        super(CompilePhase.CLASS_GENERATION)
    }

    Map<URI, List<String>> location2ClassNames = [:].withDefault { [] }

    @Override
    void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
        inspectClassNode(source, classNode)
        location2ClassNames.each { location, fqcn ->
            println "${location} -> ${fqcn.join(",")}"
        }
    }

    void inspectClassNode(SourceUnit sourceUnit, ClassNode classNode) {
        location2ClassNames.get(sourceUnit.source.URI).add(classNode.name)
        classNode.innerClasses.each {
            inspectClassNode(sourceUnit, it)
        }
    }
}

configuration.addCompilationCustomizers(new CompilerTracker())