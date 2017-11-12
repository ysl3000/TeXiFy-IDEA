package nl.rubensten.texifyidea.lang

import nl.rubensten.texifyidea.lang.BibtexDefaultEntryType.*

/**
 * @author Ruben Schellekens
 */
enum class BibtexDefaultEntry(
        override val token: String,
        override val description: String,
        override val required: Array<BibtexEntryField>,
        override val optional: Array<BibtexEntryField>
) : BibtexEntryType {

    // Regular entry types.
    ARTICLE("article",
            "An article from a journal or magazine.",
            arrayOf(AUTHOR, TITLE, JOURNAL, YEAR),
            arrayOf(NUMBER, PAGES, MONTH, NOTE, VOLUME, KEY)
    ),
    BOOK("book",
            "A book with an explicit publisher.",
            arrayOf(AUTHOR, TITLE, PUBLISHER, YEAR),
            arrayOf(EDITION, VOLUME, NUMBER, SERIES, ADDRESS, EDITION, MONTH, NOTE, KEY)
    ),
    BOOKLET("booklet",
            "A work that is printed and bound, but without a named publisher or sponsoring institution.",
            arrayOf(TITLE),
            arrayOf(AUTHOR, HOWPUBLISHED, ADDRESS, MONTH, YEAR, NOTE, KEY)
    ),
    CONFERENCE("conference",
            "The same as inproceedings, included for Scribe compatibility.",
            arrayOf(AUTHOR, TITLE, BOOKTITLE, YEAR),
            arrayOf(EDITOR, VOLUME, NUMBER, SERIES, PAGES, ADDRESS, MONTH, ORGANISATION, PUBLISHER, NOTE, KEY)
    ),
    INBOOK("inbook",
            "A part of a book, usually untitled. May be a chapter (or section, etc.) and/or a range of pages.",
            arrayOf(AUTHOR, TITLE, PAGES, PUBLISHER, YEAR),
            arrayOf(EDITOR, CHAPTER, VOLUME, NUMBER, SERIES, TYPE, ADDRESS, EDITION, MONTH, NOTE, KEY)
    ),
    INCOLLECTION("incollection",
            "A part of a book having its own title.",
            arrayOf(AUTHOR, TITLE, BOOKTITLE, PUBLISHER, YEAR),
            arrayOf(EDITOR, VOLUME, NUMBER, SERIES, TYPE, CHAPTER, PAGES, ADDRESS, EDITION, MONTH, NOTE, KEY)
    ),
    INPROCEEDINGS("inproceedings",
            "An article in a conference proceedings.",
            arrayOf(AUTHOR, TITLE, BOOKTITLE, YEAR),
            arrayOf(EDITOR, VOLUME, NUMBER, SERIES, PAGES, ADDRESS, MONTH, ORGANISATION, PUBLISHER, NOTE, KEY)
    ),
    MANUAL("manual",
            "Technical documentation.",
            arrayOf(TITLE),
            arrayOf(AUTHOR, ORGANISATION, ADDRESS, EDITION, MONTH, YEAR, NOTE, KEY)
    ),
    MASTERTHESIS("masterthesis",
            "A Master's thesis.",
            arrayOf(AUTHOR, TITLE, SCHOOL, YEAR),
            arrayOf(TYPE, ADDRESS, MONTH, NOTE, KEY)
    ),
    MISC("misc",
            "For use when nothing else fits.",
            emptyArray(),
            arrayOf(AUTHOR, TITLE, HOWPUBLISHED, MONTH, YEAR, NOTE, KEY)
    ),
    PHDTHESIS("phdthesis",
            "A Ph.D. thesis.",
            arrayOf(AUTHOR, TITLE, SCHOOL, YEAR),
            arrayOf(TYPE, ADDRESS, MONTH, NOTE, KEY)
    ),
    PROCEEDINGS("proceedings",
            "The proceedings of a conference.",
            arrayOf(TITLE, YEAR),
            arrayOf(EDITOR, VOLUME, NUMBER, SERIES, ADDRESS, MONTH, PUBLISHER, ORGANISATION, NOTE, KEY)
    ),
    TECHREPORT("techreport",
            "A report published by a school or other institution, usually numbered within a series.",
            arrayOf(AUTHOR, TITLE, INSTITUTION, YEAR),
            arrayOf(TYPE, NUMBER, ADDRESS, MONTH, NOTE, KEY)
    ),
    UNPUBLISHED("unpublished",
            "A document having an author and title, but not formally published.",
            arrayOf(AUTHOR, TITLE, NOTE),
            arrayOf(MONTH, YEAR, KEY)
    ),

    // Special entries
    STRING("string",
            "Define a string to be used later on in the bibliography",
            emptyArray(),
            emptyArray()
    ),
    PREAMBLE("preamble",
            "You can define some LaTeX commands that will be included in the .bbl file generated by BibTex using the preamble",
            emptyArray(),
            emptyArray()
    );

    companion object {

        private val LOOKUP: MutableMap<String, BibtexEntryType> = HashMap()

        init {
            for (entry in values()) {
                LOOKUP.put(entry.token, entry)
            }
        }

        @JvmStatic
        operator fun get(token: String): BibtexEntryType? {
            var trimmedToken = token.toLowerCase()
            if (token.startsWith("@")) {
                trimmedToken = token.substring(1)
            }
            return LOOKUP[trimmedToken]
        }
    }
}