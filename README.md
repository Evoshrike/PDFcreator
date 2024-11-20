# PDF Creator
## Introduction
This PDF creator uses iText to parse a txt file following a series of commands to render a PDF with styling, formatting 
and seperation of paragraphs. It is built with Maven, so should work in any IDE without any configuration (I have tested with both
IntelliJ and Eclipse) 
## Testing
- In `src/tests/java` there are a series of unit tests for the `Parser` and `Writer` classes. The `Writer` tests will produce a file `unitTestOutput.pdf` however the result of each test will be overwritten by the next.
-  In `src/main/java`, running the `Main` class will produce a `testoutput.pdf` file with the example PDF from the brief repeated multiple times to demonstrate pagination. 
## Classes
### Function
- Enum representing the different types of command
### LineContent
- Interface representing different types of content that can be on a line in the `input .txt` file
### TextLine
- Implements `LineContent`
- Wrapper for a string of text
- `getText()` - returns text as `String`
### FunctionLine
- Implements `LineContent`
- Wrapper for a `Function`
- Special case indent - also has a field for `indentAmt`. 
- 2 constructors - `FunctionLine(Function f)` and `FunctionLine(Function f, int indentAmt)`
- The 2nd constructor should be used if and only if `f == INDENT`. Use otherwise will throw an exception
- The function parameter in the 2nd constructor is only there for consistency and clarity.
### Parser
- class with one static method, constructor private (so cannot instantiate).
- method `parse(List<> strings)` - parses a list of Strings(from .txt file) to return a list of `LineContent` items.
### Writer
- Class which writes to a `Document`, given in constructor
- Constructor parameters: `Document` (to write to), `List(LineContent)` lines - lines to write to PDF (either `TextLines` or `FunctionLines`)
- method `write()` iterates through lines and handles each as appropriate: 	
	- Handles commands by updating state variables (bold, italic etc.). Uses private method `handleCommand()` to handle this for improved readability
	- Creates a `Text` element for each line of text, applying styling (bold, italic, etc.) conditionally depending on current state.
	- `Text` elements are added to a `Paragraph` object (initialised on constructor call). Commands PARAGRAPH and NOFILL flush `Paragraph` to the document and create a new `Paragraph` instance.
	- The final `Paragraph` is flushed to document at the end of the method call
### PDFCreator
- Class which takes an input and output filepath, reads from the `input .txt` file and produces an output file. 
- Will gracefully handle the absence of an `input .txt`, or an error creating a file at the output filepath. 
- method `createPDF()` performs all this functionality:
	- reads from .txt
	- uses `Parser` to parse lines into `LineContent` objects
	- uses `Writer` to turn lines into a PDF
## Design decisions / tradeoffs
### Static parse method vs non-static write method
- I decided to make my `parse()` method static, as the class was simple enough that packing all necessary state / behaviour into a single method did not massively adversely affect readability
- This meant the call to `parse()` from `createPDF()` has lower overhead (no object creation) and is slightly less verbose (no constructor call)
- Although it is slightly non-standard for OOP - normal practice would require a `Parser` object to be created
- With my `Writer` class, the `write()` method relies on other private methods to execute, as well as internal state
- This is because internalising all the necessary behaviour and state into a single method would have made it incredibly long and somewhat unreadable
- Hence I chose to accept slightly higher verbosity in the use of my `Writer` class, in exchange for a more readable implementation. 
### NO_FILL being an implicit PARAGRAPH command (in addition to NO_FILL)
- In the example .txt sample, `NO_FILL` is used at the start without `PARAGRAPH` in front of it. While this does not create any issues initially, if you repeat the demo content multiple times, there was initially no line break between the last line and the new title. 
- One option would be to require a `PARAGRAPH` command at the start of every chunk of text, but this is not given in the brief. Hence to allow paragraphs which aren't explicitly specified as `PARAGRAPH` by command to be seperated from each other, I made the decision to make `NO_FILL` have additional functionality of flushing the current paragraph (akin to the `PARAGRAPH` command)
- This is a somewhat imperfect solution as it does not say in the brief that `NO_FILL` should do this, however it is implausible to imagine a context where someone wanted to have half a paragraph filled and half unfilled (if that is even possible). 
- In the demo, `FILL` is used after `PARAGRAPH`, but `NO_FILL` is used without `PARAGRAPH`, so it seemed sensible to give `NO_FILL` this additional functionality, based on the intuition that it is a reset command so could also reset the paragraph. 
### Read functionality within PDF creator
- While deciding on my class hierarchy to use, I considered  making a `FileReader` class to explicitly seperate concerns from `PDFCreator`, so that `PDFCreator` uses a `Reader`, a `Parser` and then a `Writer` - a logical progression. 
- However, since the reading from file can be done in a one-line method call using standard libraries, it seemed excessive to seperate this into its own class.
### Write method using conditional instanceof
- This syntax is slightly ugly and could easily be replaced by having a `writeToParagraph()` method in the `LineContent` interface, and implementing in `FunctionLine` and `TextLine`
- However, this would appear to break the encapsulation between these classes - semantically, `LineContent` objects are just supposed to represent the content of the lines in a manageable way
- There is not meant to be any dependency between these classes and (for example) the use of the iText library - this should be contained purely within `Writer`.
-  Hence the `write()` method is purely executed in the `Writer` class.
### Lack of unit tests for writing bold / italic
- These functionalities do both work (as demonstrated in the main test), however when browsing the iText API documentation there appears to be no way to verify that a `Text` object has its text style set to bold
- Various possible parameters e.g. Stroke width, font weight all returned null when tested.
- The implementation of `setBold()` seems to vary depending on font, and I cannot find any retrievable metadata in either the `Text` or `Paragraph` object that can confirm the object contains bold text. 
- The same applies for italics, hence there are no unit tests for either. While this is far from ideal, the overall test does confirm the functionalities work as intended. 
- I considered manually setting parameters such as font weight so that the output would be testable, but this seemed a lot more verbose and unnecessarily complicated to fix something that isn't broken.
### Hardcoded value in Writer constructor for indent amount
- I had varied values and thought `15.0` seemed to produce good results, so stayed with that
- The value is set in the constructor so easily modifiable to anyone modifying the `Writer` class
- I could have included a `setIndentAmt()` method, but this seemed unnecessary given existing functionality for changing the indent with the `INDENT` command
### Writer constructor has final lines field
- It would have been feasible to have a method to update the `lines` field, but this seemed unnecessary since to write multiple lists of `LineContent` to one document, you could use multiple `Writer` objects 
## Acknowledgments
- The iText library (https://github.com/itext) was crucial in making this possible
- This was a fun and engaging project to complete, so thank you to whoever had the idea to set this assignment
- If you made it this far, thank you for taking the time to read! 😀


	