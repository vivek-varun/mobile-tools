import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.nio.file.Files

class LocPlugin : Plugin<Project> {

    companion object {
        private const val PLUGIN_NAME = "metrics-loc"
    }

    override fun apply(project: Project) {
        val languages = mutableListOf("kotlin", "java")
        languages.forEach { language ->
            project.task(PLUGIN_NAME)
                .doLast {
                    val srcFolder = StringBuilder()
                        .append("src")
                        .append(File.separator)
                        .append("main")
                        .append(File.separator)
                        .append(language)
                        .toString()
                    val srcFiles = project.fileTree(srcFolder).files
                    val totalFiles = srcFiles.size
                    val loc = srcFiles
                        .parallelStream()
                        .map(File::toPath)
                        .flatMap {
                            try {
                                Files.lines(it)
                            } catch (exception: IOException) {
                                exception.printStackTrace()
                                throw RuntimeException(exception.message, exception)
                            }
                        }
                        .count()
                    println("Module: ${project.displayName}")
                    println("Language: $language")
                    println("Number of files: $totalFiles")
                    println("Number of lines of Code: $loc")
                }
        }
    }
}
