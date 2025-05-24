# README.md

# Ada Hello World Project

This project is a simple Ada program that outputs "Hello, World!" to the console. It serves as an introductory example for those learning the Ada programming language.

## Project Structure

The project consists of the following files:

- `src/hello.adb`: The main Ada program that prints "Hello, World!".
- `README.md`: This documentation file.
- `project.gpr`: The project file for building the Ada program.

## How to Compile and Run

To compile and run the Ada program, follow these steps:

1. **Install an Ada Compiler**: Make sure you have an Ada compiler installed on your system. GNAT is a popular choice.

2. **Navigate to the Project Directory**: Open a terminal and navigate to the root directory of the project.

3. **Compile the Program**: Use the following command to compile the program:
   ```
   gprbuild -P project.gpr
   ```

4. **Run the Program**: After successful compilation, run the program with the following command:
   ```
   ./src/hello
   ```

You should see the output:
```
Hello, World!
```

## License

This project is licensed under the MIT License. See the LICENSE file for more details.