# Console-Builder

This is a small builder api for a console application. It supports validation, completion and history. Used libraries are jline and guava (compile and runtime).

## Features
* Validation
* Completion
* Conversion                                                  
* Extensive enum support (completion, conversion and validation)
* History
* Question specific history                                   
* IOExceptions from jline encapsulated into runtime exceptions
* Builder api
* User input as end result of builder

## Usage

### Example 1 (Validation)
    
    String firstName = Ask.ask("Please enter your first name")
            .validateWith(Validators.notEmpty("Empty String not allowed"))
            .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Invalid format"))
            .answer();
            
    System.out.println("Hello " + firstName);
    
Result in terminal: 
    
    Please enter your first name
    > |
    Empty String not allowed
    Invalid format
                      
    Please enter your first name
    > a|
    Invalid format  
                      
    Please enter your first name
    > Dominik|
    Hello Dominik
    
### Example 2 (completion and validation, enum magic)
              
    public enum Gender { MALE, FEMALE }
    ...                                
    Gender gender = ask("Please enter your gender")
            .answer(Gender.class, "Please enter valid gender");
    System.out.println("Gender " + gender.toString());  
    
Result in terminal: 

    Please enter your gender     
    > robot|
    Please enter valid gender
    
    Please enter your gender
    > <Tab>|
    female   male 
    > m<Tab>| 
    > male|
    Gender male

### Example 3 (completion, value transformation)
                                                    
    Integer age = ask("how old are you?")
            .completeWith(new StringsCompleter("22", "33", "44", "55", "66"))
            .validateWith(validateAge("please enter valid age"))
            .answer(toInteger());          
    System.out.println("Age " + age);  
    
    
Result in terminal: 

    how old are you?
    > <Tab>|
    22   33   44   55   66   
    > 4<Tab>|
    > 44|
    Age 44
    
    
### Example 4 (history with specific file)
    
    String favoriteColor = Ask.ask("What ist your favorite color?")
            .useHistoryFrom("color")
            .answer();                                   
    System.out.println("Color " + color); 
    
Result in terminal: 

    What ist your favorite color?
    > red|
    Color red
    
Second run

    What ist your favorite color?
    > <ArrowUp>|
    > red|     
    Color red
    
        