# Console-Builder

This is a small builder api based on JLine and Guava.

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
    > Dominik
    Hello Dominik
    
### Example 2 (completion, value transformation)

    Gender gender = Ask.ask("Please enter your gender")
            .validateWith(validateGender("Please enter valid gender"))
            .completeWith(new StringsCompleter(FluentIterable.from(newArrayList(Gender.values())).transform(fromGender()).toList()))
            .answer(toGender());  
    System.out.println("Gender " + gender.toString());
    
Result in terminal: 

    Please enter your gender
    > <Tab>
    FEMALE   MALE     
    > MA<TAB>
    > MALE
    Gender MALE
    
### Example 3 (history with specific file)
    
    String favoriteColor = Ask.ask("What ist your favorite color?")
            .useHistoryFrom("color")
            .answer();                                   
    System.out.println("Color " + color); 
    
Result in terminal: 

    What ist your favorite color?
    > red
    Color red
    
Second run

    What ist your favorite color?
    > <ArrowUp>
    > red     
    Color red
    
        