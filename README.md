# Orange Cart

Orange Cart is a Java-based shopping cart application built as a side project. The idea started as a friendly duel: a friend wanted to build his own cart system in Python, so I decided to join in and create mine in Java. The project demonstrates modern JavaFX UI techniques, background task handling, and a modular code structure.

## Features
- JavaFX user interface with FXML views
- Shopping cart management
- Payment gateway simulation (with non-blocking UI)
- Modular MVC structure
- Nigerian currency formatting
- Pulse monitoring utility

## Project Structure
- `src/main/java/com/ocart/orange_cart/` - Main source code
  - `controller/` - JavaFX controllers for views
  - `db/` - Database manager (stub)
  - `model/` - Data models (Product, ShoppingCart)
  - `util/` - Utility classes
  - `view/` - FXML view files
  - `assets/` - CSS theme
- `pom.xml` - Maven build file

## How to Run
1. Ensure you have Java 17+ and Maven installed.
2. Clone the repository.
3. Run with Maven:
   ```sh
   mvn javafx:run
   ```
   Or use your IDE to run `Main.java`.

## Why This Project?
This was a fun side project inspired by a friend's challenge to build a Jumia Clone. It was a great way to explore JavaFX, concurrency, and best practices in UI responsiveness.

## License
MIT License
