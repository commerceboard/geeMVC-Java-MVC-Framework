# Geemvc

Geemvc is a fast lightweight MVC-framework written for Java8+. All classes within Geemvc have been injected by Google-Guice, making it the most adaptable and flexible Java MVC-framework. In order to simplify extending Geemvc we have ensured that no private or static fields and methods exist, therefore making it the No. 1 choice for framework developers that need a good starting point. In many cases however it is not necessary to extend and override classes with Google-Guice. Instead, you simply create your own adapters that are automatically loaded by Geemvc.

## Goals

* Make it very easy to map requests to controller handler methods.
* Simplify validation and enable the usage of javax.validation checks (JSR-303).
* Integrate the usage of JSR-311 annotations (@Path, @GET, @Produces ...) to simplify the creation of REST APIs.
* Make it fun and easy to create modern webapps.
* Allow the framework itself to be very simple to extend by implementing adapter classes or by directly overriding functionality via Google-Guice injection.
* Increase the flexibility by allowing routing and validation rules to be defined in either Javascript, Groovy or MVEL.

## Motivation

There are some very good MVC-frameworks out there - so why create another? When testing various MVC-frameworks for a large and very flexible E-Commerce platform we noticed that, although most of them meet our needs to a certain degree, there was always something that the framework lacked in order to completely fullfill our goals. And although the main frameworks offer good extension points, like adapter classes etc, or to override certain classes by configuration, there always seemed to be some caveat like private or static fields and methods, which made it impossible to extend the functionlity as needed. After a year of creating workarounds in order to make the required changes we made the decision to create our own MVC-framework, which is fun and simple to use and enables you to change practically any class to your needs.

## Requirements

* Java8+.
* Java8+ compatible servlet container.

## Example Controller
Simply annotate your controller and handler method and Geemvc will automatically find it.

```java
@Controller
@Request("/hello")
public class HelloWorldController {
    
    @Request("world")
    public String helloWorld() {
        System.out.println("Well done! You have successfully called the /hello/world controller.");        
        
        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}
```

## Passing URI Parameters to your Controller
Geemvc will automatically parse specified parameters out of the request URI and make them available to the handler method.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    @Request("/world/{id}")
    public String helloWorld(@PathParam Long id) {
        System.out.println("Well done! You can now use the id path parameter in your code. The id is: " + id);

        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}
```

## Passing Query Parameters to your Controller
Of course the same works for query parameters. Simply specify them in your method signature. The same goes for header, cookie or session values. Just use the respective annotations in your handler methods: @Header, @Cookie or @Session. 

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    @Request("/world/{id}")
    public String helloWorld(@PathParam Long id, @Param String myQueryParam) {
        System.out.println("Thanks, you sent the following query parameter: " + myQueryParam);

        return "forward: /WEB-INF/jsp/hello-world.jsp";
    }
}    
```

## Passing Parameters to the View
Here we are getting some fictitious value from an injected service and passing it to the view - our JSP page.

```java
@Controller
@Request("/hello")
public class HelloWorldController {

    protected Service someService;

    @Inject
    protected HelloWorldController(Service someService) {
        this.someService = someService;
    }

    @Request("/world/{id}")
    public View helloWorld(@PathParam Long id, @Param String myQueryParam) {
        System.out.println("Cool, I am passing the parameter 'myViewParam' to the view!");

        return Views.forward("/WEB-INF/jsp/hello-world.jsp")
                .bind("myViewParam", someService.getById(id));
    }
}
```
