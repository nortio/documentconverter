package io.github.nortio.documentconverter

import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

class Converter {
    fun markdownToHtml(data: String): String {
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(data)
        return HtmlGenerator(data, parsedTree, flavour).generateHtml() + htmlHead
    }
}