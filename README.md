# Java Image Processing GUI

A desktop image-editing application built with **Java Swing** for a university programming course project (April 2025).

## Features

- Open images using a file chooser and save edited images as JPEG files
- Apply black-and-white, red/blue swap, inverted-hue, rotation, and mirror filters
- Adjust brightness with a slider
- Select and crop an image region
- Undo and redo image edits

## Technologies

Java, Swing, AWT, `BufferedImage`, `ImageIO`, and object-oriented programming.

## Run locally

Requires a Java Development Kit (JDK). From the repository root, run:

```bash
javac -d out src/assign11/*.java
java -cp out assign11.ImageProcessorProgram
```

When the application opens, select an image to edit. The application is a desktop GUI, not a browser-based application.

## Project structure

- `ImageProcessorProgram.java` — application entry point
- `ImageProcessorFrame.java` — window, menus, controls, and editing history
- `ImagePanel.java` — image display and mouse interactions
- `Image.java` — image data and transformations
- `Pixel.java` — pixel representation

## Credits

Class project by **Eamon Lynch and John Sayasith**. Original source files also credit **Prof. Martin** for contributions to `Image.java` and `ImagePanel.java`. Original source code and attribution are preserved.

## Notes

The code was originally developed as a university class project and is shared here as a programming portfolio example. The source compiles with `javac`; GUI functionality has not been independently tested in this environment.
