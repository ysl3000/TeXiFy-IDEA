package nl.rubensten.texifyidea.inspections.latex

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import nl.rubensten.texifyidea.insight.InsightGroup
import nl.rubensten.texifyidea.inspections.TexifyInspectionBase
import nl.rubensten.texifyidea.util.*

/**
 * @author Ruben Schellekens, Sten Wessel
 */
open class LatexDuplicateLabelInspection : TexifyInspectionBase() {

    override fun getInspectionGroup() = InsightGroup.LATEX

    override fun getDisplayName() = "Duplicate labels"

    override fun getInspectionId(): String = "DuplicateLabel"

    override fun inspectFile(file: PsiFile, manager: InspectionManager, isOntheFly: Boolean): List<ProblemDescriptor> {
        val descriptors = descriptorList()

        // Fill up a set of labels.
        val labels = mutableMapOf<String, MutableSet<String>>()
        val firstPass = mutableMapOf<String, MutableSet<String>>()
        for (cmd in file.commandsInFileSet()) {
            val labelName = cmd.requiredParameter(0) ?: continue

            if (cmd.name != "\\label" && cmd.name != "\\bibitem") {
                continue
            }

            if (firstPass[cmd.name!!]?.let { labelName in it } == true) {
                labels.getOrPut(cmd.name!!, { mutableSetOf() }).add(labelName)
                continue
            }
            firstPass.getOrPut(cmd.name!!, { mutableSetOf() }).add(labelName)
        }

        for (id in file.bibtexIdsInFileSet()) {
            val labelName = id.idName()

            if (firstPass["\\bibitem"]?.let { labelName in it } == true) {
                labels.getOrPut("\\bibitem", { mutableSetOf() }).add(labelName)
                continue
            }
            firstPass.getOrPut("\\bibitem", { mutableSetOf() }).add(labelName)
        }

        // Check labels in file.
        for (cmd in file.commandsInFile()) {
            if (cmd.name != "\\label" && cmd.name != "\\bibitem") {
                continue
            }

            val labelName = cmd.requiredParameter(0) ?: continue
            if (labelName in labels[cmd.name!!] ?: continue) {
                descriptors.add(manager.createProblemDescriptor(
                        cmd,
                        TextRange.from(cmd.commandToken.textLength + 1, labelName.length),
                        "Duplicate label '$labelName'",
                        ProblemHighlightType.GENERIC_ERROR,
                        isOntheFly
                ))
            }
        }

        return descriptors
    }
}
